package no.sintef.bvr.metrics;

import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductSet;

/**
 *
 */
public class PairWiseDistanceError implements Metric<ProductSet> {

    private final double desiredDistance;

    public PairWiseDistanceError(double desiredDiversity) {
        this.desiredDistance = desiredDiversity;
    }

    @Override
    public double of(ProductSet products) {
        return absolute(products);
    }

    public double absolute(ProductSet products) {
        assert !products.isEmpty() : "Invalid empty sample!";

        double sum = 0;
        final int productCount = products.size();
        for (int i = 0; i < productCount; i++) {
            for (int j = i + 1; j < productCount; j++) {
                final double distance = products.withKey(i).distanceWith(products.withKey(j));
                final double error = desiredDistance - distance;
                sum += error * error;
            }
        }
        return sum;
    }

}
