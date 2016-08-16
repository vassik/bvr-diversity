package no.sintef.bvr.sampler.diversity.evolution.selection;

import no.sintef.bvr.sampler.diversity.evolution.Couple;
import no.sintef.bvr.sampler.diversity.evolution.Selection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import no.sintef.bvr.sampler.diversity.evolution.Individual;
import no.sintef.bvr.sampler.diversity.evolution.Population;

/**
 * Select only the top-n % percent of the population.
 */
public class TruncationSelection implements Selection {

    private final double breedingFraction;

    public TruncationSelection(double breedingFraction) {
        validate(breedingFraction);
        this.breedingFraction = breedingFraction;
    }

    private void validate(double breedingFraction1) throws IllegalArgumentException {
        if (breedingFraction1 <= 0D || breedingFraction1 > 1D) {
            final String message = String.format("Breeding fraction shall be a value within ]0 , 1] (found %.2f).", breedingFraction1);
            throw new IllegalArgumentException(message);
        }
    }

    @Override 
    public Iterable<Couple> select(Random random, Population population) {
        final int eliteSize = (int) Math.floor(population.size() * breedingFraction);
        final List<Couple> selection = new ArrayList<>(eliteSize);
        for (int index = 0; index < eliteSize; index++) {
            final Individual individual = population.get(index);
            final Individual partner = selectAnyFrom(random, population);
            selection.add(new Couple(individual, partner));
        }
        return selection;
    }

    private Individual selectAnyFrom(Random random, Population candidates) {
        assert !candidates.isEmpty() : "Error: Empty population!";
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        int selected = random.nextInt(candidates.size());
        return candidates.get(selected);
    }

}
