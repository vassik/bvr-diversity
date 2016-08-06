/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.metrics;

import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductSet;

public class DistanceMatrix {

    public double[][] of(ProductSet products) {
        assert !products.isEmpty() : "Cannot compute a distance matrix for an empty sample!";
        
        double[][] matrix = new double[products.size()][products.size()];
        for (int i=0 ; i<products.size() ;i++) {
            Product product_i = products.withKey(i);
            for (int j=i+1 ; j< products.size() ; j++) {
                Product product_j = products.withKey(j);
                final double distance = product_i.distanceWith(product_j);
                matrix[i][j] = distance;
                matrix[j][i] = distance;
            }
        }      
        return matrix;
    }
}
