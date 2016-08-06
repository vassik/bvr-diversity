
package no.sintef.bvr.constraints;

import java.util.Objects;
import no.sintef.bvr.spl.Product;

public class Negation extends LogicalExpression {
    
    private final LogicalExpression operand;

    public Negation(LogicalExpression operand) {
        this.operand = operand;
    }
    
    @Override
    public String toString() {
        return "(not " + operand + ")";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.operand);
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
        final Negation other = (Negation) obj;
        if (!Objects.equals(this.operand, other.operand)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.onNegation(operand);
    }
    
}
