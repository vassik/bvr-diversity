/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.metrics;

import no.sintef.bvr.Feature;
import no.sintef.bvr.Product;
import no.sintef.bvr.sampler.Sample;

/**
 *
 * @author franckc
 */
public class Diversity {
    
    public double of(Sample sample) {
        assert !sample.isEmpty(): "Invalid empty sample!";
        
        double sum = 0;
        for (Product product_A: sample) {
            for (Product product_B: sample) {
                if (product_A != product_B) {
                    sum += distance(sample, product_A, product_B);
                }
            }
        }
        return sum / sample.size();
    }
    
    private double distance(Sample sample, Product productA, Product productB) {
        double sum = 0;
        for (Feature eachFeature: sample.productLine()) {
            sum += Math.abs(productA.statusOf(eachFeature) - productB.statusOf(eachFeature));
        }
        return sum / sample.productLine().featureCount();
    }
    
}
