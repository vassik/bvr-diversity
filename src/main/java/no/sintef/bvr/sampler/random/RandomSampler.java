/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import no.sintef.bvr.Product;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.Sample;
import no.sintef.bvr.sampler.Sampler;

/**
 * Random Sampling of product lines
 */
public class RandomSampler implements Sampler {

    private final Random random;
    private final ProductLine productLine;
    private final List<Product> cache;

    public RandomSampler(ProductLine productLine) {
        this.random = new Random();
        this.productLine = productLine;
        cache = new ArrayList<>();
        fillCache();
    }

    private void fillCache() {
        final Generator generator = Generator.createFor(productLine);
        while (generator.hasNext()) {
            Product product = new Product(generator.next());
            if (product.isValid()) {
                cache.add(product);
            }
        }
        if (cache.isEmpty()) {
            throw new IllegalStateException("There is no valid product");
        }
    }

    @Override
    public Sample sample(int productCount) {
        if (productCount > cache.size()) {
            throw new IllegalArgumentException(String.format("The product line has only %d valid products", cache.size()));
        }
        Sample result = new Sample(productLine);
        List<Integer> indexes = new ArrayList<>(cache.size());
        for(int i=0 ; i<cache.size() ; i++) {
            indexes.add(i);
        }
        
        for(int i=0 ;i<productCount;i++) {
            Integer chosenIndex = random.nextInt(indexes.size());
            result.add(cache.get(indexes.get(chosenIndex)));
            indexes.remove(chosenIndex);
        }

        return result;
    }

    protected boolean randomFeature() {
        return random.nextBoolean();
    }

}
