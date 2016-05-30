/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.metrics;

import no.sintef.bvr.Product;
import no.sintef.bvr.sampler.Sample;

/**
 *
 * @author franckc
 */
public class Diversity {
    
    public double of(Sample sample) {
        final int maximum = sample.size() * (sample.size()-1);
        return absolute(sample) / maximum;
    }
    
    public double absolute(Sample sample) {
        assert !sample.isEmpty(): "Invalid empty sample!";
        
        double sum = 0;
        for (Product product_A: sample) {
            for (Product product_B: sample) {
                if (product_A != product_B) {
                    sum += product_A.distanceWith(product_B); 
                }
            }
        }
        return sum;
    }
        
}
