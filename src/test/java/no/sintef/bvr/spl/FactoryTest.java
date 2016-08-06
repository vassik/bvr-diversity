/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.spl;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author franckc
 */
public class FactoryTest {

    private final Factory create;

    public FactoryTest() {
        create = new Factory(FeatureSet.fromDefaultTemplate(4));
    }

    @Test
    public void shouldBuildTheEmptyProductFromIntegerCode() {
        final Product expectedProduct = create.anEmptyProduct();
        assertEquals(expectedProduct, create.productFromCode(0));
    }

    @Test
    public void shouldBuildTheFullProductFromIntegerCode() {
        final Product expectedProduct = create.aFullProduct();
        assertEquals(expectedProduct, create.productFromCode(15));
    }

    @Test
    public void shouldBuildTheNinethFromIntegerCode() {
        final Product expectedProduct = create.aProductWith(true, false, false, true);
        assertEquals(expectedProduct, create.productFromCode(9));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNegativeIntegerCode() {
        create.productFromCode(-3);
    }

    @Test
    public void shouldBuildProductSetFromIntegerCodes() {
        ProductSet expected = create.createProductSet(new boolean[][] {
            {false, false, false, false},
            {true,  false, false, false},
            {true,  true,  false, false},
            {true,  true,  true,  false},
            {true,  true,  true,  true }
        });
        assertEquals(expected, create.productSetFromCodes(0, 1, 3, 7, 15));
    }

}
