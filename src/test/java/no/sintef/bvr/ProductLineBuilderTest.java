/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr;

import no.sintef.bvr.spl.ConstrainedProductLine;
import no.sintef.bvr.spl.ProductLineReader;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;


public class ProductLineBuilderTest {
    
    private final ProductLineReader read;
    
    public ProductLineBuilderTest() {
        read = new ProductLineReader();
    }
    
    @Test
    public void readAProductLineWithConstraints() throws IOException {
        String text = "features: \n"
                    + "  F1, F2, F3\n"
                    + "constraints:\n"
                    + "  - F1 implies F2\n"
                    + "  - not (F1 implies (F2 or F3))\n"
                    ;
        
        ConstrainedProductLine result = read.from(text); 
        
        assertEquals(3, result.features().count());
        assertEquals(2, result.constraints().size());
    }
    
    
    @Test
    public void readAProductLineWithMoreConstraints() throws IOException {
        String text = "features: \n"
                    + "  F1, F2, F3, F4, F5, F6, F7\n"
                    + "constraints:\n"
                    + "  - F1 implies F2\n"
                    + "  - not (F1 implies (F2 or F3))\n"
                    + "  - F1 implies (F5 and F6)\n"
                    + "  - F3 or (F2 and F4)\n"
                    ;
        
        ConstrainedProductLine result = read.from(text); 
        
        assertEquals(7, result.features().count());
        assertEquals(4, result.constraints().size());
    }
    
}
