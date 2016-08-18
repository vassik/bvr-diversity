/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.generator;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import static junit.framework.Assert.assertEquals;
import no.sintef.bvr.spl.ConstrainedProductLine;
import static no.sintef.bvr.constraints.Builder.not;
import org.junit.Test;
import static no.sintef.bvr.constraints.Builder.feature;

/**
 *
 * @author franckc
 */
public class ProductLineWirterTests {

    private final ProductLineWriter csv;
    private final OutputStream output;

    public ProductLineWirterTests() {
        output = new ByteArrayOutputStream();
        csv = new ProductLineWriter(output);
    }

    @Test
    public void testWritingProductLineWithoutConstraints() {
        ConstrainedProductLine productLine = new ConstrainedProductLine(2);

        csv.write(productLine);

        String expectedText
                = "features:" + NEW_LINE
                + "f0, f1" + NEW_LINE
                + "constraints:" + NEW_LINE
                + "";

        assertEquals(expectedText, output.toString());
    }

    @Test
    public void testWritingProductLineWithConstraints() {
        ConstrainedProductLine productLine 
                = new ConstrainedProductLine(2, feature("f0").implies(feature("f1")));

        csv.write(productLine);

        String expectedText
                = "features:" + NEW_LINE
                + "f0, f1" + NEW_LINE
                + "constraints:" + NEW_LINE
                + " - (f0 implies f1)" + NEW_LINE;

        assertEquals(expectedText, output.toString());
    }

    @Test
    public void testWritingCompoundConstraints() {
        ConstrainedProductLine productLine 
                = new ConstrainedProductLine(3, 
                        feature("f1").implies(feature("f0").or(not(feature("f2")))));

        csv.write(productLine);

        String expectedText
                = "features:" + NEW_LINE
                + "f0, f1, f2" + NEW_LINE
                + "constraints:" + NEW_LINE
                + " - (f1 implies (f0 or (not f2)))" + NEW_LINE;

        assertEquals(expectedText, output.toString());
    }

    private static final String NEW_LINE = System.lineSeparator();

}
