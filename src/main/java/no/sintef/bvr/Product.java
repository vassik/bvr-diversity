/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr;

import java.util.BitSet;
import java.util.Objects;

/**
 *
 * @author franckc
 */
public class Product {

    private final ProductLine productLine;
    private final BitSet features;

    public Product(ProductLine productLine, boolean... isSelected) {
        assert isSelected.length == productLine.featureCount() : "Invalid feature count!";

        this.productLine = productLine;
        this.features = new BitSet(productLine.featureCount());
        for (Feature eachFeature : productLine) {
            this.features.set(eachFeature.index(), isSelected[eachFeature.index()]);
        }
    }

    public int statusOf(Feature feature) {
        return features.get(feature.index()) ? 1 : 0;
    }

    public boolean offers(Feature feature) {
        return features.get(feature.index());
    }

    public int featureCount() {
        return features.cardinality();
    }

    public ProductLine productLine() {
        return productLine;
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("{ ");
        for (Feature eachFeature : productLine) {
            buffer
                    .append(eachFeature.name())
                    .append(":")
                    .append(offers(eachFeature))
                    .append(" ");
        }
        buffer.append("}");
        return buffer.toString();
    }

    public void toggle(Feature feature) {
        features.flip(feature.index());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.productLine);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (!Objects.equals(this.productLine, other.productLine)) {
            return false;
        }
        if (!Objects.equals(this.features, other.features)) {
            return false;
        }
        return true;
    }
    
    
}
