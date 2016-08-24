package no.sintef.bvr;

import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

/**
 * A single test case to experiment and profile
 */
public class Sandbox {


    @Test
    public void testSat4j() throws TimeoutException, ContradictionException {
    
        ISolver solver = SolverFactory.newDefault();
        
        solver.newVar(4);
        solver.setExpectedNumberOfClauses(1);
        solver.addClause(new VecInt(new int[]{1, -1}));
        
        assertTrue(solver.isSatisfiable());
        
        System.out.println(Arrays.toString(solver.model()));
    
    }


}
