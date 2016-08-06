package no.sintef.bvr.constraints;

import java.util.Objects;


public class Conjunction extends LogicalExpression {

    private final LogicalExpression leftOperand;
    private final LogicalExpression rightOperand;

    public Conjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.leftOperand);
        hash = 67 * hash + Objects.hashCode(this.rightOperand);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Conjunction other = (Conjunction) obj;
        if (!Objects.equals(this.leftOperand, other.leftOperand)) {
            return false;
        }
        if (!Objects.equals(this.rightOperand, other.rightOperand)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "(" + leftOperand + " and " + rightOperand + ")";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.onConjunction(leftOperand, rightOperand);
    }
    
    
    
}
