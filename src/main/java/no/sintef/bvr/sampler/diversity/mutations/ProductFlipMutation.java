
package no.sintef.bvr.sampler.diversity.mutations;

import no.sintef.bvr.spl.Product;

/**
 * Flip all the feature of the product
 */
public class ProductFlipMutation implements ProductMutation {

    @Override
    public boolean applyTo(Product product) {
        product.invert();
        return true;
    }
    
    
    
}
