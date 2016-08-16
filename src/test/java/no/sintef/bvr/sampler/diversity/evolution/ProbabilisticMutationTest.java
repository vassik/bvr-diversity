package no.sintef.bvr.sampler.diversity.evolution;

import java.util.Random;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Specification of the ProbabiliticMutation class
 */
public class ProbabilisticMutationTest {

    private final Mutation anyMutation;
    private final Individual anyIndividual;
    private final Random random;
    private final double probability;

    public ProbabilisticMutationTest() {
        random = mock(Random.class);
        anyMutation = mock(Mutation.class);
        anyIndividual = mock(Individual.class);
        probability = 0.25;
    }

    @Test
    public void shouldBeTriggeredWhenTheDrawIsBelowTheProbability() {
        when(random.nextDouble()).thenReturn(probability - 0.1);

        ProbabilisticMutation probabilistic = new ProbabilisticMutation(random, anyMutation, probability);

        probabilistic.apply(anyIndividual);

        verify(anyMutation, times(1)).apply(anyIndividual);
    }

    @Test
    public void shouldNotBeTriggeredWhenTheDrawIsBelowTheProbability() {
        when(random.nextDouble()).thenReturn(probability + 0.1);

        ProbabilisticMutation probabilistic = new ProbabilisticMutation(random, anyMutation, probability);

        probabilistic.apply(anyIndividual);

        verify(anyMutation, never()).apply(anyIndividual);
    }

    @Test
    public void shouldNotBeTriggeredWhenTheDrawIsExactlyTheProbability() {
        when(random.nextDouble()).thenReturn(probability);

        ProbabilisticMutation probabilistic = new ProbabilisticMutation(random, anyMutation, probability);

        probabilistic.apply(anyIndividual);

        verify(anyMutation, never()).apply(anyIndividual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectProbabilityLowerThanZero() {
        new ProbabilisticMutation(random, anyMutation, -0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectProbabilityAboveOne() {
        new ProbabilisticMutation(random, anyMutation, 1.5);
    }

}
