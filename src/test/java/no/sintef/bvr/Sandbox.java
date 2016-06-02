
package no.sintef.bvr;

import org.junit.Test;

/**
 * A single test case to experiment and profile
 */
public class Sandbox {

    @Test
    public void sandbox() {
       Controller controller = new Controller();
       controller.execute(new String[]{"sample_pl.txt", "10000"});
    }

    
    @Test
    public void miniSandbox() {
        System.out.printf("%8.2e", 0D);
    }
}
