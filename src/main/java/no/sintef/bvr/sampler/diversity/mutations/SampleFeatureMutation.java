
package no.sintef.bvr.sampler.diversity.mutations;

import no.sintef.bvr.spl.Feature;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductSet;

/**
 * Flip a given feature on the products of the given sample
 */
public class SampleFeatureMutation {

    private final Feature selectedFeature;
    
    public SampleFeatureMutation(Feature selectedFeature) {
        this.selectedFeature = selectedFeature;
    }
    
    public void applyTo(ProductSet products) {
        for(Product eachProduct: products) {
            eachProduct.toggle(selectedFeature);
        }
    }
    
}
