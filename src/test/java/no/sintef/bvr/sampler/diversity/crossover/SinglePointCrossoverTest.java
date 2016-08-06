package no.sintef.bvr.sampler.diversity.crossover;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import no.sintef.bvr.sampler.diversity.Individual;
import no.sintef.bvr.spl.Factory;
import no.sintef.bvr.spl.FeatureSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the single-point crossover
 */
public class SinglePointCrossoverTest {

    private static final int FEATURE_COUNT = 4;
    private static final int PRODUCT_COUNT = 4;
    private static final int CUT_POINT = 2;

    private final Factory factory;
    private final SinglePointCrossover crossover;
    private final Individual father;
    private Individual mother;

    public SinglePointCrossoverTest() {
        factory = new Factory(FeatureSet.fromDefaultTemplate(FEATURE_COUNT));
        crossover = new SinglePointCrossover(CUT_POINT);
        father = anIndividualWithProducts(0, 1, 2, 3);
        mother = anIndividualWithProducts(4, 5, 6, 7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectParentsWhoseSizeFallBehindTheCutPoint() {
        mother = anIndividualWithSize(CUT_POINT - 1);

        crossover.breed(father, mother);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectBreedingParentsWhoseSizeDiffers() {
        mother = anIndividualWithSize(PRODUCT_COUNT + 1);

        crossover.breed(father, mother);
    }

    @Test
    public void theChildrensProductsShouldMatchTheParentsProducts() {
        List<Individual> children = crossover.breed(father, mother);

        Set<Individual> expectedChildren = new HashSet<>();
        expectedChildren.add(anIndividualWithProducts(0, 1, 6, 7));
        expectedChildren.add(anIndividualWithProducts(4, 5, 2, 3));

        assertEquals(expectedChildren, new HashSet<>(children));
    }

    private Individual anIndividualWithSize(int desiredSize) {
        int[] productCodes = new int[desiredSize];
        for (int index = 0; index < desiredSize; index++) {
            productCodes[index] = index;
        }
        return new Individual(factory.productSetFromCodes(productCodes));
    }

    private Individual anIndividualWithProducts(int... productCodes) {
        return new Individual(factory.productSetFromCodes(productCodes));
    }

}
