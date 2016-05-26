/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr;

import static no.sintef.bvr.constraints.Builder.feature;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProductLineTest {

    @Test
    public void validProductsShouldSatisfyProductLine() {
        ProductLine productLine = new ProductLine(2);
        productLine.addConstraint(feature(1).implies(feature(0)));

        Product product = new Product(productLine, true, true);

        assertTrue(productLine.isSatisfiedBy(product));
    }

    @Test
    public void invalidProductsShouldNotSatisfyProductLine() {
        ProductLine productLine = new ProductLine(2);
        productLine.addConstraint(feature(1).implies(feature(0)));

        Product product = new Product(productLine, false, true);

        assertFalse(productLine.isSatisfiedBy(product));
    }

}
