package no.sintef.bvr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import no.sintef.bvr.constraints.LogicalExpression;


public class ProductLine implements Iterable<Feature> {

    private final Map<String, Feature> featuresByName;
    private final Map<Integer, Feature> featuresByIndex;
    private final List<LogicalExpression> constraints;
            
    public ProductLine(int featureCount) {
        featuresByName = new HashMap<>();
        featuresByIndex = new HashMap<>();
        for(int index=0; index<featureCount; index++) {
            final Feature newFeature = new Feature(index, "f%d");
            featuresByName.put(newFeature.name(), newFeature);
            featuresByIndex.put(index, newFeature);
        }
        constraints = new ArrayList<>();
    }

    public ProductLine(List<String> features, List<LogicalExpression> constraints) {
        featuresByName = new HashMap<>();
        featuresByIndex = new HashMap<>();
        for(int index=0; index<features.size(); index++) {
            final Feature newFeature = new Feature(index, features.get(index));
            featuresByName.put(newFeature.name(), newFeature);
            featuresByIndex.put(index, newFeature);
        }
        this.constraints = new ArrayList<>(constraints);
    }

    public int featureCount() {
        return featuresByName.size();
    } 
    
    public Collection<Feature> features() {
        return Collections.unmodifiableCollection(featuresByName.values());
    }

    @Override
    public Iterator<Feature> iterator() {
        return featuresByName.values().iterator();
    }

    public Feature featureAt(int index) {
        return featuresByIndex.get(index);
    }

    public boolean isSatisfiedBy(Product product) {
        for(LogicalExpression anyConstraint: constraints) {
            if (anyConstraint.isViolatedBy(product)) {
                return false;
            }
        }
        return true;
    }

    public void addConstraint(LogicalExpression constraint) {
        this.constraints.add(constraint);
    }

    public Feature featureNamed(String name) {
        return this.featuresByName.get(name);
    }

    public List<LogicalExpression> constraints() {
        return Collections.unmodifiableList(constraints);
    }
   
}
