package no.sintef.bvr.sampler.diversity.crossover;

import java.util.List;
import java.util.Random;
import no.sintef.bvr.sampler.diversity.ProductSetIndividual;
import no.sintef.bvr.sampler.diversity.evolution.Couple;
import no.sintef.bvr.sampler.diversity.evolution.Crossover;
import no.sintef.bvr.sampler.diversity.evolution.Individual;
import no.sintef.bvr.spl.Factory;
import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.spl.ProductSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Specification of the crossover operator, based on the union and intersection
 * of sets (in the mathematical sense).
 */
public class IntersectionBasedCrossoverTest {

    private static final int FEATURE_COUNT = 4;

    private final Factory create;
    private final Crossover crossover;
    private final ProductSet mother;
    private final ProductSet father;

    public IntersectionBasedCrossoverTest() {
        create = new Factory(FeatureSet.fromDefaultTemplate(FEATURE_COUNT));
        mother = create.productSetFromCodes(3, 4, 5, 6);
        father = create.productSetFromCodes(1, 2, 3, 4);

        Random random = new Random();
        crossover = new SetBasedCrossover(random, create.aProductLineWithProducts(FeatureSet.fromDefaultTemplate(4), 1, 2, 3, 4, 5, 6), 4);

    }

    @Test
    public void childShouldEqualsItsParentsWhenParentsAreSimilar() {
        final ProductSet child = breed(father, father);

        assertEquals(4, child.size());
        assertEquals(father, child);
    }

    @Test
    public void childShouldHaveACorrectLength() {
        final ProductSet child = breed(father, mother);

        assertEquals(4, child.size());
    }

    @Test
    public void childShouldContainsTheIntersectionOfTheirParentsProducts() {
        final ProductSet child = breed(father, mother);

        child.contains(create.productFromCode(3));
        child.contains(create.productFromCode(4));
    }

    private ProductSet breed(ProductSet father, ProductSet mother) {
        ProductSetIndividual dad = new ProductSetIndividual(father);
        ProductSetIndividual mum = new ProductSetIndividual(mother);

        List<Individual> children = crossover.breed(new Couple(dad, mum));

        assertEquals(1, children.size());
        return ((ProductSetIndividual) children.get(0)).products();
    }

}
