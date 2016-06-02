/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.diversity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.Sample;
import no.sintef.bvr.sampler.Sampler;

public class Population {

    private static final int WORKER_COUNT = Runtime.getRuntime().availableProcessors();
    private static final double BREEDING_FRACTION = 0.10;

    private final static Random random = new Random();

    private final ExecutorService executor;
    private final CompletionService<List<Individual>> tasks;
    private final EvolutionListener listener;
    private final int capacity;
    private final int eliteSize;
    private final List<Individual> individuals;

    public Population(ProductLine productLine, int size, Sampler sampler, EvolutionListener listener) {
        this.executor = Executors.newFixedThreadPool(WORKER_COUNT);
        this.tasks = new ExecutorCompletionService<>(executor);
        this.capacity = size;
        this.eliteSize = (int) (capacity * BREEDING_FRACTION);
        this.individuals = new ArrayList<>(capacity + 2 * eliteSize);
        for (int individual = 0; individual < size; individual++) {
            individuals.add(new Individual(sampler.sample(productLine)));
        }
        this.listener = listener;
    }

    public Sample fittest() {
        assert !individuals.isEmpty() : "Invalid empty population!";
        return individuals.get(0).sample();
    }

    public Sample convergeTo(final Goal goal, int MAX_EPOCH) {
        rank(goal);
        for (int epoch = 0; epoch < MAX_EPOCH; epoch++) {
            listener.epoch(epoch, MAX_EPOCH, individuals.get(0).fitness());
            if (goal.isSatisfiedBy(fittest())) {
                break;
            }
            startBuidlingNextGeneration(tasks, goal);
            collectNextGeneration(tasks);
            Collections.sort(individuals);
            kill();
        }
        wrapUp();
        return fittest();
    }

    private void startBuidlingNextGeneration(CompletionService<List<Individual>> tasks, final Goal goal) {
        for (int index = 0; index < eliteSize; index++) {
            final Individual indidviual = individuals.get(index);
            final Individual partner = selectAnyFrom(individuals);
            tasks.submit(new Callable<List<Individual>>() {
                @Override
                public List<Individual> call() throws Exception {
                    return evolve(indidviual, partner, goal);
                }
            });
        }
    }

    private Individual selectAnyFrom(List<Individual> candidates) {
        assert !candidates.isEmpty() : "Error: Empty population!";
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        int selected = random.nextInt(candidates.size());
        return candidates.get(selected);
    }

    private void collectNextGeneration(CompletionService<List<Individual>> tasks) throws RuntimeException {
        for (int index = 0; index < eliteSize; index++) {
            try {
                List<Individual> children = tasks.take().get();
                individuals.addAll(children);

            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void wrapUp() throws RuntimeException {
        listener.complete();
        executor.shutdown();
        try {
            executor.awaitTermination(1L, TimeUnit.SECONDS);

        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Individual> evolve(Individual individual, Individual partner, Goal goal) {
        final List<Individual> children = individual.mateWith(partner);
        for (Individual eachChild : children) {
            eachChild.mutate();
            eachChild.evaluate(goal);
        }
        return children;
    }

    private void rank(final Goal goal) {
        final CountDownLatch latch = new CountDownLatch(individuals.size());
        for (final Individual eachIndividual : individuals) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    eachIndividual.evaluate(goal);
                    latch.countDown();
                }
            });
        }
        waitAllAt(latch);
        Collections.sort(individuals);
    }

    private void waitAllAt(final CountDownLatch latch) throws RuntimeException {
        try {
            latch.await(1, TimeUnit.SECONDS);

        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);

        }
    }

    private void kill() {
        if (individuals.size() > capacity) {
            int count = individuals.size() - capacity;
            for (int index = 0; index < count; index++) {
                individuals.remove(individuals.size() - 1);
            }
        }
        assert individuals.size() == capacity : "Population: " + individuals.size() + "(expecting: " + capacity + ")";
    }

}
