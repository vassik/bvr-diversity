/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr;

import java.util.BitSet;

/**
 *
 * @author franckc
 */
public class Product {

    private final ProductLine productLine;
    private final BitSet features;

    public Product(ProductLine productLine, boolean[] isSelected) {
        assert isSelected.length == productLine.featureCount() : "Invalid feature count!";
        
        this.productLine = productLine;
        this.features = new BitSet(productLine.featureCount());
        for (Feature eachFeature: productLine) {
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

}
