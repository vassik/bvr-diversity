
package no.sintef.bvr.constraints;

import java.util.Objects;


public class FeatureByName extends LogicalExpression {
    
    private final String name;

    public FeatureByName(String featureName) {
        this.name = featureName;
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.onFeatureByName(name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.name);
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
        final FeatureByName other = (FeatureByName) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
    
    
}
