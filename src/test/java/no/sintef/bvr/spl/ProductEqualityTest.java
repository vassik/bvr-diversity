package no.sintef.bvr.spl;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the comparison of products
 */
public class ProductEqualityTest {

    private final Factory create;

    public ProductEqualityTest() {
        create = new Factory(FeatureSet.fromDefaultTemplate(4));
    }

    @Test
    public void twoSimilarProductsShouldBeEqual() {
        Product product = create.anEmptyProduct();
        Product copy = create.anEmptyProduct();

        assertEquals(product, copy);
    }
    
    @Test
    public void twoDifferentProductsShouldNotBeEqual() {
        Product product = create.anEmptyProduct();
        Product copy = create.aFullProduct();

        assertNotEquals(product, copy);
    }

}
