package no.sintef.bvr.constraints;

public class FeatureByIndex extends LogicalExpression {

    private final int featureIndex;

    public FeatureByIndex(int featureIndex) {
        this.featureIndex = featureIndex;
    }

    @Override
    public String toString() {
        return "f@" + featureIndex;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.onFeatureByIndex(featureIndex);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.featureIndex;
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
        final FeatureByIndex other = (FeatureByIndex) obj;
        if (this.featureIndex != other.featureIndex) {
            return false;
        }
        return true;
    }
    
    

}
