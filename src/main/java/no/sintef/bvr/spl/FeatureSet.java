package no.sintef.bvr.spl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represent a set of features
 */
public class FeatureSet implements Iterable<Feature> {

    public static final String DEFAULT_FEATURE_NAME = "f%d";

    public static FeatureSet fromDefaultTemplate(int featureCount) {
        return fromTemplate(featureCount, DEFAULT_FEATURE_NAME);
    }

    public static FeatureSet fromTemplate(int featureCount, String template) {
        final String[] featureNames = new String[featureCount];
        for (int i = 0; i < featureCount; i++) {
            featureNames[i] = String.format(template, i);
        }
        return fromNames(featureNames);
    }

    public static FeatureSet fromNames(String... names) {
        return fromNames(Arrays.asList(names));
    }

    public static FeatureSet fromNames(List<String> names) {
        final Feature[] features = new Feature[names.size()];
        for (int index = 0; index < names.size(); index++) {
            features[index] = new Feature(names.get(index));
        }
        return new FeatureSet(features);

    }

    private final Map<String, Feature> byName;
    private final Map<Integer, Feature> byIndex;

    public FeatureSet(Feature... features) {
        this.byName = new HashMap<>();
        this.byIndex = new HashMap<>();
        for (int index = 0; index < features.length; index++) {
            final Feature eachFeature = features[index];
            if (byName.containsKey(eachFeature.name())) {
                throw new DuplicatedFeatureException(String.format("Feature '%s' already exists!", eachFeature.name()));
            }
            byName.put(eachFeature.name(), eachFeature);
            byIndex.put(index, eachFeature);
        }
    }

    public int count() {
        return byName.size();
    }

    @Override
    public Iterator<Feature> iterator() {
        return byName.values().iterator();
    }

    public Feature withName(String name) {
        if (!byName.containsKey(name)) {
            throw new UnknownFeatureException(String.format("No feature named '%s'", name));
        }
        return byName.get(name);
    }

    public Feature withIndex(int key) {
        if (!byIndex.containsKey(key)) {
            throw new UnknownFeatureException(String.format("No feature defined at index %d", key));
        }
        return byIndex.get(key);
    }

    public int indexOf(Feature feature) {
        for (Map.Entry<Integer, Feature> anyEntry : byIndex.entrySet()) {
            if (anyEntry.getValue().equals(feature)) {
                return anyEntry.getKey();
            }
        }
        throw new UnknownFeatureException(String.format("Unknown feature '%s'", feature.name()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.byName);
        hash = 97 * hash + Objects.hashCode(this.byIndex);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FeatureSet other = (FeatureSet) obj;
        if (!Objects.equals(this.byName, other.byName)) {
            return false;
        }
        if (!Objects.equals(this.byIndex, other.byIndex)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return byName.keySet().toString();
    }
    
}
