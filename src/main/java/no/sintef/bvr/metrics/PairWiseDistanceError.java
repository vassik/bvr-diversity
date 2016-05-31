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
public class PairWiseDistanceError {

    private final double desiredDiversity;

    public PairWiseDistanceError(double desiredDiversity) {
        this.desiredDiversity = desiredDiversity;
    }
    
    public double of(Sample sample) {
        return Math.sqrt(absolute(sample));
    }

    public double absolute(Sample sample) {
        assert !sample.isEmpty() : "Invalid empty sample!";

        double sum = 0;
        for (Product product_A : sample) {
            for (Product product_B : sample) {
                if (product_A != product_B) {
                    sum += Math.pow(desiredDiversity - product_A.distanceWith(product_B), 2);
                }
            }
        }
        return sum;
    }

}
