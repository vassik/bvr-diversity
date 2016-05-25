/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler;

import java.util.Random;
import no.sintef.bvr.Feature;
import no.sintef.bvr.Product;
import no.sintef.bvr.ProductLine;

/**
 *
 * @author franckc
 */
public class RandomSampler implements Sampler {
    
    private final Random random;
    private final int sampleSize;
    
    public RandomSampler(int sampleSize) {
        this.random = new Random();
        this.sampleSize = sampleSize;
    }

    @Override
    public Sample sample(ProductLine productLine) {
        Sample sample = new Sample(productLine);
        for (int index = 0 ; index<sampleSize ;index++) {
            Product product = createProduct(productLine);
            sample.add(product);
        }
        return sample;
    }
    
    private Product createProduct(ProductLine productLine) {
        boolean features[] = new boolean[productLine.featureCount()];
        for (Feature eachFeature: productLine) {
            features[eachFeature.index()] = randomFeature();
        }
        return new Product(productLine, features);
    }
    
    protected boolean randomFeature() {
        return random.nextBoolean();
    }
    
}
