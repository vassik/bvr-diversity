
package no.sintef.bvr.sampler.diversity.evolution;

import java.util.Arrays;
import java.util.List;

/**
 * Combine multiple objective using the sum of squared errors.
 */
public final class MultiObjective extends Objective {

    private final List<Objective> objectives;

    public MultiObjective(Objective... subgoals) {
        assert subgoals.length > 0: "Missing subgoals!";
        this.objectives = Arrays.asList(subgoals);
    }

    @Override
    public double distanceFrom(Individual candidate) { 
        double total = 0D;
        for(Objective eachObjective: objectives) {
            total += Math.pow(eachObjective.distanceFrom(candidate), 2);
        }
        return total;
    }

}
