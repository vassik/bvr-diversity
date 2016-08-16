package no.sintef.bvr.sampler.diversity.crossover;

import no.sintef.bvr.sampler.diversity.evolution.Crossover;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import no.sintef.bvr.sampler.diversity.evolution.Couple;
import no.sintef.bvr.sampler.diversity.evolution.Individual;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductSet;

/**
 * The single-point crossover.
 *
 * Given a 'cut-point' that splits the chromosome in a left and a right parts,
 * this crossover generates two children, namely left1 + right2 and left2 +
 * right1.
 *
 * For instance breeding [1,2,3,4] with [5,6,7,8], and cut-point of 3 yields
 * [1,2,3|8] and [5,6,7|4].
 */
public class SinglePointCrossover implements Crossover {

    private final int cutPoint;

    public SinglePointCrossover(int cutPoint) {
        this.cutPoint = cutPoint;
    }

    @Override
    public List<Individual> breed(Couple couple) {
        checkCompatibility(couple);
        checkCutPointValidity(couple);

        final List<Product> childA = new LinkedList<>();
        final List<Product> childB = new LinkedList<>();

        final Individual father = couple.father();
        final Individual mother = couple.mother();
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

    private void checkCompatibility(Couple couple) throws IllegalArgumentException {
        if (couple.father().size() != couple.mother().size()) {
            final String message = String.format("Parents have different length! (%d != %d)", couple.father().size(), couple.mother().size());
            throw new IllegalArgumentException(message);
        }
    }

    private void checkCutPointValidity(Couple couple) throws IllegalArgumentException {
        if (cutPoint >= couple.father().size() || cutPoint >= couple.mother().size()) {
            final String message = String.format("Cut-point exceeds parents' length! (%d >= %d)", cutPoint, couple.father().size());
            throw new IllegalArgumentException(message);
        }
    }

}
