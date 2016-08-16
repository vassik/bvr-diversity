
package no.sintef.bvr.sampler.diversity.evolution;

import java.util.List;

/**
 * Breeding two individuals
 */
public interface Crossover {

    List<Individual> breed(Couple couple);
    
}
