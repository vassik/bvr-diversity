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

    private final double desiredDistance;

    public PairWiseDistanceError(double desiredDiversity) {
        this.desiredDistance = desiredDiversity;
    }

    public double of(Sample sample) {
        return Math.sqrt(absolute(sample));
    }

    public double absolute(Sample sample) {
        assert !sample.isEmpty() : "Invalid empty sample!";

        double sum = 0;
        final int productCount = sample.size();
        for (int i = 0; i < productCount; i++) {
            final Product product_i = sample.productAt(i);
            for (int j = i + 1; j < productCount; j++) {
                final double distance = product_i.distanceWith(sample.productAt(j));
                final double error = desiredDistance - distance;
                sum += error * error;
            }
        }
        return sum;
    }

}
