package no.sintef.bvr.spl;

import org.junit.Test;
import static org.junit.Assert.*;
import no.sintef.bvr.constraints.LogicalExpression;
import static no.sintef.bvr.constraints.Builder.feature;

public class ProductLineTest {

    private final FeatureSet features;
    private final ProductLine productLine;

    public ProductLineTest() {
        features = FeatureSet.fromDefaultTemplate(2);
        final LogicalExpression constraint = feature(1).implies(feature(0));
        productLine = new ConstrainedProductLine(2, constraint);
    }

    @Test
    public void shouldIncludeAValidProduct() {
        assertTrue(productLine.contains(new Product(features, true, true)));
    }

    @Test
    public void shouldExcludeInvalidProduct() {
        assertFalse(productLine.contains(new Product(features, false, true)));
    }

}
