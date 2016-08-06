package no.sintef.bvr.spl;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test a simple feature set
 */
public class FeatureSetTest {

    private final Feature[] allFeatures;
    private final FeatureSet features;

    public FeatureSetTest() {
        allFeatures = new Feature[]{
            new Feature("a"),
            new Feature("b"),
            new Feature("c"),
            new Feature("d"),
            new Feature("e")
        };
        features = new FeatureSet(allFeatures);
    }

    @Test
    public void shouldProvideAccessToFeatureByIndex() {
        for (int index = 0; index < allFeatures.length; index++) {
            assertSame(allFeatures[index], features.withIndex(index));
        }
    }

    @Test
    public void shouldProvideAccessToFeatureByName() {
        assertSame(allFeatures[0], features.withName("a"));
        assertSame(allFeatures[1], features.withName("b"));
        assertSame(allFeatures[2], features.withName("c"));
        assertSame(allFeatures[3], features.withName("d"));
        assertSame(allFeatures[4], features.withName("e"));
    }
    
    @Test(expected = DuplicatedFeatureException.class)
    public void shouldDetectDuplicatedFeatures() {
        new FeatureSet(new Feature("a"), new Feature("b"), new Feature("a"));
    }

    @Test
    public void shouldProvideTheIndexOfAGivenFeature() {
        assertEquals(3, features.indexOf(new Feature("d")));
    }

    @Test(expected = UnknownFeatureException.class)
    public void shouldRejectUnknownFeatures() {
        features.indexOf(new Feature("foo"));
    }

    @Test(expected = UnknownFeatureException.class)
    public void shouldRejectUnknownIndex() {
        features.withIndex(124);
    }

    @Test(expected = UnknownFeatureException.class)
    public void shouldRejectUnknownName() {
        features.withName("foo");
    }
        
    @Test(expected = IllegalArgumentException.class)
    public void oneCannotCreateAProductWithTheWrongNumberOfFeatures() {
        new Product(features, true, true, true);
    }
    
    

}
