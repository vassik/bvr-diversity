package no.sintef.bvr.sampler.diversity.mutations;

import no.sintef.bvr.sampler.diversity.ProductSetIndividual;
import no.sintef.bvr.sampler.diversity.evolution.Mutation;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.sampler.diversity.evolution.Individual;

/**
 * Replace a product in a product set
 */
public class SingleProductMutation implements Mutation {

    private final Product target;
    private final Product replacement;

    public SingleProductMutation(Product target, Product replacement) {
        this.target = target;
        this.replacement = replacement;
    }

    @Override
    public void apply(Individual individual) {
        ((ProductSetIndividual) individual).products().replace(target, replacement);
    }

}
