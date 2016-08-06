package no.sintef.bvr.constraints.rewrite;

import java.util.List;
import java.util.Map;
import no.sintef.bvr.constraints.LogicalExpression;
import no.sintef.bvr.constraints.Visitor;

/**
 * Provide various type of rewriter and converters
 */
public class Factory {
    
    public Rewrite rewrite(LogicalExpression expression) {
        return new Rewrite(expression, this);
    }
    
    public Visitor<List<LogicalExpression>> conjunctionExtractor() {
        return new CnfClauseExtractor();
    }

    public Visitor<Map<Integer, Boolean>> featureIndexExtractor() {
        return new DimacsConverter();
    }
    
    public Visitor<LogicalExpression> implicationRemover() {
        return new ImplicationRemover();
    }
    
    public Visitor<LogicalExpression> pushNegationInward() {
        return new PushNegationInward();
    }
    
    public Visitor<LogicalExpression> disjunctionDistributor() {
        return new DistributeDisjunctionOverConjunction();
    }

}
