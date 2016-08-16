
package no.sintef.bvr.sampler.diversity.evolution;

import no.sintef.bvr.sampler.diversity.evolution.Individual;

/**
 * Couple of individual, ready to mate
 */
public class Couple {
    
    private final Individual father;
    private final Individual mother;

    public Couple(Individual father, Individual mother) {
        this.father = father;
        this.mother = mother;
    }

    public Individual father() {
        return father;
    }

    public Individual mother() {
        return mother;
    }
    
    
    
}
