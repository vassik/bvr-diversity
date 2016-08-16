package no.sintef.bvr.sampler.diversity;

import no.sintef.bvr.sampler.diversity.evolution.Population;
import no.sintef.bvr.sampler.diversity.evolution.Individual;
import java.util.ArrayList;
import java.util.List;
import no.sintef.bvr.spl.Factory;
import no.sintef.bvr.spl.FeatureSet;
import org.junit.Test;

/**
 * Test populations
 */
public class PopulationTest {

    private final Factory create;
    private final int capacity;
    private final Population population;

    public PopulationTest() {
        create = new Factory(FeatureSet.fromDefaultTemplate(5));
        capacity = 5;
        population = new Population(capacity);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectAddingOneMoreThanCapacity() {
        fillUpPopulation();
        
        population.add(anIndividualWithProducts(2, 3, 4, 5, 6));
    }
        
    private final void fillUpPopulation() {
        for(int i=0 ; i<capacity ; i++) {
            population.add(new Individual(create.productSetFromCodes(i, i+1, i+2)));
        }
    }

    private Individual anIndividualWithProducts(int... productCodes) {
        return new Individual(create.productSetFromCodes(productCodes));
    }

    
    @Test(expected=IllegalStateException.class)
    public void shouldRejectAddingManyMoreThanCapacity() {
        List<Individual> newComers = createIndividuals(capacity + 1);
        
        population.addAll(newComers);
    }

    private List<Individual> createIndividuals(int count) {
        final List<Individual> newComers = new ArrayList<>(count);
        for(int i=0 ; i<count ; i++) {
            newComers.add(anIndividualWithProducts(i+1,i+2,i+3));
        }
        return newComers;
    }

}
