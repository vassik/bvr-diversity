
package no.sintef.bvr.metrics;

import no.sintef.bvr.Product;
import no.sintef.bvr.sampler.Sample;

/**
 *
 */
public class PairWiseDistanceError {

    private final double desiredDistance;

    public PairWiseDistanceError(double desiredDiversity) {
        this.desiredDistance = desiredDiversity;
    }

    public double of(Sample sample) {
        return absolute(sample);
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
