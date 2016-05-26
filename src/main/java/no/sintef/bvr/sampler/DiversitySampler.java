/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import no.sintef.bvr.Feature;
import no.sintef.bvr.Product;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.metrics.Diversity;
import no.sintef.bvr.sampler.RandomSampler;
import no.sintef.bvr.sampler.Sample;
import no.sintef.bvr.sampler.Sampler;
import no.sintef.bvr.sampler.diversity.Goal;
import no.sintef.bvr.sampler.diversity.Population;

/**
 *
 * @author franckc
 */
public class DiversitySampler implements Sampler {

    public static final double DEFAULT_DIVERSITY = 0.75;
    private static final int MAX_EPOCH = 1000;

    private final Goal goal;
    private final int desiredSampleSize;

    DiversitySampler(int sampleSize) {
        this(sampleSize, DEFAULT_DIVERSITY);
    }

    DiversitySampler(int sampleSize, double diversity) {
        desiredSampleSize = sampleSize;
        goal = new Goal(diversity);
    }

    @Override
    public Sample sample(ProductLine productLine) {
        final Population population = new Population(productLine, 100, new RandomSampler(desiredSampleSize));
        return population.convergeTo(goal, MAX_EPOCH);
    }

}



