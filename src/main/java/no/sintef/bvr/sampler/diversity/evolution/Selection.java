
package no.sintef.bvr.sampler.diversity.evolution;

import java.util.Random;

/**
 * Decide how couples are formed from a given population
 */
public interface Selection {
    
    Iterable<Couple> select(Random random, Population population);
    
}
