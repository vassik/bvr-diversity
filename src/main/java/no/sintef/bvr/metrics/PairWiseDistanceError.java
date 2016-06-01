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
        for (int i = 0; i < sample.size(); i++) {
            for (int j = i + 1; j < sample.size(); j++) {
                final double distance = sample.productAt(i).distanceWith(sample.productAt(j));
                sum += Math.pow(desiredDistance - distance, 2);
            }
        }
//        for (Product product_A : sample) {
//            for (Product product_B : sample) {
//                if (product_A != product_B) {
//                    sum += Math.pow(desiredDistance - product_A.distanceWith(product_B), 2);
//                }
//            }
//        }
        return sum;
    }

}
