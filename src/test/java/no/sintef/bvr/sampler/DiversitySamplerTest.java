/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler;

import java.util.HashSet;
import java.util.Set;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import no.sintef.bvr.Product;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.metrics.Diversity;
import org.junit.Test;

public class DiversitySamplerTest {

    @Test
    public void shouldYieldTheCorrectNumberOfProducts() {
        ProductLine productLine = new ProductLine(2);

        DiversitySampler sampler = new DiversitySampler(5);
        Sample result = sampler.sample(productLine);

        assertEquals(5, result.size());
    }

    @Test
    public void shouldYieldTheCorrectProducts() {
        ProductLine productLine = new ProductLine(2);

        DiversitySampler sampler = new DiversitySampler(2, 1);
        Sample result = sampler.sample(productLine);

        Set<Sample> candidates = new HashSet<>();
        candidates.add(new Sample(productLine,
                new Product(productLine, true, true),
                new Product(productLine, false, false)));
        candidates.add(new Sample(productLine,
                new Product(productLine, false, true),
                new Product(productLine, true, false)));

        assertTrue("Invalid sample:\n" + result + "\n candidates are " + candidates, candidates.contains(result));
    }

    @Test
    public void shouldYieldALowDiversitySample() {
        ProductLine productLine = new ProductLine(2);

        DiversitySampler sampler = new DiversitySampler(2, 0);
        Sample result = sampler.sample(productLine);

        Diversity diversity = new Diversity();
        assertEquals(0D, diversity.of(result));
    }

    @Test
    public void shouldYieldAHighDiversitySample() {
        ProductLine productLine = new ProductLine(2);

        DiversitySampler sampler = new DiversitySampler(2, 1);
        Sample result = sampler.sample(productLine);

        Diversity diversity = new Diversity();
        assertEquals(1D, diversity.of(result));
    }

}
