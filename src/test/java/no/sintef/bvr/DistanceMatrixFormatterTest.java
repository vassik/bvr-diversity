/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr;

import java.io.ByteArrayOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author franckc
 */
public class DistanceMatrixFormatterTest {

    @Test
    public void testSimpleDataMatrix() {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        CsvDistanceMatrixFormatter csv = new CsvDistanceMatrixFormatter(output);

        double[][] matrix = new double[][]{
            {0D, 0D, 0D, 0D},
            {0D, 0D, 0D, 0D},
            {0D, 0D, 0D, 0D},
            {0D, 0D, 0D, 0D}
        };
        csv.write(matrix);
        
        final String newLine = System.lineSeparator();    
        String expected = "0.00e+00, 0.00e+00, 0.00e+00, 0.00e+00" + newLine
                        + "0.00e+00, 0.00e+00, 0.00e+00, 0.00e+00" + newLine
                        + "0.00e+00, 0.00e+00, 0.00e+00, 0.00e+00" + newLine
                        + "0.00e+00, 0.00e+00, 0.00e+00, 0.00e+00" + newLine
                        ;
                
        assertEquals(output.toString(), expected, output.toString());
       // assertFalse(true);
    }

}
