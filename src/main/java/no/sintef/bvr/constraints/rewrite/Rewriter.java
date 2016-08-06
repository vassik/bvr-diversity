
package no.sintef.bvr.constraints.rewrite;

import static no.sintef.bvr.constraints.Builder.feature;
import static no.sintef.bvr.constraints.Builder.not;
import no.sintef.bvr.constraints.LogicalExpression;
import no.sintef.bvr.constraints.Visitor;

/**
 * Rewrite Logical Expression
 */
class Rewriter implements Visitor<LogicalExpression> {

    @Override
    public LogicalExpression onConjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return leftOperand.accept(this).and(rightOperand.accept(this));
    }

    @Override
    public LogicalExpression onDisjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return leftOperand.accept(this).or(rightOperand.accept(this));
    }

    @Override
    public LogicalExpression onImplication(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return leftOperand.accept(this).implies(rightOperand.accept(this));
    }

    @Override
    public LogicalExpression onNegation(LogicalExpression operand) {
        return not(operand.accept(this));
    }

    @Override
    public LogicalExpression onFeatureByIndex(int featureIndex) {
        return feature(featureIndex);
    }

    @Override
    public LogicalExpression onFeatureByName(String featureName) {
        return feature(featureName);
    }

}

class ImplicationRemover extends Rewriter {

    @Override
    public LogicalExpression onImplication(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return not(leftOperand.accept(this))
                .or(rightOperand.accept(this));
    }

}

class PushNegationInward extends Rewriter {

    @Override
    public LogicalExpression onNegation(LogicalExpression operand) {
        return operand.accept(new Negate());
    }

}

class Negate extends Rewriter {

    @Override
    public LogicalExpression onNegation(LogicalExpression operand) {
        return operand;
    }

    @Override
    public LogicalExpression onFeatureByName(String name) {
        return not(super.onFeatureByName(name));
    }

    @Override
    public LogicalExpression onFeatureByIndex(int index) {
        return not(super.onFeatureByIndex(index));
    }

    @Override
    public LogicalExpression onImplication(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return not(super.onImplication(leftOperand, rightOperand));
    }

    @Override
    public LogicalExpression onDisjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return leftOperand.accept(this).and(rightOperand.accept(this));
    }

    @Override
    public LogicalExpression onConjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return leftOperand.accept(this).or(rightOperand.accept(this));
    }

}

class DistributeDisjunctionOverConjunction extends Rewriter {

    @Override
    public LogicalExpression onDisjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return rightOperand.accept(new InjectDisjunctionWith(leftOperand));
    }

}

class InjectDisjunctionWith extends Rewriter {

    private final LogicalExpression operand;

    public InjectDisjunctionWith(LogicalExpression operand) {
        this.operand = operand;
    }

    @Override
    public LogicalExpression onFeatureByIndex(int index) {
        return operand.or(feature(index));
    }

    @Override
    public LogicalExpression onFeatureByName(String name) {
        return operand.or(feature(name));
    }

    @Override
    public LogicalExpression onNegation(LogicalExpression operand) {
        return this.operand.or(not(operand));
    }

    @Override
    public LogicalExpression onConjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return leftOperand.accept(this).and(rightOperand.accept(this));
    }

    @Override
    public LogicalExpression onDisjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return operand.or(leftOperand.or(rightOperand));
    }

    @Override
    public LogicalExpression onImplication(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return operand.or(leftOperand.implies(rightOperand));
    }

}
