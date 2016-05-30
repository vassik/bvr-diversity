/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author franckc
 */
public class ProductLineBuilderTest {
    
    private final ProductLineReader read;
    
    public ProductLineBuilderTest() {
        read = new ProductLineReader();
    }
    
    @Test
    public void readAProductLineWithConstraints() throws IOException {
        String text = "features: F1, F2, F3\n"
                    + "constraints:\n"
                    + "  - F1 implies F2\n"
                    + "  - not (F1 implies (F2 or F3))\n"
                    ;
        
        ProductLine result = read.from(text); 
        
        assertEquals(3, result.featureCount());
        assertEquals(2, result.constraints().size());
    }
    
}
