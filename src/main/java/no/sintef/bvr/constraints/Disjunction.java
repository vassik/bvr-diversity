package no.sintef.bvr.constraints;

import no.sintef.bvr.Product;

public class Disjunction extends LogicalExpression {

    private final LogicalExpression leftOperand;
    private final LogicalExpression rightOperand;

    public Disjunction(LogicalExpression left, LogicalExpression right) {
        this.leftOperand = left;
        this.rightOperand = right;
    }

    @Override
    protected boolean evaluateOn(Product product) {
        return leftOperand.evaluateOn(product) || rightOperand.evaluateOn(product);

    }

    @Override
    public String toString() {
        return "(" + leftOperand + " or " + rightOperand + ")";
    }

}
