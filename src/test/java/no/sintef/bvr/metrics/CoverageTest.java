package no.sintef.bvr.metrics;

import no.sintef.bvr.spl.Factory;
import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.spl.ProductSet;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;


public class CoverageTest {

    private static final int FEATURE_COUNT = 5;
    
    private final FeatureSet features;
    private final Factory create;
    private final Coverage coverage;

    public CoverageTest() {
        features = FeatureSet.fromDefaultTemplate(FEATURE_COUNT);
        create = new Factory(features);
        coverage = new Coverage(features);
    }

    @Test
    public void theFullProductShouldHaveACoverageOf05() {
        ProductSet sample = new ProductSet(create.aFullProduct());
        assertEquals(0.5, coverage.of(sample));
    }

    @Test
    public void twoFullProductsShouldHaveACoverageOf0_5() {
        ProductSet sample = new ProductSet(
                create.aFullProduct(),
                create.aFullProduct());
        assertEquals(0.5, coverage.of(sample)); 
    }

    @Test
    public void theEmptyProductShouldHaveACoverageOf05() {
        ProductSet sample = new ProductSet(create.anEmptyProduct());
        assertEquals(0.5, coverage.of(sample));
    }

    @Test
    public void allFeatureAndNoFeatureShouldHaveACoverageOf1() {
        ProductSet sample = new ProductSet(
                create.anEmptyProduct(),
                create.aFullProduct());
        
        assertEquals(1D, coverage.of(sample));
    }
    
    @Test
    public void aRandomProductShouldCoverHalfOfTheFeature() {
        ProductSet sample = new ProductSet(create.aRandomProduct());
        assertEquals(0.5, coverage.of(sample));
    }

    @Test
    public void coverageOfTwoSingleFeatureProducts() {
        ProductSet sample = create.createProductSet(
                new boolean[][]{
                    {true, false, false, false, false},
                    {false, true, false, false, false}
                });

        assertEquals(7D / (2 * features.count()), coverage.of(sample));
    }

}
