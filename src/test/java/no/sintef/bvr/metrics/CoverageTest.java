/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.metrics;

import static junit.framework.Assert.assertEquals;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.Sample;
import org.junit.Test;


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
    public void allFeatureShouldHaveACoverageOf05() {
        Sample sample = new Sample(productLine);
        sample.addProduct(false, false, false, false, false);

        assertEquals(0.5, coverage.of(sample));
    }

    @Test
    public void noFeatureShouldHaveACoverageOf05() {
        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, true, true, true);

        assertEquals(0.5, coverage.of(sample));
    }

        @Test
    public void allFeatureAndNoFeatureShouldHaveACoverageOf1() {
        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, true, true, true);
        sample.addProduct(false, false, false, false, false);

        assertEquals(1D, coverage.of(sample));
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

        assertEquals(7D / (2 * featureCount), coverage.of(sample));
    }
    
}
