/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.constraints;

import no.sintef.bvr.Feature;
import no.sintef.bvr.Product;

/**
 *
 * @author franckc
 */
public class FeatureByName extends LogicalExpression {
    
    private final String name;

    public FeatureByName(String featureName) {
        this.name = featureName;
    }

    @Override
    protected boolean evaluateOn(Product product) {
        Feature feature = product.productLine().featureNamed(name);
        return product.offers(feature);
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    
}
