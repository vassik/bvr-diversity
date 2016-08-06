package no.sintef.bvr.sampler.diversity.objective;

import no.sintef.bvr.spl.ProductSet;

/**
 * An objective that can be satisfied to different degree.
 */
public abstract class Objective {

    public static double DEFAULT_TOLERANCE = 0.5;

    public boolean isSatisfiedBy(ProductSet products) {
        return isSatisfiedBy(products, DEFAULT_TOLERANCE);
    }

    public boolean isSatisfiedBy(ProductSet candidate, double tolerance) {
        return distanceFrom(candidate) < tolerance;
    }

    public abstract double distanceFrom(ProductSet candidate);

}
