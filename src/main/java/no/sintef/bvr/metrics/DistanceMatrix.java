/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.metrics;

import no.sintef.bvr.Product;
import no.sintef.bvr.sampler.Sample;

public class DistanceMatrix {

    public double[][] of(Sample sample) {
        assert !sample.isEmpty() : "Cannot compute a distance matrix for an empty sample!";
        
        double[][] matrix = new double[sample.size()][sample.size()];
        for (int i=0 ; i<sample.size() ;i++) {
            Product product_i = sample.productAt(i);
            for (int j=i+1 ; j< sample.size() ; j++) {
                Product product_j = sample.productAt(j);
                final double distance = product_i.distanceWith(product_j);
                matrix[i][j] = distance;
                matrix[j][i] = distance;
            }
        }      
        return matrix;
    }
}
