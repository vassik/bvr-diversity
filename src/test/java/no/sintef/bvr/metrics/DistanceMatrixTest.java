package no.sintef.bvr.metrics;

import static junit.framework.Assert.assertEquals;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.Sample;
import org.junit.Test;

/**
 * Specification of the distance matrix calculation
 */
public class DistanceMatrixTest {

    @Test
    public void testSimpleDistanceMatrix() {
        ProductLine productLine = new ProductLine(3);

        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, true);
        sample.addProduct(false, false, false);

        DistanceMatrix distanceMatrix = new DistanceMatrix();
        double[][] expected = new double[][]{
            {0D, 1D},
            {1D, 0D}};

        final double[][] actual = distanceMatrix.of(sample);
        verifyMatrix(expected, actual);
    }

    @Test
    public void testDistanceMatrixWith5Products() {
        ProductLine productLine = new ProductLine(4);

        Sample sample = new Sample(productLine);
        sample.addProduct(true, true, true, true);
        sample.addProduct(true, true, true, false);
        sample.addProduct(true, true, false, false);
        sample.addProduct(true, false, false, false);
        sample.addProduct(false, false, false, false);

        DistanceMatrix distanceMatrix = new DistanceMatrix();
        double[][] expected = new double[][]{
            {0.00, 0.25, 0.50, 0.75, 1.00},
            {0.25, 0.00, 0.25, 0.50, 0.75},
            {0.50, 0.25, 0.00, 0.25, 0.50},
            {0.75, 0.50, 0.25, 0.00, 0.25},
            {1.00, 0.75, 0.50, 0.25, 0.00},
        };

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
