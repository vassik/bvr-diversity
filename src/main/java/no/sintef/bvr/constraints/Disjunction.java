package no.sintef.bvr.constraints;

import java.util.Objects;
import no.sintef.bvr.spl.Product;

public class Disjunction extends LogicalExpression {

    private final LogicalExpression leftOperand;
    private final LogicalExpression rightOperand;

    public Disjunction(LogicalExpression left, LogicalExpression right) {
        this.leftOperand = left;
        this.rightOperand = right;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.leftOperand);
        hash = 53 * hash + Objects.hashCode(this.rightOperand);
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
        final Disjunction other = (Disjunction) obj;
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
        return "(" + leftOperand + " or " + rightOperand + ")";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.onDisjunction(leftOperand, rightOperand);
    }

}
