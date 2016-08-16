
package no.sintef.bvr.sampler.diversity.evolution;

import no.sintef.bvr.sampler.diversity.evolution.Objective;
import no.sintef.bvr.sampler.diversity.evolution.Individual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test objective
 */
public abstract class ObjectiveTest {
    
    private final Objective objective;
    private final Individual anyCandidate;
    private final double expectedDistance;
    
    public ObjectiveTest(Objective objective, double expectedDistance) {
        this.objective =  objective;
        this.anyCandidate = null;
        this.expectedDistance = expectedDistance;
    }

    @Test
    public void shouldComputeTheDistance() {
        assertEquals(expectedDistance, objective.distanceFrom(anyCandidate), 1e-9);
    }

    @Test
    public void shouldBeSatisfiedWhenDistanceFallsBehindTolerance() {
        double tolerance = expectedDistance + 1;
        assertTrue(objective.isSatisfiedBy(anyCandidate, tolerance));
    }

    @Test
    public void shouldNotBeSatisfiedWhenDistanceExceedsTolerance() {
        double tolerance = expectedDistance - 1;
        assertFalse(objective.isSatisfiedBy(anyCandidate, tolerance));
    }
    
}
