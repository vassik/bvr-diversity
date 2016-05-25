/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author franckc
 */
public class ProductLine implements Iterable<Feature> {

    private final Map<String, Feature> featuresByName;
    
    public ProductLine(int featureCount) {
        featuresByName = new HashMap<>();
        for(int index=0; index<featureCount; index++) {
            final Feature newFeature = new Feature(index, "f%d");
            featuresByName.put(newFeature.name(), newFeature);
        }
    }

    public int featureCount() {
        return featuresByName.size();
    } 

    @Override
    public Iterator<Feature> iterator() {
        return featuresByName.values().iterator();
    }
    
    
    
}
