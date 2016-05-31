/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.diversity;

import no.sintef.bvr.metrics.Coverage;
import no.sintef.bvr.metrics.PairWiseDistanceError;
import no.sintef.bvr.sampler.Sample;

/**
 * The optimisation objective, including the evaluation (i.e.,
 * fitness) of individuals.
 */
public class Goal {

    private static final double TOLERANCE = 1e-6;

    private final double desiredDiversity;
    private final PairWiseDistanceError diversity;
    private final Coverage coverage;

    public Goal(double diversity) {
        this.coverage = new Coverage();
        this.diversity = new PairWiseDistanceError(diversity);
        this.desiredDiversity = diversity;
    }

    boolean isSatisfiedBy(Sample sample) {
        return error(sample) < TOLERANCE;
    }

    double error(Sample sample) {
        return Math.pow(diversity.of(sample), 2) 
                + Math.pow(1D - coverage.of(sample), 2);
    }

    double fitnessOf(Individual individual) {
        return 10 * 1D / (1 + error(individual.sample()));
    }

}
