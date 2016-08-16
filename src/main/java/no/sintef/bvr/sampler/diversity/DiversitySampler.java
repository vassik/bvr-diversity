
package no.sintef.bvr.sampler.diversity;

import no.sintef.bvr.sampler.diversity.evolution.EvolutionListener;
import no.sintef.bvr.sampler.diversity.evolution.Evolution;
import java.util.Random;
import no.sintef.bvr.sampler.diversity.evolution.MultiObjective;
import no.sintef.bvr.metrics.Coverage;
import no.sintef.bvr.metrics.PairWiseDistanceError;
import no.sintef.bvr.spl.ProductLine;
import no.sintef.bvr.spl.ProductSet;
import no.sintef.bvr.sampler.Sampler;

public class DiversitySampler implements Sampler {

    public static final double DEFAULT_DIVERSITY = 1;
    private static final int POPULATION_SIZE = 200;
    private static final int MAX_EPOCH = 1000;

    private final EvolutionListener listener;
    private final MultiObjective goal;
    private final int maxEpoch;

    private final ProductLine productLine;
    
    public DiversitySampler(ProductLine productLine) {
        this(productLine, DEFAULT_DIVERSITY, MAX_EPOCH);
    }
    
    public DiversitySampler(ProductLine productLine, double diversity) {
        this(productLine, diversity, MAX_EPOCH);
    }

    public DiversitySampler(ProductLine productLine, double diversity, int maxEpoch) {
        this(productLine, diversity, maxEpoch, new EvolutionListener());
    }
    
    public DiversitySampler(ProductLine productLine, double diversity, int maxEpoch, EvolutionListener listener) {
        this.productLine = productLine;
        this.goal = new MultiObjective(
                new DesiredValue(new Coverage(productLine.features()), 1), 
                new DesiredValue(new PairWiseDistanceError(diversity), 0));
        this.listener = listener;
        this.maxEpoch = maxEpoch;
    }

    @Override
    public ProductSet sample(int productCount) {
        final Evolution evolution = new Evolution(new TheIndividualFactory(new Random(), productLine, productCount), listener);
        return evolution.convergeTo(goal, maxEpoch).products();
    }

}



