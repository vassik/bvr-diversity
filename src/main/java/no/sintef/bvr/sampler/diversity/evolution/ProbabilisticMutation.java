package no.sintef.bvr.sampler.diversity.evolution;

import java.util.Random;

/**
 * Turn a mutation into a probabilistic one, that is one that occurs with a
 * given probability.
 */
public class ProbabilisticMutation implements Mutation {

    private final Random random;
    private final Mutation mutation;
    private final double probability;
    
    private ProbabilisticMutation(Mutation mutation, double probability) {
        this(new Random(), mutation, probability);
    }

    public ProbabilisticMutation(Random random, Mutation mutation, double probability) {
        this.probability = checkProbability(probability);
        this.random = random;
        this.mutation = mutation;
    }

    private static double checkProbability(double value) {
        if (value < 0D || value > 1D) {
            final String message = String.format(INVALID_PROBABILITY_VALUE, value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }
    
    public static final String INVALID_PROBABILITY_VALUE = "Value %.3f out of probability range [0, 1]!";

    @Override
    public void apply(Individual individual) {
        if (mutationOccurs()) {
            mutation.apply(individual);
        }
    }

    private boolean mutationOccurs() {
        return random.nextDouble() < probability;
    }

}
