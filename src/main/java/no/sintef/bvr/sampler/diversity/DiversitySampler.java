package no.sintef.bvr.sampler.diversity;

import no.sintef.bvr.sampler.diversity.evolution.EvolutionListener;
import no.sintef.bvr.sampler.diversity.evolution.Evolution;
import java.util.Random;
import no.sintef.bvr.spl.ProductLine;
import no.sintef.bvr.spl.ProductSet;
import no.sintef.bvr.sampler.Sampler;
import no.sintef.bvr.sampler.diversity.evolution.Individual;
import no.sintef.bvr.sampler.diversity.evolution.Objective;

public class DiversitySampler implements Sampler {

    public static final Objective DEFAULT_DIVERSITY = ObjectiveFactory.maximiseDiversity();
    private static final int DEFAULT_MAXIMUM_GENERATION = 1000;

    private final EvolutionListener listener;
    private final Objective goal;
    private final int maxEpoch;

    private final ProductLine productLine;

    public DiversitySampler(ProductLine productLine) {
        this(productLine, DEFAULT_DIVERSITY, DEFAULT_MAXIMUM_GENERATION);
    }

    public DiversitySampler(ProductLine productLine, Objective objective) {
        this(productLine, objective, DEFAULT_MAXIMUM_GENERATION);
    }

    public DiversitySampler(ProductLine productLine, Objective objective, int maxEpoch) {
        this(productLine, objective, maxEpoch, new EvolutionListener());
    }

    public DiversitySampler(ProductLine productLine, Objective objective, int maxEpoch, EvolutionListener listener) {
        this.productLine = productLine;
        this.goal = objective;
        this.listener = listener;
        this.maxEpoch = maxEpoch;
    }

    @Override
    public ProductSet sample(int productCount) {
        final Evolution evolution = new Evolution(new TheIndividualFactory(new Random(), productLine, productCount), listener);
        final Individual champion = evolution.convergeTo(goal, maxEpoch);
        return ((ProductSetIndividual) champion).products();
    }

}
