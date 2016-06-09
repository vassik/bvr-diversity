package no.sintef.bvr.sampler.diversity;

import static junit.framework.Assert.assertEquals;
import no.sintef.bvr.Product;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.Sample;
import org.junit.Test;

public class IndividualTest {

    private ProductLine productLine;
    private int featureCount;
    private Individual individual;
    private Product product;
    private Sample sample;

    public IndividualTest() {
        featureCount = 5;
        productLine = new ProductLine(featureCount);
        sample = new Sample(productLine);
        product = new Product(productLine, true, true, true, true, true);
        sample.add(product);
        individual = new Individual(sample);
    }

    @Test
    public void mutationShouldInvertOneFeature_1() {
        final int strength = 2;

        individual.doMutate(product, strength);

        verify(strength);
    }

    private void verify(final int mutationStrength) {
        assertEquals(featureCount - mutationStrength, product.activefeatureCount());
    }

    @Test
    public void mutationShouldInvertTwoFeature_2() {
        final int strength = 4;

        individual.doMutate(product, strength);

        verify(strength);
    }

}
