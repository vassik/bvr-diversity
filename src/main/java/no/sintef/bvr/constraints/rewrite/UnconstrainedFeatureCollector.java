package no.sintef.bvr.constraints.rewrite;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import no.sintef.bvr.spl.Feature;
import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.constraints.LogicalExpression;

/**
 * Traverse logical expression and compute the set of features that are not
 * constrained.
 */
public class UnconstrainedFeatureCollector extends NameResolver<Void> {

    private final Set<Feature> unconstrainedFeatures;

    public UnconstrainedFeatureCollector(FeatureSet features) {
        super(features);
        this.unconstrainedFeatures = new HashSet<>();
        for (Feature eachFeature : features()) {
            unconstrainedFeatures.add(eachFeature);
        }
    }

    public Set<Feature> result() {
        return Collections.unmodifiableSet(unconstrainedFeatures);
    }

    @Override
    public Void onConjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        leftOperand.accept(this);
        rightOperand.accept(this);
        return null;
    }

    @Override
    public Void onDisjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        leftOperand.accept(this);
        rightOperand.accept(this);
        return null;
    }

    @Override
    public Void onImplication(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        leftOperand.accept(this);
        rightOperand.accept(this);
        return null;
    }

    @Override
    public Void onNegation(LogicalExpression operand) {
        operand.accept(this);
        return null;
    }

    @Override
    public Void onFeatureByIndex(int index) {
        unconstrainedFeatures.remove(features().withIndex(index));
        return null;
    }

}
