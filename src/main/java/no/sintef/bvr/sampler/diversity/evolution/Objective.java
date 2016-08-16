package no.sintef.bvr.sampler.diversity.evolution;

/**
 * An objective that can be satisfied to different degree.
 */
public abstract class Objective {

    public static double DEFAULT_TOLERANCE = 0.5;

    public boolean isSatisfiedBy(Individual individual) {
        return isSatisfiedBy(individual, DEFAULT_TOLERANCE);
    }

    public boolean isSatisfiedBy(Individual candidate, double tolerance) {
        return distanceFrom(candidate) < tolerance;
    }

    public abstract double distanceFrom(Individual individual);

}
