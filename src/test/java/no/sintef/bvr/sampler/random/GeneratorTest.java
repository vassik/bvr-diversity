/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.random;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import no.sintef.bvr.spl.ConstrainedProductLine;
import no.sintef.bvr.spl.Product;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class GeneratorTest {

    private final int featureCount;

    public GeneratorTest(int featureCount, int expectedProductCount) {
        this.expectedProductCount = expectedProductCount;
        this.featureCount = featureCount;
    }
    
    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        Collection<Object[]> testData = new LinkedList<>();
 
        for(int i=1 ; i<10; i++) {
            testData.add(new Object[]{i, (int) Math.pow(2, i)});
        }
        
        return testData;
    }

    @Test
    public void testGenerator() {
        ConstrainedProductLine productLine = new ConstrainedProductLine(featureCount);

        Generator generator = Generator.createFor(productLine);

        assertEquals(expectedProductCount, countProducts(generator));
    }
    
    private final int expectedProductCount;

    private int countProducts(Generator generator) {
        List<Product> products = new LinkedList<>();
        while (generator.hasNext()) {
            final Product nextProduct = generator.next();
            if (products.contains(nextProduct)) {
                throw new AssertionError("Duplicated product '" + nextProduct + "'(" + products + ")");
            }
            products.add(nextProduct.copy());
        }
        return products.size();
    }

}
