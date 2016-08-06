/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.diversity.objective;

import java.util.Arrays;
import java.util.List;
import no.sintef.bvr.spl.ProductSet;

/**
 * Combine multiple objective using the sum of squared errors.
 */
public class MultiObjective extends Objective {

    private final List<Objective> objectives;

    public MultiObjective(Objective... subgoals) {
        assert subgoals.length > 0: "Missing subgoals!";
        this.objectives = Arrays.asList(subgoals);
    }

    @Override
    public double distanceFrom(ProductSet candidate) {
        double total = 0D;
        for(Objective eachObjective: objectives) {
            total += Math.pow(eachObjective.distanceFrom(candidate), 2);
        }
        return total;
    }

}
