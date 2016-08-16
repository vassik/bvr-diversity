package no.sintef.bvr.sampler.diversity.selection;

import no.sintef.bvr.sampler.diversity.evolution.selection.TruncationSelection;
import no.sintef.bvr.sampler.diversity.evolution.Couple;
import no.sintef.bvr.sampler.diversity.evolution.Selection;
import java.util.Random;
import no.sintef.bvr.sampler.diversity.evolution.Individual;
import no.sintef.bvr.sampler.diversity.evolution.Population;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Test truncation selection test
 */
public class TruncationSelectionTest {

    private static final double BREEDING_RATIO = 0.25;

    private final Selection selection;

    public TruncationSelectionTest() {
        selection = new TruncationSelection(BREEDING_RATIO);
    }

    @Test
    public void shouldSelectTheProperNumberOfIndividual() {
        Population population = aPopulationOfSize(100);
       
        Iterable<Couple> winners = selection.select(new Random(), population);

        final int expectedCount = (int) Math.round(100 * BREEDING_RATIO);
        assertEquals(expectedCount, count(winners));
    }

    private Population aPopulationOfSize(int size) {
        Population population = new Population(size);
        for (int i = 0; i < size; i++) {
            population.add(mock(Individual.class));
        }
        return population;
    }
    
    private int count(Iterable<Couple> couples) {
        int count = 0;
        for(Couple eachCouple: couples) count++;
        return count;
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNegativeBreedingFraction() {
        new TruncationSelection(-0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectZAeroBreedingFraction() {
        new TruncationSelection(0D);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectAboveOneBreedingFraction() {
        new TruncationSelection(1.5);
    }

}
