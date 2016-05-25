/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import no.sintef.bvr.Feature;
import no.sintef.bvr.Product;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.metrics.Diversity;

/**
 *
 * @author franckc
 */
public class DiversitySampler implements Sampler {

    private final int desiredSampleSize;

    DiversitySampler(int sampleSize) {
        desiredSampleSize = sampleSize;
    }

    @Override
    public Sample sample(ProductLine productLine) {
        Population population = new Population(productLine, 100, new RandomSampler(desiredSampleSize));
        for (int epoch = 0; epoch < 100; epoch++) {
            population.evolve();
        }
        return population.fittest();
    }

}


class Individual {

    private final static Random random = new Random();
    
    private final Sample sample;
    
    public Individual(Sample sample) {
        this.sample = sample;
    }

    boolean isFitterThan(Individual best) {
         return fitness() <= best.fitness();
    }

    protected double fitness() {
        return 1D / DIVERSITY.of(sample);
    }
    
    protected static final Diversity DIVERSITY = new Diversity();

    Sample sample() {
        return sample;
    }

    void mutate() {
        if (mutationOccurs()) {
            aRandomProduct().toggle(aRandomFeature()); 
        }
    }

    private boolean mutationOccurs() {
        double draw = random.nextDouble();
        return draw < MUTATION_PROBABILITY;
    }
    
    protected static final double MUTATION_PROBABILITY = 0.25;

    private Product aRandomProduct() {
        int selected = random.nextInt(sample.size());
        return sample.asList().get(selected);
    }
    
    private Feature aRandomFeature() {
        int selected = random.nextInt(sample.productLine().featureCount());
        return sample.productLine().featureAt(selected);
    }
       
}

class NullIndividual extends Individual {

    public NullIndividual() {
        super(null);
    }

    @Override
    protected double fitness() {
        return Double.MAX_VALUE;
    }
    
}

class Population {
    
    private final Sampler sampler;
    private final ProductLine productLine;
    private final Set<Individual> individuals;
    
    public Population(ProductLine productLine, int size, Sampler sampler) {
        this.sampler = sampler;
        this.productLine = productLine;
        this.individuals = new HashSet<>();
        for(int individual=0 ; individual<size ;individual++) {
            individuals.add(new Individual(sampler.sample(productLine)));
        }
    }
    
    public void evolve() {
        for(Individual eachIndividual: individuals) {
            eachIndividual.mutate();
        }
    }
    
    public Sample fittest() {
        Individual fittest = new NullIndividual();
        for (Individual anyIndividual: individuals) {
            if (anyIndividual.isFitterThan(fittest)) {
                fittest = anyIndividual;
            }
        }
        return fittest.sample();
    }

}
