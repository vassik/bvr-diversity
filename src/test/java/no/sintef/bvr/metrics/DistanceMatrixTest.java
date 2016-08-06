package no.sintef.bvr.metrics;

import static junit.framework.Assert.assertEquals;
import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductSet;
import org.junit.Test;

/**
 * Specification of the distance matrix calculation
 */
public class DistanceMatrixTest {

    @Test
    public void testSimpleDistanceMatrix() {
        FeatureSet features = FeatureSet.fromDefaultTemplate(3);
        ProductSet sample
                = new ProductSet(
                        new Product(features, true, true, true),
                        new Product(features, false, false, false));

        DistanceMatrix distanceMatrix = new DistanceMatrix();
        double[][] expected = new double[][]{
            {0D, 1D},
            {1D, 0D}};

        final double[][] actual = distanceMatrix.of(sample);
        verifyMatrix(expected, actual);
    }

    @Test
    public void testDistanceMatrixWith5Products() {
        FeatureSet features = FeatureSet.fromDefaultTemplate(4);
        ProductSet sample
                = new ProductSet(
                        new Product(features, true, true, true, true),
                        new Product(features, true, true, true, false),
                        new Product(features, true, true, false, false),
                        new Product(features, true, false, false, false),
                        new Product(features, false, false, false, false));

        DistanceMatrix distanceMatrix = new DistanceMatrix();
        double[][] expected = new double[][]{
            {0.00, 0.25, 0.50, 0.75, 1.00},
            {0.25, 0.00, 0.25, 0.50, 0.75},
            {0.50, 0.25, 0.00, 0.25, 0.50},
            {0.75, 0.50, 0.25, 0.00, 0.25},
            {1.00, 0.75, 0.50, 0.25, 0.00},};

        final double[][] actual = distanceMatrix.of(sample);
        verifyMatrix(expected, actual);
    }

    private void verifyMatrix(double[][] expected, double[][] actual) {
        assertEquals("Missing rows", expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals("Missing columns", expected[i].length, actual[i].length);
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals("Wrong value at [" + i + "," + j + "]", expected[i][j], actual[i][j]);
            }
        }
    }

}
