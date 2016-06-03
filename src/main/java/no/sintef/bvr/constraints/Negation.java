
package no.sintef.bvr.constraints;

import no.sintef.bvr.Product;

public class Negation extends LogicalExpression {
    
    private final LogicalExpression operand;

    public Negation(LogicalExpression operand) {
        this.operand = operand;
    }

    @Override
    protected boolean evaluateOn(Product product) {
        return !operand.evaluateOn(product);
    }
    
    @Override
    public String toString() {
        return "(not " + operand + ")";
    }
    
}
