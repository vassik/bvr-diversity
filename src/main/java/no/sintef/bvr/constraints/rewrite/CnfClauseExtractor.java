package no.sintef.bvr.constraints.rewrite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import no.sintef.bvr.constraints.LogicalExpression;
import no.sintef.bvr.constraints.Visitor;
import static no.sintef.bvr.constraints.Builder.feature;
import static no.sintef.bvr.constraints.Builder.not;

/**
 * Extract CNF clauses from the formula
 */
public class CnfClauseExtractor implements Visitor<List<LogicalExpression>> {

    protected List<LogicalExpression> defaultValue() {
        return new ArrayList<>();
    }

    @Override
    public List<LogicalExpression> onConjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        List<LogicalExpression> result = new LinkedList<>();
        result.addAll(leftOperand.accept(this));
        result.addAll(rightOperand.accept(this));
        return result;
    }

    @Override
    public List<LogicalExpression> onDisjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return Collections.singletonList(leftOperand.or(rightOperand));
    }

    @Override
    public List<LogicalExpression> onImplication(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        throw new IllegalStateException("The expression is not in conjunctive normal form (implication found)");
    }

    @Override
    public List<LogicalExpression> onNegation(LogicalExpression operand) {
        return Collections.singletonList(not(operand));
    }

    @Override
    public List<LogicalExpression> onFeatureByIndex(int featureIndex) {
        return Collections.singletonList(feature(featureIndex));
    }

    @Override
    public List<LogicalExpression> onFeatureByName(String featureName) {
        return Collections.singletonList(feature(featureName));
    }

}
