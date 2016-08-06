package no.sintef.bvr.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import no.sintef.bvr.spl.Factory;
import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.spl.ProductSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static junit.framework.Assert.assertEquals;

@RunWith(Parameterized.class)
public class DiversityTest {

    private static final int FEATURE_COUNT = 5;
    private final Diversity diversity;
    private final ProductSet sample;
    private final double expectation;

    public DiversityTest(boolean[][] isActive, double expectedDiversity) {
        diversity = new Diversity();
        final Factory factory = new Factory(FeatureSet.fromDefaultTemplate(FEATURE_COUNT));
        sample = factory.createProductSet(isActive);
        expectation = expectedDiversity;
    }

    @Test
    public void shouldBeCorrect() {
        assertEquals(expectation, diversity.of(sample));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> tests() {
        List<Object[]> tests = new ArrayList<>();

        tests.add(twoSimilarProducts());

        tests.add(twoOppositeProducts());

        tests.add(twoProductsWithOneFeatureThatVaries());

        tests.add(new Object[]{
            4D / (4 * 3),
            new boolean[][]{
                {true, true, true, true, true},
                {true, true, true, true, true},
                {true, true, true, false, true},
                {true, true, false, false, false}
            }
        });

        return tests;
    }

    private static Object[] twoProductsWithOneFeatureThatVaries() {
        return new Object[]{
            1D / FEATURE_COUNT,
            new boolean[][]{
                {true, true, false, false, false},
                {true, true, false, false, true}
            }
        };
    }

    private static Object[] twoOppositeProducts() {
        return new Object[]{
            1D,
            new boolean[][]{
                {true, true, false, false, false},
                {false, false, true, true, true}
            }
        };
    }

    private static Object[] twoSimilarProducts() {
        return new Object[]{
            0D,
            new boolean[][]{
                {true, true, false, false, false},
                {true, true, false, false, false}
            }
        };
    }

}
