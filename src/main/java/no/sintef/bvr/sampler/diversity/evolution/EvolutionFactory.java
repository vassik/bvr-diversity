
package no.sintef.bvr.sampler.diversity.evolution;

/**
 * Create individuals
 */
public interface EvolutionFactory {
    
    Population anEmptyPopulation(int capacity);
    
    Individual aRandomIndividual();
    
    Mutation aRandomMutation();
    
    Crossover aRandomCrossover();
    
}
