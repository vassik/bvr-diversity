package no.sintef.bvr.sampler.diversity;

import no.sintef.bvr.sampler.diversity.evolution.ObjectiveTest;
import no.sintef.bvr.metrics.Metric;
import no.sintef.bvr.spl.ProductSet;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Test the desired values
 */
public class DesiredValueTest extends ObjectiveTest {

    public DesiredValueTest() {
        super(new DesiredValue(aMetricThatReturns(5), TARGET_VALUE), 5);
    }
    private static final int TARGET_VALUE = 10;

    private static Metric aMetricThatReturns(double expectedDistance) {
        final Metric metric = mock(Metric.class);
        when(metric.of(any(ProductSet.class))).thenReturn(expectedDistance);
        return metric;
    }

    @Test
    public void distanceShouldBePositiveWhenTheMetricReturnBeyondThreshold() {
        DesiredValue objective = new DesiredValue(aMetricThatReturns(TARGET_VALUE + 3), TARGET_VALUE);

        assertTrue(objective.distanceFrom(anyCandidate) >= 0);
    }

    @Test
    public void distanceShouldBePositiveWhenTheMetricReturnBelowThreshold() {
        DesiredValue objective = new DesiredValue(aMetricThatReturns(TARGET_VALUE - 3), TARGET_VALUE);

        assertTrue(objective.distanceFrom(anyCandidate) >= 0);
    }

}
