package no.sintef.bvr.constraints;

import no.sintef.bvr.spl.Product;

public abstract class LogicalExpression {

    public final LogicalExpression implies(LogicalExpression operand) {
        return new Implication(this, operand);
    }

    public final LogicalExpression and(LogicalExpression operand) {
        return new Conjunction(this, operand);
    }

    public final LogicalExpression or(LogicalExpression operand) {
        return new Disjunction(this, operand);
    }


    public abstract <T> T accept(Visitor<T> visitor); 

}
