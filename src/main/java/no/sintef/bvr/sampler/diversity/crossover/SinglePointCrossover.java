package no.sintef.bvr.sampler.diversity.crossover;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import no.sintef.bvr.sampler.diversity.Individual;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductSet;

/**
 * The single-point crossover.
 *
 * Given a 'cut-point' that split the chromosome in a left and a right parts,
 * this crossover generates two children, namely left1 + right2 and left2 +
 * right1.
 */
public class SinglePointCrossover {

    private final int cutPoint;

    public SinglePointCrossover(int cutPoint) {
        this.cutPoint = cutPoint;
    }

    public List<Individual> breed(Individual father, Individual mother) {
        checkCompatibility(father, mother);
        checkCutPointValidity(father, mother);

        final List<Product> childA = new LinkedList<>();
        final List<Product> childB = new LinkedList<>();

        for (int index = 0; index < father.size(); index++) {
            if (index < cutPoint) {
                childA.add(father.products().withKey(index).copy());
                childB.add(mother.products().withKey(index).copy());
            } else {
                childA.add(mother.products().withKey(index).copy());
                childB.add(father.products().withKey(index).copy());
            }
        }
        final ArrayList<Individual> children = new ArrayList<>();
        children.add(new Individual(new ProductSet(childA)));
        children.add(new Individual(new ProductSet(childB)));
        return children;
    }

    private void checkCompatibility(Individual father, Individual mother) throws IllegalArgumentException {
        if (father.size() != mother.size()) {
            final String message = String.format("Parents have different length! (%d != %d)", father.size(), mother.size());
            throw new IllegalArgumentException(message);
        }
    }

    private void checkCutPointValidity(Individual father, Individual mother) throws IllegalArgumentException {
        if (cutPoint >= father.size() || cutPoint >= mother.size()) {
            final String message = String.format("Cut-point exceeds parents' length! (%d >= %d)", cutPoint, father.size());
            throw new IllegalArgumentException(message);
        }
    }

}
