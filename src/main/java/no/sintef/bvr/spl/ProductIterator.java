package no.sintef.bvr.spl;

import java.util.Iterator;
import no.sintef.bvr.constraints.LogicalExpression;
import no.sintef.bvr.constraints.rewrite.Rewrite;
import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVec;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

/**
 * Iterate over the valid products
 */
public class ProductIterator implements Iterator<Product> {

    private final ConstrainedProductLine productLine;
    private final IProblem problem;

    public ProductIterator(ConstrainedProductLine productLine) {
        try {
            this.productLine = productLine;
            this.problem = convert(productLine);
        } catch (ContradictionException ex) {
            throw new IllegalStateException("empty product line", ex);
        }
    }

    private static IProblem convert(ConstrainedProductLine productLine) throws ContradictionException {
        ISolver solver = SolverFactory.newDefault();

        convertUnconstrainedFeatures(solver, productLine);
        convertConstraints(solver, productLine);
        return new ModelIterator(solver);
    }

    private static void convertUnconstrainedFeatures(ISolver solver, ConstrainedProductLine productLine) {
        int index = 1;
        for (Feature eachUnconstrainedFeature : productLine.unconstrainedFeatures()) {
            solver.registerLiteral(index);
            index++;
        }
    }

    private static void convertConstraints(ISolver solver, ConstrainedProductLine productLine) throws ContradictionException {
        final IVec clauses = new Vec();

        for (LogicalExpression eachConstraint : productLine.constraints()) { 
            int[][] subClauses = new Rewrite(eachConstraint).toDimacsCNF();
            for (int[] eachSubClause : subClauses) { 
                clauses.push(new VecInt(eachSubClause));
            }
        }

        solver.addAllClauses(clauses); 
    }

    @Override
    public boolean hasNext() {
        try {
            return problem.isSatisfiable();

        } catch (TimeoutException ex) {
            throw new IllegalStateException("timeout", ex);
        }
    }

    @Override
    public Product next() {
        if (!hasNext()) {
            throw new IllegalStateException("There is no next product");
        }
        return toProduct(problem.model());
    }

    private Product toProduct(int[] model) {
        final Product product = new Product(productLine.features());
        for (int eachVariableIndex : model) {
            final int shiftedIndex = Math.abs(eachVariableIndex) - 1;
            product.set(productLine.features().withIndex(shiftedIndex), eachVariableIndex > 0);
        }
        return product;
    }

}
