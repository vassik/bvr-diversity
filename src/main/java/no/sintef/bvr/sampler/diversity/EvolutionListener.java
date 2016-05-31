
package no.sintef.bvr.sampler.diversity;

/**
 * Interface to follow-up during the evolution
 */
public class EvolutionListener {

    public void epoch(int epoch, int MAX_EPOCH, double fitness) {
        // By Default, we do nothing
    }
    
}
