/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.random;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import no.sintef.bvr.Product;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.Sample;
import no.sintef.bvr.sampler.Sampler;
import static no.sintef.bvr.constraints.Builder.feature;
import org.junit.Test;



public class RandomSamplerTest {

    private final ProductLine productLine;

    public RandomSamplerTest() {
        productLine = new ProductLine(5);
    }
    
    @Test
    public void testUnderConstraints() {
        ProductLine productLine = new ProductLine(2);
        productLine.addConstraint(feature(0).and(feature(1)));

        Sampler sampler = new RandomSampler(1);
        
        Sample sample = sampler.sample(productLine);
        
        assertFalse(sample.isEmpty());
        final Product expected = new Product(productLine, true, true);
        assertEquals(expected, sample.productAt(0));
                    
    }
    
}
