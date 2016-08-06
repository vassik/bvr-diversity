package no.sintef.bvr.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import no.sintef.bvr.spl.Factory;
import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.spl.ProductSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Test the computation of absolute diversity
 */
@RunWith(Parameterized.class)
public class AbsoluteDiversityTest {

    private final double expectedDiversity;
    private final Diversity diversity;
    private final ProductSet sample;

    public AbsoluteDiversityTest(double expectedDiversity, boolean[][] isActive) {
        this.expectedDiversity = expectedDiversity;
        final Factory factory = new Factory(FeatureSet.fromDefaultTemplate(5));
        this.sample = factory.createProductSet(isActive);
        this.diversity = new Diversity();
    }

    @Test
    public void absoluteDiversityShouldBeCorrect() {
        assertEquals(expectedDiversity, diversity.absolute(sample));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final List<Object[]> data = new ArrayList<>();

        data.add(new Object[]{
            2D,
            new boolean[][]{
                {true, true, false, false, false},
                {false, false, true, true, true}}
        });

        data.add(new Object[]{
            0D,
            new boolean[][]{
                {true, true, false, false, false},
                {true, true, false, false, false}}
        });

        return data;
    }

}
