package no.sintef.bvr.spl;

import java.util.BitSet;
import java.util.Objects;

/**
 * A product instance of a given product line (i.e., a "resolution" in BVR
 * parlance)
 */
public class Product {

    private final FeatureSet features;
    private final BitSet isActive;

    public Product(FeatureSet features) {
        this(features, new boolean[features.count()]);
    }
    
    public Product(FeatureSet features, boolean... isSelected) {
        checkFeatureCount(features, isSelected);
        this.features = features;
        this.isActive = new BitSet(features.count());
        for (final Feature eachFeature : features) {
            final int index = features.indexOf(eachFeature);
            this.isActive.set(index, isSelected[index]);
        }
    }

    private void checkFeatureCount(FeatureSet features, boolean[] isSelected) throws IllegalArgumentException {
        if (features.count() != isSelected.length) {
            final String message
                    = String.format("%d features expected, but %d found!", features.count(), isSelected.length);
            throw new IllegalArgumentException(message);
        }
    }

    private Product(Product source) {
        this.features = source.features;
        this.isActive = (BitSet) source.isActive.clone();
    }

    public Product copy() {
        return new Product(this);
    }

    boolean satisfies(FeatureSet features) {
        return this.features.equals(features);
    }

    public boolean offers(Feature feature) {
        return isActive.get(features.indexOf(feature));
    }

    public int activeFeatureCount() {
        return isActive.cardinality();
    }

    public double distanceWith(Product other) {
        BitSet copy = (BitSet) isActive.clone();
        copy.xor(other.isActive);
        return copy.cardinality() / (double) features.count();
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("{ ");
        for (int index = 0; index < features.count(); index++) {
            buffer
                    .append(index)
                    .append(":")
                    .append(isActive.get(index))
                    .append(" ");
        }
        buffer.append("}");
        return buffer.toString();
    }

    public void toggle(Feature feature) {
        isActive.flip(features.indexOf(feature));
    }

    public void toggle(Product other) {
        this.isActive.xor(other.isActive);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.features);
        hash = 47 * hash + Objects.hashCode(this.isActive);
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
        final Product other = (Product) obj;
        if (!this.features.equals(other.features)) {
            return false;
        }
        return this.isActive.equals(other.isActive);
    }

    public void set(Feature feature, boolean isSelected) {
        isActive.set(features.indexOf(feature), isSelected);
    }

    public BitSet asBitSet() {
        return this.isActive;
    }

    public void invert() {
        isActive.flip(0, features.count());
    }

    long code() {
        long code = 0L;
        for (Feature eachFeature: features) {
            final int index = features.indexOf(eachFeature);
            if (isActive.get(index)) {
                code += 0x01 << index; 
            }
        }
        return code;
    }
    
}
