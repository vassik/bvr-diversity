/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.random;

import java.util.Random;
import no.sintef.bvr.Product;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.Sample;
import no.sintef.bvr.sampler.Sampler;

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
        final Generator generator = Generator.createFor(productLine);
        Sample sample = new Sample(productLine);
        for (int index = 0; index < sampleSize; index++) {
            Product product = null;
            do {
                if (!generator.hasNext()) throw new IllegalArgumentException("Not enough valid products!");
                product = new Product(generator.next());
            } while (!product.isValid());
            sample.add(product);
        }
        return sample;
    }


    protected boolean randomFeature() {
        return random.nextBoolean();
    }

}
