/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.random;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import no.sintef.bvr.spl.ConstrainedProductLine;
import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductSet;
import no.sintef.bvr.sampler.Sampler;
import org.junit.Test;
import static no.sintef.bvr.constraints.Builder.feature;
import static no.sintef.bvr.constraints.Builder.feature;

public class RandomSamplerTest {

    @Test
    public void testUnderConstraints() {
        ConstrainedProductLine productLine 
                = new ConstrainedProductLine(2, feature(0).and(feature(1)));

        Sampler sampler = new RandomSampler(productLine);

        ProductSet sample = sampler.sample(1);

        assertFalse(sample.isEmpty());
        final Product expected = new Product(FeatureSet.fromDefaultTemplate(2), true, true);
        assertEquals(expected, sample.withKey(0));

    }

}
