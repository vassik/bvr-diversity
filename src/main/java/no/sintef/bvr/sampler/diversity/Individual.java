package no.sintef.bvr.sampler.diversity;

import java.util.ArrayList;
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
    private final int mutationCapacity;

    public Individual(Sample sample) {
        this.mutationCapacity = (int) Math.max(1, (int) sample.productLine().featureCount() * MUTATION_CAPACITY);
        this.sample = sample;
        this.fitness = 0;
    }
    private static final double MUTATION_CAPACITY = 0.75;

    Sample sample() {
        return sample;
    }

    boolean mutate() {
        if (mutationOccurs()) {
            for (int i = 0; i < mutationStrength(); i++) {
                doMutate(aRandomProduct(), mutationStrength());
            }
            return true;
        }
        return false;
    }

    private boolean mutationOccurs() {
        double draw = random.nextDouble();
        return draw < MUTATION_PROBABILITY;
    }

    private static final double MUTATION_PROBABILITY = 0.01;

    private int mutationStrength() {
        return 1 + random.nextInt(mutationCapacity);
    }

    boolean doMutate(final Product product, int strength) {
        assert strength >= 1 : "Cannot flip no feature";
        assert strength <= product.features().size() : "Cannot flip more than all features!";

        final List<Feature> remainingFeatures = new ArrayList<>(product.features());
        final List<Feature> flippedFeatures = new ArrayList<>(product.features());
        
        for (int attempt = 0; attempt < MAX_MUTATION_ATTEMPT; attempt++) {
            tryMutate(strength, remainingFeatures, product, flippedFeatures);
            if (!product.isValid()) {
                revertMutation(flippedFeatures, product, remainingFeatures);
                continue;
            }
            return true;
        }
        
        return false;
    }

    private void revertMutation(final List<Feature> flippedFeatures, final Product product, final List<Feature> remainingFeatures) {
        for (Feature eachFlippedFeature : flippedFeatures) {
            product.toggle(eachFlippedFeature);
            remainingFeatures.add(eachFlippedFeature);
        }
        flippedFeatures.clear();
    }

    private void tryMutate(int strength, final List<Feature> remainingFeatures, final Product product, final List<Feature> flippedFeatures) {
        for (int count = 0; count < strength; count++) {
            final Feature selectedFeature = takeAny(remainingFeatures);
            product.toggle(selectedFeature);
            flippedFeatures.add(selectedFeature);
        }
    }

    private static final int MAX_MUTATION_ATTEMPT = 100;

    private Product aRandomProduct() {
        int index = random.nextInt(sample.size());
        return sample.productAt(index);
    }

    private Feature takeAny(List<Feature> features) {
        assert !features.isEmpty() : "Cannot take from an empty collection";
        if (features.size() == 1) {
            return features.get(0);
        }
        int selected = random.nextInt(features.size());
        return features.remove(selected);
    }

    void evaluate(Goal goal) {
        fitness = goal.fitnessOf(this);
    }

    @Override
    public int compareTo(Individual other) {
        return Double.compare(other.fitness, fitness);
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
        final ArrayList<Individual> children = new ArrayList<>();
        children.add(new Individual(childA));
        children.add(new Individual(childB));
        return children;
    }

    List<Individual> mateWithV2(Individual partner) {
        final Sample childA = new Sample(sample.productLine());
        final Sample childB = new Sample(sample.productLine());

        for (int index = 0; index < sample.size(); index++) {
            if (random.nextBoolean()) {
                childA.add(new Product(sample.productAt(index)));
                childB.add(new Product(partner.sample.productAt(index)));
            } else {
                childA.add(new Product(partner.sample.productAt(index)));
                childB.add(new Product(sample.productAt(index)));
            }
        }
        final ArrayList<Individual> children = new ArrayList<>();
        children.add(new Individual(childA));
        children.add(new Individual(childB));
        return children;
    }

    List<Individual> mateWithV3(Individual partner) {
        final Sample childA = new Sample(sample.productLine());
        final Sample childB = new Sample(sample.productLine());

        int switchPoint = random.nextInt(sample.size());
        for (int index = 0; index < sample.size(); index++) {
            if (switchPoint == index) {
                childA.add(new Product(sample.productAt(index)));
                childB.add(new Product(partner.sample.productAt(index)));
            } else {
                childA.add(new Product(partner.sample.productAt(index)));
                childB.add(new Product(sample.productAt(index)));
            }
        }
        final ArrayList<Individual> children = new ArrayList<>();
        children.add(new Individual(childA));
        children.add(new Individual(childB));
        return children;
    }

    public double fitness() {
        return this.fitness;
    }

}
