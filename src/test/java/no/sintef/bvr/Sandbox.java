/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr;

import static junit.framework.Assert.assertEquals;
import no.sintef.bvr.sampler.Sample;
import no.sintef.bvr.sampler.diversity.DiversitySampler;
import org.junit.Test;

/**
 *
 * @author franckc
 */
public class Sandbox {

    @Test
    public void sandbox() {
        ProductLine productLine = new ProductLine(2);

        DiversitySampler sampler = new DiversitySampler(4, 1D, 5);
        Sample result = sampler.sample(productLine);

        assertEquals(4, result.size());
    }

}
