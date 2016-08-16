package no.sintef.bvr.sampler.diversity;

import no.sintef.bvr.sampler.diversity.crossover.SinglePointCrossover;
import no.sintef.bvr.sampler.diversity.evolution.Individual;
import no.sintef.bvr.sampler.diversity.evolution.Mutation;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import no.sintef.bvr.sampler.diversity.evolution.Crossover;
import no.sintef.bvr.sampler.diversity.mutations.SingleProductMutation;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductLine;
import no.sintef.bvr.spl.ProductSet;
import no.sintef.bvr.sampler.diversity.evolution.EvolutionFactory;
import no.sintef.bvr.sampler.diversity.evolution.Population;
import no.sintef.bvr.sampler.diversity.evolution.ProbabilisticMutation;
import no.sintef.bvr.sampler.diversity.evolution.selection.TruncationSelection;

/**
 * Create Product
 */
public class TheIndividualFactory implements EvolutionFactory {

    private static final double MUTATION_PROBABILITY = 0.1;
    private static final double BREEDING_FRACTION = 0.25;

    private final Random random;
    private final ProductLine productLine;
    private final int sampleSize;

    public TheIndividualFactory(Random random, ProductLine productLine, int sampleSize) {
        this.random = random;
        this.productLine = productLine;
        this.sampleSize = sampleSize;
    }

    @Override
    public Population anEmptyPopulation(int capacity) {
        return new Population(capacity, new TruncationSelection(BREEDING_FRACTION));
    }

    @Override
    public Individual aRandomIndividual() {
        final List<Product> products = new ArrayList<>(sampleSize);
        for (int i = 0; i < sampleSize; i++) {
            final Product randomProduct = aRandomProduct();
            products.add(randomProduct);
        }
        return new Individual(new ProductSet(products));
    }

    private Product aRandomProduct() {
        final int randomIndex = random.nextInt(productLine.products().size());
        return productLine.products().withKey(randomIndex);
    }

    @Override
    public Mutation aRandomMutation() {
        final Product target = null;
        final Product replacement = aRandomProduct();
        return new ProbabilisticMutation(random, new SingleProductMutation(target, replacement), MUTATION_PROBABILITY);
    }

    @Override
    public Crossover aRandomCrossover() {
        return new SinglePointCrossover(2);
    }

}
