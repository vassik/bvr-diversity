
package no.sintef.bvr.sampler.diversity.mutations;

import no.sintef.bvr.spl.Product;

/**
 *
 * @author franckc
 */
public interface ProductMutation {

    boolean applyTo(Product product);
    
}
