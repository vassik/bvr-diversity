package no.sintef.bvr.spl;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the creation of products
 */
public class ProductCreationTest {

    private final FeatureSet features;

    public ProductCreationTest() {
        this.features = FeatureSet.fromDefaultTemplate(FEATURE_COUNT);
    }

    public static final int FEATURE_COUNT = 5;
    
    @Test
    public void shouldCreateTheEmptyProduct() {
        Product empty = new Product(features);
        assertEquals(0, empty.activeFeatureCount());
    }
    
    @Test
    public void shouldCreateTheFullProduct() {
        Product fullProduct = new Product(features, true, true, true, true, true);
        assertEquals(FEATURE_COUNT, fullProduct.activeFeatureCount());
    }

}
