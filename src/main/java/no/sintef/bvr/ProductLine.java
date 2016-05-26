package no.sintef.bvr;

import java.util.ArrayList;
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

    public int featureCount() {
        return featuresByName.size();
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
   
}
