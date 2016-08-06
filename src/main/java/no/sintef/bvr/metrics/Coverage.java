/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.metrics;

import java.util.BitSet;
import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductSet;


public class Coverage implements Metric<ProductSet> {
        
    private final FeatureSet features;

    public Coverage(FeatureSet features) {
        this.features = features;
    }
        
    @Override
    public double of(ProductSet sample) {
        final int featureCount = features.count();
        final BitSet covered = new BitSet(featureCount);
        final BitSet notCovered = new BitSet(featureCount);
        for (Product anyProduct: sample) {
            final BitSet offered = (BitSet) anyProduct.asBitSet().clone();
            covered.or(offered); 
            offered.flip(0, featureCount);
            notCovered.or(offered);
        }
        return (covered.cardinality() + notCovered.cardinality()) / ((double) 2 * featureCount);
    }
} 
