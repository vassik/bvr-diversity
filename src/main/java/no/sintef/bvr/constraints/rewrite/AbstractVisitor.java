package no.sintef.bvr.constraints.rewrite;

import no.sintef.bvr.constraints.LogicalExpression;
import no.sintef.bvr.constraints.Visitor;

public class AbstractVisitor<T> implements Visitor<T> {

    protected T defaultValue() {
        throw new IllegalStateException("No default value defined!");
    }

    @Override
    public T onConjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return defaultValue();
    }

    @Override
    public T onDisjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return defaultValue();
    }

    @Override
    public T onImplication(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return defaultValue();
    }

    @Override
    public T onNegation(LogicalExpression operand) {
        return defaultValue();
    }

    @Override
    public T onFeatureByIndex(int featureIndex) {
        return defaultValue();
    }

    @Override
    public T onFeatureByName(String featureName) {
        return defaultValue();
    }

}
