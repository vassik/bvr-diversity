package no.sintef.bvr.constraints.rewrite;

import java.util.List;
import java.util.Map;
import no.sintef.bvr.constraints.LogicalExpression;
import no.sintef.bvr.constraints.Visitor;
import static no.sintef.bvr.constraints.Builder.not;
import static no.sintef.bvr.constraints.Builder.feature;

public class Rewrite {

    private final LogicalExpression target;
    private final Factory factory;
    
    public Rewrite(LogicalExpression target) {
        this(target, new Factory());
    }

    public Rewrite(LogicalExpression target, Factory factory) {
        this.target = target;
        this.factory = factory;
    }

    public LogicalExpression toCNF() {
        return target
                .accept(factory.implicationRemover())
                .accept(factory.pushNegationInward())
                .accept(factory.disjunctionDistributor());
    }

    public int[][] toDimacsCNF() {
        final List<LogicalExpression> clauses = toCNF().accept(factory.conjunctionExtractor());
        int[][] results = new int[clauses.size()][];
        for (int index = 0; index < clauses.size(); index++) {
            LogicalExpression eachClause = clauses.get(index);
            results[index] = factory.rewrite(eachClause).toDimacsClause();
        }
        return results;
    }

    int[] toDimacsClause() {
        final Map<Integer, Boolean> isSelected = target.accept(factory.featureIndexExtractor());
        final int[] result = new int[isSelected.size()];
        int index = 0;
        for (Integer eachFeatureIndex : isSelected.keySet()) {
            result[index] = convert(eachFeatureIndex, isSelected.get(eachFeatureIndex));
            index++;
        }
        return result;
    }


    private int convert(Integer eachFeatureIndex, final boolean isSelected) {
        final int shiftedIndex = eachFeatureIndex + 1;
        if (isSelected) {
            return shiftedIndex;
        }
        return -shiftedIndex;

    }

}


