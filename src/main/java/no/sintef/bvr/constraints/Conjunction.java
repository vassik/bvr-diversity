package no.sintef.bvr.constraints;

import no.sintef.bvr.Product;


public class Conjunction extends LogicalExpression {

    private final LogicalExpression leftOperand;
    private final LogicalExpression rightOperand;

    public Conjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    protected boolean evaluateOn(Product product) {
        return leftOperand.evaluateOn(product) && rightOperand.evaluateOn(product);
    }
    
    
}
