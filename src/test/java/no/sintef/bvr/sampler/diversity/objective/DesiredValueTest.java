
package no.sintef.bvr.sampler.diversity.objective;

import no.sintef.bvr.metrics.Metric;
import no.sintef.bvr.spl.ProductSet;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Test the desired values
 */
public class DesiredValueTest extends ObjectiveTest {

    public DesiredValueTest() {
        super(new DesiredValue(aMetricThatReturns(5), 10), 5);
    }

    private static Metric aMetricThatReturns(double expectedDistance) {
        final Metric metric = mock(Metric.class);
        when(metric.of(any(ProductSet.class))).thenReturn(expectedDistance);
        return metric;
    }
   
}
