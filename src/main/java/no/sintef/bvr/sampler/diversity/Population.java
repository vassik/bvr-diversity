/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.diversity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.Sample;
import no.sintef.bvr.sampler.Sampler;

/**
 *
 * @author franckc
 */
public class Population {

    private static final double BREEDING_FRACTION = 0.10;

    private final static Random random = new Random();

    private final int capacity;
    private final int eliteSize;
    private final List<Individual> individuals;

    public Population(ProductLine productLine, int size, Sampler sampler) {
        this.capacity = size;
        this.eliteSize = (int) (capacity *  BREEDING_FRACTION);
        this.individuals = new ArrayList<>();
        for (int individual = 0; individual < size; individual++) {
            individuals.add(new Individual(sampler.sample(productLine)));
        }
    }

    public void mutate() {
        for (Individual eachIndividual : individuals) {
            eachIndividual.mutate();
        }
    }

    public Sample fittest() {
        assert !individuals.isEmpty() : "Invalid empty population!";
        return individuals.get(0).sample();
    }

    public Sample convergeTo(Goal goal, int MAX_EPOCH) {
        for (int epoch = 0; epoch < MAX_EPOCH; epoch++) {
            rank(goal);
            if (goal.isSatisfiedBy(fittest())) {
                return fittest();
            }
            kill();
            breed();
            mutate();
        }
        return fittest();
    }

    private void rank(Goal goal) {
        for (Individual eachIndividual : individuals) {
            eachIndividual.evaluate(goal);
        }
        Collections.sort(individuals);
    }

    private void kill() {
        if (individuals.size() > capacity) {
            int count = individuals.size() - capacity;
            for (int index = 0; index < count; index++) {
                individuals.remove(individuals.size()-1);
            }
        }
        assert individuals.size() == capacity: "Population: " + individuals.size() + "(expecting: " + capacity + ")";
    }

    private void breed() {
        final List<Individual> allChildren = new ArrayList<>(2 * eliteSize);
        for (Individual eachIndividual : elite()) {
            List<Individual> children = eachIndividual.mateWith(aRandomIndividual());
            allChildren.addAll(children);
        }
        individuals.addAll(allChildren);
    }

    private Iterable<Individual> elite() {
        return individuals.subList(0, eliteSize);
    }

    private Individual aRandomIndividual() {
        assert !individuals.isEmpty() : "Error: Empty population!";
        int selected = random.nextInt(individuals.size());
        return individuals.get(selected);
    }

}