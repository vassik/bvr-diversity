/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler;

import no.sintef.bvr.ProductLine;

/**
 *
 * @author franckc
 */
public interface Sampler {
        
    Sample sample(ProductLine productLine);
    
}
