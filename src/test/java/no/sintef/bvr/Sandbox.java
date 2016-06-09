package no.sintef.bvr;

import org.junit.Test;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

/**
 * A single test case to experiment and profile
 */
public class Sandbox {

    @Test
    public void sandbox() {
        Controller controller = new Controller();
        controller.execute(new String[]{"gen_pl_20.txt", "5", "10000"});
    }

    @Test
    public void bdd() throws ContradictionException, TimeoutException {
        final int MAXVAR = 2;
        final int NBCLAUSES = 3;

        ISolver solver = SolverFactory.newDefault();

        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(NBCLAUSES);
        solver.addClause(new VecInt(new int[]{1, -2}));
        solver.addClause(new VecInt(new int[]{3}));

        ModelIterator problem = new ModelIterator(solver);;
        if (!problem.isSatisfiable()) {
            System.out.println("No Solution");
            return;
        }
        
        while (problem.isSatisfiable()) {
            System.out.println("Solution Found:");
            int[] model = problem.model();
            for(int index=0 ; index<model.length ; index++) {
                System.out.printf("%d:%d ", index, model[index]);
            }
            System.out.println("");
        } 
    }
}
