package no.sintef.bvr.spl;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the equality of two product sets
 */
public class ProductSetEqualityTest {

    private final Factory factory;

    public ProductSetEqualityTest() {
        factory = new Factory(FeatureSet.fromDefaultTemplate(4));
    }

    @Test
    public void twoSimilarProductSetsShouldBeEqual() {
        ProductSet productSet = factory.productSetFromCodes(1, 2, 3, 4);
        ProductSet copy = factory.productSetFromCodes(1, 2, 3, 4);

        assertEquals(productSet, copy);
    }
    
    @Test
    public void twoProductSetsWhoseProductOrderDifferShouldBeEqual() {
        ProductSet productSet = factory.productSetFromCodes(1, 2, 3, 4);
        ProductSet shuffledCopy = factory.productSetFromCodes(4, 3, 2, 1);

        assertEquals(productSet, shuffledCopy);
    }

    @Test
    public void twoDifferentProductSetsShouldBeEquals() {
        ProductSet productSet = factory.productSetFromCodes(1, 2, 3, 4);
        ProductSet anotherProductSet = factory.productSetFromCodes(6, 7, 8, 9);

        assertNotEquals(productSet, anotherProductSet);
    }
}
