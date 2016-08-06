package no.sintef.bvr.sampler;

import no.sintef.bvr.spl.ProductSet;

/**
 * Sampler search subsets of valid products for a given product line
 */
public interface Sampler {
        
    ProductSet sample(int count);
    
}
