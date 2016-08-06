/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.spl;

import no.sintef.bvr.spl.FeatureSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the quality of FeatureSets
 */
public class FeatureSetEqualityTest {
    
    private final FeatureSet features;
    private final FeatureSet similarFeatures;
    private final FeatureSet differentFeatures;
    private final FeatureSet shuffledFeatures;
    
    public FeatureSetEqualityTest() {
        features = FeatureSet.fromTemplate(3, "f_%d");
        similarFeatures = FeatureSet.fromTemplate(3, "f_%d");
        differentFeatures = FeatureSet.fromTemplate(3, "f%d");
        shuffledFeatures = FeatureSet.fromNames("f_0", "f_2", "f_1");
    }
    
    
    @Test
    public void testTwoIdenticalFeatureSets() {
        assertEquals(features, similarFeatures);
    }
    
    @Test
    public void testTwoSimilarFeatureSetsWithDifferentOrder() {
        assertNotEquals(features, shuffledFeatures);
    }

    @Test
    public void testTwoDifferentFeatureSets() {
        assertNotEquals(features, differentFeatures);
    }
}
