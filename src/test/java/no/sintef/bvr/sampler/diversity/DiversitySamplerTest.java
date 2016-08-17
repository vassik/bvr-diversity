/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.diversity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import no.sintef.bvr.spl.ConstrainedProductLine;
import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductSet;
import no.sintef.bvr.metrics.Diversity;
import org.junit.Test;
import static no.sintef.bvr.constraints.Builder.feature;
import static no.sintef.bvr.constraints.Builder.feature;
import org.junit.Ignore;

public class DiversitySamplerTest {

    @Test
    public void shouldYieldTheCorrectNumberOfProducts() {
        ConstrainedProductLine productLine = new ConstrainedProductLine(2);

        DiversitySampler sampler = new DiversitySampler(productLine, 1D, 5);
        ProductSet result = sample(sampler, 4);

        assertEquals(4, result.size());
    }

    private ProductSet sample(DiversitySampler sampler, int count) {
        return sampler.sample(count);
    }

    @Ignore
    public void shouldYieldTheCorrectProducts() {
        ConstrainedProductLine productLine = new ConstrainedProductLine(2);

        DiversitySampler sampler = new DiversitySampler(productLine, 1);
        ProductSet result = sample(sampler, 2);

        FeatureSet features = FeatureSet.fromDefaultTemplate(2);
        Set<ProductSet> candidates = new HashSet<>();
        candidates.add(
                new ProductSet(
                        new Product(features, true, true),
                        new Product(features, false, false)));
        candidates.add(
                new ProductSet(
                        new Product(features, false, true),
                        new Product(features, true, false)));

        assertTrue("Invalid sample:\n" + result + "\n candidates are " + candidates, candidates.contains(result));
    }

    @Test
    public void shouldYieldTheCorrectProductsUnderConstraints() {
        ConstrainedProductLine productLine
                = new ConstrainedProductLine(2, feature(1).implies(feature(0)));

        DiversitySampler sampler = new DiversitySampler(productLine, 1);
        ProductSet result = sample(sampler, 2);

        FeatureSet features = FeatureSet.fromDefaultTemplate(2);
        List<ProductSet> candidates = Arrays.asList(new ProductSet[]{
            new ProductSet(
                new Product(features, true, true),
                new Product(features, false, false)
            )
        });

        assertTrue("Invalid sample:\n" + result + "\n candidates are " + candidates, candidates.contains(result));
    }

    @Ignore
    public void shouldYieldALowDiversitySample() {
        ConstrainedProductLine productLine = new ConstrainedProductLine(2);

        DiversitySampler sampler = new DiversitySampler(productLine, 0);
        ProductSet result = sample(sampler, 2);

        Diversity diversity = new Diversity();
        assertEquals(0D, diversity.of(result));
    }

    @Test
    public void shouldYieldAHighDiversitySample() {
        ConstrainedProductLine productLine = new ConstrainedProductLine(2);

        DiversitySampler sampler = new DiversitySampler(productLine, 1);
        ProductSet result = sample(sampler, 2);

        Diversity diversity = new Diversity();
        assertEquals(1D, diversity.of(result));
    }

    @Test
    public void sandbox() {
        ConstrainedProductLine productLine
                = new ConstrainedProductLine(5,
                        feature(1).implies(feature(0)),
                        feature(2).implies(feature(1)),
                        feature(3).implies(feature(1)),
                        feature(4).implies(feature(0)));

        DiversitySampler sampler = new DiversitySampler(productLine, 1);
        ProductSet result = sample(sampler, 4);

        System.out.println("Result:\n" + result);

        Diversity diversity = new Diversity();
        System.out.println(" + diversity: " + diversity.of(result));
    }

}
