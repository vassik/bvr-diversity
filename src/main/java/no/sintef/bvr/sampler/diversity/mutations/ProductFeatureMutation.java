package no.sintef.bvr.sampler.diversity.mutations;

import no.sintef.bvr.spl.Feature;
import no.sintef.bvr.spl.Product;

/**
 * Flip a single feature of the given product. Always succeed.
 */
public class ProductFeatureMutation implements ProductMutation {

    private final Feature feature;

    public ProductFeatureMutation(Feature toFlip) {
        this.feature = toFlip;
    }

    @Override
    public boolean applyTo(Product product) {
        product.toggle(feature);
        return true;
    }

}
