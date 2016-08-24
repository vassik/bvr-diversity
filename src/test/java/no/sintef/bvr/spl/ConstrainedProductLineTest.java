
package no.sintef.bvr.spl;

import java.util.HashSet;
import java.util.Set;
import static no.sintef.bvr.constraints.Builder.feature;
import static no.sintef.bvr.constraints.Builder.not;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Behaviour of constrainedProductLine
 */
public class ConstrainedProductLineTest {

    private final ConstrainedProductLine productLine;
    private final Factory create;
    private final FeatureSet features;

    public ConstrainedProductLineTest() {
        features = FeatureSet.fromTemplate(2, "f%d");
        create = new Factory(features);
        productLine = new ConstrainedProductLine(features, not(feature(0)));
    }
    
    @Test
    public void shouldHaveTheRightProducts() {
        ProductSet expectedProducts = create.productSetFromCodes(0,2);
        
        assertEquals(expectedProducts, productLine.products());
    }
    
    
    @Test
    public void shouldExposeItsUnconstrainedFeatures() {
        Set<Feature> features = makeFeatureSet("f1");
        
        assertEquals(features, makeSet(productLine.unconstrainedFeatures()));
    }

    private Set<Feature> makeFeatureSet(String... features) {
        Set<Feature> result = new HashSet<>();
        for(String eachFeature: features) {
            result.add(new Feature(eachFeature));
        }
        return result;
    }

    private Object makeSet(Iterable<Feature> features) {
        Set<Feature> result = new HashSet<>();
        for(Feature eachFeature: features) {
            result.add(eachFeature);
        }
        return result; 
    }
    
    
}
