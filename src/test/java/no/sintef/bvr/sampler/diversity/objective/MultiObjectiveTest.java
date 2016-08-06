package no.sintef.bvr.sampler.diversity.objective;

import no.sintef.bvr.spl.ProductSet;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Test the behaviour of multiple objectives
 */
public class MultiObjectiveTest extends ObjectiveTest {

    private static final int DISTANCE_A = 10;
    private static final int DISTANCE_B = 5;

    public MultiObjectiveTest() {
        super(new MultiObjective(anObjectiveAt(DISTANCE_A), anObjectiveAt(DISTANCE_B)),
              Math.pow(DISTANCE_A, 2) + Math.pow(DISTANCE_B, 2));
    }

    private static DesiredValue anObjectiveAt(double distance) {
        DesiredValue objective = mock(DesiredValue.class);
        when(objective.distanceFrom(any(ProductSet.class))).thenReturn(distance);
        return objective;
    }

}
