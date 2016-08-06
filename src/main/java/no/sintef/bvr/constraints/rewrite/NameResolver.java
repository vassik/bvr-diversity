package no.sintef.bvr.constraints.rewrite;

import no.sintef.bvr.spl.Feature;
import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.constraints.Visitor;

/**
 * Convert feature names into feature index
 */
public abstract class NameResolver<T> implements Visitor<T> {

    private final FeatureSet features;

    public NameResolver(FeatureSet features) {
        this.features = features;
    }

    public FeatureSet features() {
        return this.features;
    }

    @Override
    public final T onFeatureByName(String name) {
        final Feature feature = features.withName(name);
        return onFeatureByIndex(features.indexOf(feature));
    }

}
