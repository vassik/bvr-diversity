/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.metrics;

import java.util.BitSet;
import no.sintef.bvr.Product;
import no.sintef.bvr.sampler.Sample;


public class Coverage {
        
    public double of(Sample sample) {
        final int featureCount = sample.productLine().featureCount();
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
