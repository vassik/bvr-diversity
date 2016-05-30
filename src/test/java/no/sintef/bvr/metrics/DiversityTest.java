package no.sintef.bvr.metrics;

import static junit.framework.Assert.assertEquals;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.Sample;
import org.junit.Test;

public class DiversityTest {

    private final Diversity diversity;
    private final int featureCount;
    private final ProductLine productLine;

    public DiversityTest() {
        featureCount = 5;
        diversity = new Diversity();
        productLine = new ProductLine(featureCount);
    }

    @Test
    public void twoSimilarProductShouldHaveAZeroDiversity() {
        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, false, false, false);
        sample.addProduct(true, true, false, false, false);

        assertEquals(0., diversity.of(sample));
    }

    @Test
    public void twoOppositeProductShouldHaveAOneDiversity() {
        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, false, false, false);
        sample.addProduct(false, false, true, true, true);

        assertEquals(1., diversity.of(sample));
    }

    @Test
    public void oneFeatureDifferenceShouldLeadToADiversityOf() {
        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, false, false, false);
        sample.addProduct(true, true, false, false, true);

        assertEquals(1D / featureCount, diversity.of(sample));
    }
    
    @Test
    public void testMaximumAbsoluteDiversity() {
        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, false, false, false);
        sample.addProduct(false, false, true, true, true);

        assertEquals(2D, diversity.absolute(sample));
    }

    @Test
    public void testMinimumAbsoluteDiversity() {
        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, false, false, false);
        sample.addProduct(true, true, false, false, false);

        assertEquals(0D, diversity.absolute(sample));
    }
    
    @Test
    public void testMediumAbsoluteDiversity() {
        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, false, false, false);
        sample.addProduct(true, true, false, false, true);
        sample.addProduct(true, true, false, true, true);

        assertEquals(8D/5, diversity.absolute(sample), 1e-6);
    }

    @Test
    public void sandbox() {
        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, true, true, true);
        sample.addProduct(true, true, true, true, true);
        sample.addProduct(true, true, true, false, true);
        sample.addProduct(true, true, false, false, false);

        assertEquals(4D / (4 * 3), diversity.of(sample));
    }

}
