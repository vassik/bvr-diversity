package no.sintef.bvr.sampler.diversity;

import no.sintef.bvr.metrics.Coverage;
import no.sintef.bvr.metrics.PairWiseDistanceError;
import no.sintef.bvr.sampler.diversity.evolution.MultiObjective;
import no.sintef.bvr.sampler.diversity.evolution.Objective;
import no.sintef.bvr.spl.Factory;
import no.sintef.bvr.spl.FeatureSet;
import org.junit.Test;

/**
 *
 */
public class FitnessTest {

    @Test
    public void testFitness() {
        FeatureSet features = FeatureSet.fromDefaultTemplate(2);
        Factory factory = new Factory(features);
        Objective objective = new MultiObjective(
                new DesiredValue(new Coverage(features), 1),
                new DesiredValue(new PairWiseDistanceError(1), 0)
        );

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ProductSetIndividual individual = new ProductSetIndividual(factory.productSetFromCodes(i, j));
                individual.evaluateAgainst(objective);
                System.out.println(individual);
            }
        }

    }

}