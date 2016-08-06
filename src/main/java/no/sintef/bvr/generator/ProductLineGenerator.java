package no.sintef.bvr.generator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import no.sintef.bvr.spl.ConstrainedProductLine;
import no.sintef.bvr.spl.Feature;
import no.sintef.bvr.spl.FeatureSet;
import static no.sintef.bvr.constraints.Builder.feature;
import no.sintef.bvr.constraints.LogicalExpression;
import static no.sintef.bvr.constraints.Builder.feature;

/**
 * Generate Random ProductLine
 */
public class ProductLineGenerator {
    
    public static void main(String args[]) throws FileNotFoundException, IOException {
        ProductLineGenerator generate = new ProductLineGenerator();
        final int featureCount = 20;
        
        ConstrainedProductLine productLine = generate.oneProductLine(featureCount);
        
        final String fileName = String.format("gen_pl_%d.txt", featureCount);
        try (FileOutputStream file = new FileOutputStream(fileName)) {
            ProductLineWriter txt = new ProductLineWriter(file);
            txt.write(productLine);
        }
        
        System.out.printf("Product line (%d features) stored in '%s'\n", featureCount, fileName);
        
    }

    private final Random random;

    public ProductLineGenerator() {
        random = new Random();
    }

    public ConstrainedProductLine oneProductLine(int featureCount) {
        final ArrayList<LogicalExpression> constraints = new ArrayList<>();
        buildLayout(constraints, 
                new ArrayList<Feature>(featureCount));
        return new ConstrainedProductLine(FeatureSet.fromTemplate(featureCount, "f%d"), constraints);
    }

    private Feature buildLayout(List<LogicalExpression> constraints, List<Feature> remaining) {
        assert remaining.size() > 0 : "Cannot make a layout for no feature!";

        if (remaining.size() == 1) {
            return atomicFeature(constraints, remaining);
        }

        return compositeFeature(constraints, remaining);
    }

    private Feature atomicFeature(List<LogicalExpression> constraints, List<Feature> remaining) {
        return removeOne(remaining);
    }

    private Feature removeOne(List<Feature> options) {
        assert !options.isEmpty() : "Cannot remove from an empty list!";
        //return options.remove(random.nextInt(options.size()));
        return options.remove(0);
    }

    private Feature compositeFeature(List<LogicalExpression> constraints, List<Feature> remaining) {
        Feature root = removeOne(remaining);
        int childrenCount = chooseNumberOfChildren(remaining);
        int[] childrenBudget = allocate(remaining.size(), childrenCount);
        for (int eachChildBudget : childrenBudget) {
            Feature childRoot = buildLayout(constraints, takeFrom(remaining, eachChildBudget));
            constraints.add(feature(childRoot.name()).implies(feature(root.name())));
        }
        return root;
    }

    private int chooseNumberOfChildren(List<Feature> remaining) {
        return uniformDistribution(remaining);
    }
    
    private int uniformDistribution(List<Feature> remaining) {
        return 1 + random.nextInt(Math.max(remaining.size()/2, 1));
    }

    private int[] allocate(int budget, int childrenCount) {
        assert budget >= childrenCount : "Two many children for the given budget!";
        
        int[] allocation = new int[childrenCount];
        for (int eachChild = 0; eachChild < childrenCount; eachChild++) {
            allocation[eachChild] = 1;
        }
        int bonus = budget - childrenCount;
        for (int eachPenny = 0; eachPenny < bonus; eachPenny++) {
            int winningChild = random.nextInt(childrenCount);
            allocation[winningChild] += 1;
        }
        return allocation;
    }

    private List<Feature> takeFrom(List<Feature> remaining, int budget) {
        assert remaining.size() >= budget: "Not enough budget (requested " + budget + ", available: " + remaining.size();
        List<Feature> features = new ArrayList<>(budget);
        for(int i = 0; i<budget; i++) {
            features.add(remaining.remove(0));
        }
        return features;
    }

}
