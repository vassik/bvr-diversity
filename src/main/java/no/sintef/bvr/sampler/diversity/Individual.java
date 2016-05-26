
package no.sintef.bvr.sampler.diversity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import no.sintef.bvr.Feature;
import no.sintef.bvr.Product;
import no.sintef.bvr.sampler.Sample;

/**
 * Individual being evolved by the genetic algorithm.
 */
public class Individual implements Comparable<Individual> {

    private final static Random random = new Random();

    private final Sample sample;
    private double fitness;

    public Individual(Sample sample) {
        this.sample = sample;
        this.fitness = 0;
    }
    
    Sample sample() {
        return sample;
    }

    void mutate() {
        if (mutationOccurs()) {
            aRandomProduct().toggle(aRandomFeature());
        }
    }

    private boolean mutationOccurs() {
        double draw = random.nextDouble();
        return draw < MUTATION_PROBABILITY;
    }

    private static final double MUTATION_PROBABILITY = 0.25;

    private Product aRandomProduct() {
        int index = random.nextInt(sample.size());
        return sample.productAt(index);
    }

    private Feature aRandomFeature() {
        int selected = random.nextInt(sample.productLine().featureCount());
        return sample.productLine().featureAt(selected);
    }

    void evaluate(Goal goal) {
        fitness = goal.fitnessOf(this);
    }

    @Override
    public int compareTo(Individual other) {
        return (int) (other.fitness - other.fitness);
        
    }

    List<Individual> mateWith(Individual partner) {
        final Sample childA = new Sample(sample.productLine());
        final Sample childB = new Sample(sample.productLine());

        final int cutPoint = random.nextInt(sample.size());
        for (int index = 0; index < sample.size(); index++) {
            if (index < cutPoint) {
                childA.add(new Product(sample.productAt(index)));
                childB.add(new Product(partner.sample.productAt(index))); 
            } else {
                childA.add(new Product(partner.sample.productAt(index)));
                childB.add(new Product(sample.productAt(index)));
            }
        }
        
        return Arrays.asList(new Individual[] {new Individual(childA), new Individual(childB)});
    }

}
