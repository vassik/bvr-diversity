/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.generator;

import java.io.OutputStream;
import java.io.PrintStream;
import no.sintef.bvr.spl.ConstrainedProductLine;
import no.sintef.bvr.spl.Feature;
import no.sintef.bvr.constraints.LogicalExpression;

public class ProductLineWriter {

    private final PrintStream output;
    
    public ProductLineWriter(OutputStream output) {
        this.output = new PrintStream(output);
    }

    void write(ConstrainedProductLine productLine) {
        output.println("features:");
        int position = 0;
        for(Feature eachFeature: productLine.features()) {
            output.print(eachFeature.name());
            if (position < productLine.features().count() - 1) {
                output.print(", ");
            }
            position++;
        }
        output.println();
        output.println("constraints:");
        for(LogicalExpression eachConstraint: productLine.constraints()) {
            output.println(" - " + eachConstraint);
        }
                
                
    }
    
}
