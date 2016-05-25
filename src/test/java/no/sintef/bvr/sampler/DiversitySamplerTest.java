/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import no.sintef.bvr.Product;
import no.sintef.bvr.ProductLine;
import org.junit.Test;

/**
 *
 * @author franckc
 */
public class DiversitySamplerTest {
    
   @Test
   public void shouldYieldTheCorrectNumberOfProducts() {
       ProductLine productLine = new ProductLine(2);
       
       DiversitySampler sampler = new DiversitySampler(5);
       Sample result = sampler.sample(productLine);
       
       assertEquals(5, result.size());
   }
   
   @Test
   public void shouldYieldTheCorrectProducts() {
       ProductLine productLine = new ProductLine(2);
       
       DiversitySampler sampler = new DiversitySampler(2);
       Sample result = sampler.sample(productLine);

       verify(result, new Product(productLine, true, true),
               new Product(productLine, false, false));
   }

    private void verify(Sample result, Product... expectedProducts) {
        for(Product eachProduct: expectedProducts) {
            assertTrue("Missing product '" + eachProduct + "'", result.contains(eachProduct));
        }
    }
}
