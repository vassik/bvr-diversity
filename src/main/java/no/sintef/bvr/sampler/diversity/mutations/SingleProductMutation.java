/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.diversity.mutations;

import no.sintef.bvr.spl.Product;
import no.sintef.bvr.sampler.diversity.Individual;

/**
 *
 * @author franckc
 */
public class SingleProductMutation {

    private final Product target;
    private final Product replacement;

    public SingleProductMutation(Product targetProduct, Product replacement) {
        this.target = targetProduct;
        this.replacement = replacement;
    }
    

    public void apply(Individual individual) {
        individual.products().replace(target, replacement);
    }
    
}
