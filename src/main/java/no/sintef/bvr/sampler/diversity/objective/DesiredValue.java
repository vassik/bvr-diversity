
package no.sintef.bvr.sampler.diversity.objective;

import no.sintef.bvr.metrics.Metric;
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
    public double distanceFrom(ProductSet object) {
        return target - metric.of(object);
    }
    
}
