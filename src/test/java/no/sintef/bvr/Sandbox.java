/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr;

import org.junit.Test;

/**
 *
 * @author franckc
 */
public class Sandbox {

    @Test
    public void sandbox() {
       Controller controller = new Controller();
       controller.execute(new String[]{"sample_pl.txt", "10000"});
    }

}
