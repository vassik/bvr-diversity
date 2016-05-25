/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.metrics;

import static junit.framework.Assert.assertEquals;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.Sample;
import no.sintef.bvr.sampler.metrics.Coverage;
import org.junit.Test;

/**
 *
 * @author franckc
 */
public class CoverageTest {

    private final int featureCount;
    private final ProductLine productLine;
    private final Coverage coverage;

    public CoverageTest() {
        featureCount = 5;
        productLine = new ProductLine(featureCount);
        coverage = new Coverage();
    }

    @Test
    public void coverageOfTheEmptyProductIsZero() {
        Sample sample = new Sample(productLine);
        sample.addProduct(false, false, false, false, false);

        assertEquals(0., coverage.of(sample));
    }

    @Test
    public void coverageOfTheFullProductIsOne() {
        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, true, true, true);

        assertEquals(1., coverage.of(sample));
    }
    
    @Test
    public void coverageOfTwoOppositeProductsShouldBeOne() {
        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, false, false, false);
        sample.addProduct(false, false, true, true, true);

        assertEquals(1., coverage.of(sample));
    }
    
    @Test
    public void coverageOfTwoSingleFeatureProducts() {
        Sample sample = new Sample(productLine);
        sample.addProduct(true, false, false, false, false);
        sample.addProduct(false, true, false, false, false);

        assertEquals(2. / featureCount, coverage.of(sample));
    }
    
}
