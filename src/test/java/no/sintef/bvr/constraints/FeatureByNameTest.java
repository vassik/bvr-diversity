
package no.sintef.bvr.constraints;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * FeatureByName's behaviours
 */
public class FeatureByNameTest {

    private final FeatureByName feature;

    public FeatureByNameTest() {
        feature = new FeatureByName(NAME);
    }
    
    private static final String NAME = "foo";

    @Test
    public void shouldDisplayProperly() {
        assertEquals(NAME, feature.toString());
    }
    
    @Test
    public void shouldNotEqualNull() {        
        assertNotEquals(feature, null);
    }
    
    @Test
    public void shouldNotEqualAFeatureWithADifferentName() {
        assertNotEquals(feature, new FeatureByName(NAME + "!!!"));
    }
    
    @Test
    public void shouldEqualItself() {
        assertEquals(feature, feature);
    }
    
    @Test
    public void shouldNotEqualsAnotherDataType() {
        assertNotEquals(feature, new Object[]{});
    }
    
    @Test
    public void shouldHaveTheSameHashcodeThanAnEquivalentFeature() {
        assertEquals(feature.hashCode(), new FeatureByName(NAME).hashCode());
    }
    
}
