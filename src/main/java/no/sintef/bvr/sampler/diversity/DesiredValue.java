
package no.sintef.bvr.sampler.diversity;

import no.sintef.bvr.sampler.diversity.evolution.Objective;
import no.sintef.bvr.metrics.Metric;
import no.sintef.bvr.sampler.diversity.evolution.Individual;
import no.sintef.bvr.spl.ProductSet;

/**
 * Associate a given target value and a metric
 */
public class DesiredValue extends Objective {

    private final Metric<ProductSet> metric;
    private final double target;

    public DesiredValue(Metric<ProductSet> metric, double target) {
        this.metric = metric;
        this.target = target;
    }
    
    @Override
    public double distanceFrom(Individual individual) {
        ProductSetIndividual productSet = (ProductSetIndividual) individual;
        return target - metric.of(productSet.products());
    }
    
}
