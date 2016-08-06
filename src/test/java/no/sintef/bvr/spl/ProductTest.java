package no.sintef.bvr.spl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test the behaviour of a 5-features product, where only the 4th one is
 * disabled.
 */
public class ProductTest {

    private static final int ACTIVE_FEATURE_COUNT = 4;
    private static final int FEATURE_COUNT = ACTIVE_FEATURE_COUNT + 1;

    private final FeatureSet features;
    private final Product product;

    public ProductTest() {
        features = FeatureSet.fromDefaultTemplate(FEATURE_COUNT);
        product = new Product(features, true, true, true, false, true);
    }
 
    @Test
    public void shouldOfferAllButTheFourthFeature() {
        for (int index = 0; index < FEATURE_COUNT; index++) {
            if (index != 3) {
                assertTrue(product.offers(features.withIndex(index)));
                continue;
            }
            assertFalse(product.offers(features.withIndex(index)));
        }
    }

    @Test
    public void shouldHaveOnlyFourActiveFeature() {
        assertEquals(ACTIVE_FEATURE_COUNT, product.activeFeatureCount());
    }
    
    @Test
    public void shouldSatisfiesItsFeatureSet() {
        assertTrue(product.satisfies(features));
    }
    
    @Test
    public void shouldNotSatisfyInvalidFeatures() {
        assertFalse(product.satisfies(FeatureSet.fromDefaultTemplate(FEATURE_COUNT + 1)));
    }
    
    @Test
    public void shouldExposeItsFeatureAsAnInteger() {
        assertEquals(23L, product.code());
    }

}
