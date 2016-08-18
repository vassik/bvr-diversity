package no.sintef.bvr.sampler.diversity.crossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import no.sintef.bvr.sampler.diversity.ProductSetIndividual;
import no.sintef.bvr.sampler.diversity.evolution.Couple;
import no.sintef.bvr.sampler.diversity.evolution.Crossover;
import no.sintef.bvr.sampler.diversity.evolution.Individual;
import no.sintef.bvr.spl.Factory;
import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductLine;
import no.sintef.bvr.spl.ProductSet;

/**
 * Breed parents using the union of intersection set operators, avoiding in turn
 * the duplication of products in children.
 */
public class SetBasedCrossover implements Crossover {

    private final Random random;
    private final Factory create;
    private final int productCount;
    private final ProductLine productLine;

    public SetBasedCrossover(Random random, ProductLine productLine, int productCount) {
        this.random = random;
        this.create = new Factory(FeatureSet.fromDefaultTemplate(4));
        this.productLine = productLine;
        this.productCount = productCount;
    }

    @Override
    public List<Individual> breed(Couple couple) {
        ProductSet intersection = productsOf(couple.father()).intersectionWith(productsOf(couple.mother()));
        ProductSet products = fillUp(intersection);
        return convertAsIndividual(products);
    }

    private List<Individual> convertAsIndividual(ProductSet products) {
        final List<Individual> result = new ArrayList<>();
        result.add(new ProductSetIndividual(products));
        return result;
    }

    private ProductSet fillUp(ProductSet products) {
        final ProductSet candidates = productLine.products().differenceWith(products);
        while (products.size() < productCount) {
            products = products.extendWith(any(candidates));
        }
        return products;
    }

    private Product any(ProductSet candidates) {
        return candidates.withKey(random.nextInt(candidates.size()));
    }

    private ProductSet productsOf(Individual individual) {
        return ((ProductSetIndividual) individual).products();
    }

}
