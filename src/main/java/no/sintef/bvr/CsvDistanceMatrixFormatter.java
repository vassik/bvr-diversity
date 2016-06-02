/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr;

import java.io.OutputStream;
import java.io.PrintStream;


public class CsvDistanceMatrixFormatter {

    private final PrintStream output;
    
    public CsvDistanceMatrixFormatter(OutputStream output) {
        this.output = new PrintStream(output);
    }

    public void write(double[][] matrix) {
        for (double[] eachRow : matrix) {
            for (int column = 0; column < eachRow.length; column++) {
                output.printf("%8.2e", eachRow[column]);
                if (column < (eachRow.length - 1)) {
                    output.print(", ");
                } else {
                    output.println();
                }
            }
        }
    }
    
}
