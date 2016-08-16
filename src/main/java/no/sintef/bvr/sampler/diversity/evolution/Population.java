package no.sintef.bvr.sampler.diversity.evolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import no.sintef.bvr.sampler.diversity.evolution.selection.TruncationSelection;

/**
 * A population of individuals, ranked according to their fitness
 */
public class Population {

    private final int capacity;
    private final List<Individual> individuals;
    private final Selection selection;

    public Population(int capacity) {
        this(capacity, new TruncationSelection(0.25));
    }

    public Population(int capacity, Selection selectionStrategy) {
        this.capacity = capacity;
        this.individuals = new ArrayList<>(capacity);
        this.selection = selectionStrategy;
    }

    public Iterable<Couple> pairUp() {
        return selection.select(new Random(), this);
    }

    public void spare(int desiredSize) {
        assert desiredSize >= 0 : "Invalid desired size";

        if (size() > desiredSize) {
            int count = size() - desiredSize;
            for (int index = 0; index < count; index++) {
                remove(size() - 1);
            }
        }

        assert size() == desiredSize : "Population: " + size() + "(expecting: " + desiredSize + ")";
    }

    public boolean isEmpty() {
        return individuals.isEmpty();
    }

    public Individual get(int index) {
        return individuals.get(index);
    }

    public Individual fittest() {
        assert !individuals.isEmpty() : "Empty population!";
        return individuals.get(0);
    }

    public void rank() {
        Collections.sort(individuals);
    }

    public void add(Individual newComer) {
        checkCapacity(1);
        individuals.add(newComer);
    }

    public void addAll(Collection<Individual> newComers) {
        checkCapacity(newComers.size());
        individuals.addAll(newComers);
    }

    private void checkCapacity(int newComersCount) throws IllegalStateException {
        if (individuals.size() + newComersCount > capacity) {
            final String message = String.format("Population will exceed capacity (%d + %d > %d)", individuals.size(), newComersCount, capacity);
            throw new IllegalStateException(message);
        }
    }

    public void remove(int index) {
        individuals.remove(index);
    }

    public int size() {
        return individuals.size();
    }

}
