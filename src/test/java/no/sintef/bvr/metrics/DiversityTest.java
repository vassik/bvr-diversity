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
    public void twoOppositeProductShouldHaveAZeroDiversity() {
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

}
