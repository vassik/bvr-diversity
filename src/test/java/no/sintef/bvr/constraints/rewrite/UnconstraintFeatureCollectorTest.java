package no.sintef.bvr.constraints.rewrite;

import java.util.HashSet;
import java.util.Set;
import static no.sintef.bvr.constraints.Builder.feature;
import static no.sintef.bvr.constraints.Builder.not;
import no.sintef.bvr.constraints.LogicalExpression;
import no.sintef.bvr.spl.Feature;
import no.sintef.bvr.spl.FeatureSet;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 */
public class UnconstraintFeatureCollectorTest {

    private final UnconstrainedFeatureCollector collector;
    private final FeatureSet features;

    public UnconstraintFeatureCollectorTest() {
        features = FeatureSet.fromTemplate(4, "f%d");
        collector = new UnconstrainedFeatureCollector(features);
    }

    @Test
    public void shouldCollectFeatureNotUsedInAConjunction() {
        LogicalExpression constraint = feature("f2").and(feature("f3"));

        constraint.accept(collector);

        Set<Feature> expected = makeSet("f0", "f1");
        assertEquals(expected, collector.result());
    }

    @Test
    public void shouldCollectFeatureNotUsedInADisjunction() {
        LogicalExpression constraint = feature("f2").or(feature("f3"));

        constraint.accept(collector);

        Set<Feature> expected = makeSet("f0", "f1");
        assertEquals(expected, collector.result());
    }

    @Test
    public void shouldCollectFeatureNotUsedInAImplication() {
        LogicalExpression constraint = feature("f2").implies(feature("f3"));

        constraint.accept(collector);

        Set<Feature> expected = makeSet("f0", "f1");
        assertEquals(expected, collector.result());
    }

    @Test
    public void shouldCollectFeatureNotUsedInANegation() {
        LogicalExpression constraint = not(feature("f2"));

        constraint.accept(collector);

        Set<Feature> expected = makeSet("f0", "f1", "f3");
        assertEquals(expected, collector.result());
    }

    private Set<Feature> makeSet(String... features) {
        Set<Feature> result = new HashSet<>();
        for (String eachFeature : features) {
            result.add(new Feature(eachFeature));
        }
        return result;
    }

}
