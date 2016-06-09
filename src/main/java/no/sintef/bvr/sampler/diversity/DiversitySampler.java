/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.diversity;

import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.random.RandomSampler;
import no.sintef.bvr.sampler.Sample;
import no.sintef.bvr.sampler.Sampler;

public class DiversitySampler implements Sampler {

    public static final double DEFAULT_DIVERSITY = 1;
    private static final int POPULATION_SIZE = 200;
    private static final int MAX_EPOCH = 1000;

    private final EvolutionListener listener;
    private final Goal goal;
    private final int maxEpoch;

    private final ProductLine productLine;
    
    public DiversitySampler(ProductLine productLine) {
        this(productLine, DEFAULT_DIVERSITY, MAX_EPOCH);
    }
    
    public DiversitySampler(ProductLine productLine, double diversity) {
        this(productLine, diversity, MAX_EPOCH);
    }

    public DiversitySampler(ProductLine productLine, double diversity, int maxEpoch) {
        this(productLine, diversity, maxEpoch, new EvolutionListener());
    }
    
    public DiversitySampler(ProductLine productLine, double diversity, int maxEpoch, EvolutionListener listener) {
        this.productLine = productLine;
        this.goal = new Goal(diversity);
        this.listener = listener;
        this.maxEpoch = maxEpoch;
    }

    @Override
    public Sample sample(int productCount) {
        final Population population = new Population(productLine, POPULATION_SIZE, new RandomSampler(productLine), productCount, listener);
        return population.convergeTo(goal, maxEpoch);
    }

}



