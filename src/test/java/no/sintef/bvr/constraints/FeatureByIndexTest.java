package no.sintef.bvr.constraints;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Behaviour of FeatureByIndex
 */
public class FeatureByIndexTest {

    private static final int INDEX = 2;
    private final FeatureByIndex feature;

    public FeatureByIndexTest() {
        feature = new FeatureByIndex(INDEX);
    }

    @Test
    public void shouldNotEqualNull() {
        assertNotEquals(feature, null);
    }

    @Test
    public void shouldEqualItself() {
        assertEquals(feature, feature);
    }

    @Test
    public void shouldNotEqualsAFeatureWithADifferentIndex() {
        assertNotEquals(feature, new FeatureByIndex(INDEX + 1));
    }

}
