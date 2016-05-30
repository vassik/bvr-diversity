package no.sintef.bvr.constraints;

import no.sintef.bvr.Product;


public class Disjunction extends LogicalExpression {

    private final LogicalExpression left;
    private final LogicalExpression right;

    public Disjunction(LogicalExpression left, LogicalExpression right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    protected boolean evaluateOn(Product product) {
        return left.evaluateOn(product) || right.evaluateOn(product);
    
    }
    
}
