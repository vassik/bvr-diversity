
package no.sintef.bvr.constraints;

import no.sintef.bvr.Feature;
import no.sintef.bvr.Product;

public class FeatureByIndex extends LogicalExpression {
    
    private final int featureIndex;

    public FeatureByIndex(int featureIndex) {
        this.featureIndex = featureIndex;
    }
    
    @Override
    protected boolean evaluateOn(Product product) {
        Feature feature = product.productLine().featureAt(featureIndex);
        return product.offers(feature);
    }
    
}
