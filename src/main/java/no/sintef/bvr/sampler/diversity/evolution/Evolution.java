package no.sintef.bvr.sampler.diversity.evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Evolution {

    private static final double SPONTANEOUS_BIRTH_PROBABILITY = 0.1;
    private static final double BREEDING_RATIO = 0.25;

    private final int size = 100;
    private final int coupleCount;
    private final int childrenCount;

    private final EvolutionFactory create;
    private final EvolutionListener listener;

    public Evolution(EvolutionFactory create, EvolutionListener listener) {
        coupleCount = (int) Math.round(BREEDING_RATIO * size);
        childrenCount = 3;
        this.listener = listener;
        this.create = create;
    }

    public Individual convergeTo(final Objective objective, int MAX_EPOCH) {
        Population population = initialisePopulation(objective);
        population.rank();
        for (int epoch = 0; epoch < MAX_EPOCH; epoch++) {
            final Individual champion = population.fittest();
            listener.epoch(epoch, MAX_EPOCH, champion.fitness());
            if (objective.isSatisfiedBy(champion)) {
                break;
            }
            breed(population, objective);
            population.rank();
            population.spare(size);
        }
        return population.fittest();
    }

    private Population initialisePopulation(Objective objective) {
        final Population population = create.anEmptyPopulation(size + childrenCount * coupleCount);
        for (int i = 0; i < size; i++) {
            final Individual individual = create.aRandomIndividual();
            individual.evaluateAgainst(objective);
            population.add(individual);
        }
        return population;
    }

    private void breed(Population population, final Objective objective) throws RuntimeException {
        for (Couple eachCouple : population.pairUp()) {
            for (Individual eachChild : breed(eachCouple)) {
                mutate(eachChild);
                eachChild.evaluateAgainst(objective);
                population.add(eachChild);
            }
            for (Individual eachOrphan : spontaneousGeneration()) {
                eachOrphan.evaluateAgainst(objective);
                population.add(eachOrphan);
            }
        }
    }

    private List<Individual> breed(Couple couple) {
        return create.aRandomCrossover().breed(couple);
    }

    private void mutate(Individual eachChild) {
        create.aRandomMutation().apply(eachChild);
    }

    private List<Individual> spontaneousGeneration() {
        final List<Individual> orphans = new ArrayList<>();
        if (spontaneousGenerationOccurs()) {
            orphans.add(create.aRandomIndividual());
        }
        return orphans;
    }

    private boolean spontaneousGenerationOccurs() {
        return new Random().nextDouble() < SPONTANEOUS_BIRTH_PROBABILITY;
    }

}
