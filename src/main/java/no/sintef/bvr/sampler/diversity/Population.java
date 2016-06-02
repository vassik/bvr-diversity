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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import no.sintef.bvr.ProductLine;
import no.sintef.bvr.sampler.Sample;
import no.sintef.bvr.sampler.Sampler;

public class Population {

    //private static final int WORKER_COUNT = Runtime.getRuntime().availableProcessors() - 2;
    private static final int WORKER_COUNT = 4;
    private static final double BREEDING_FRACTION = 0.10;

    private final static Random random = new Random();

    private final ExecutorService executor;
    private final EvolutionListener listener;
    private final int capacity;
    private final int eliteSize;
    private final List<Individual> individuals;

    public Population(ProductLine productLine, int size, Sampler sampler, EvolutionListener listener) {
        this.executor = Executors.newFixedThreadPool(WORKER_COUNT);
        this.capacity = size;
        this.eliteSize = (int) (capacity * BREEDING_FRACTION);
        this.individuals = new ArrayList<>();
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
        for (int epoch = 0; epoch < MAX_EPOCH; epoch++) {
            rank(goal);
            listener.epoch(epoch, MAX_EPOCH, individuals.get(0).fitness());
            if (goal.isSatisfiedBy(fittest())) {
                break;
            }
            kill();
            breed();
            mutate();
        }
        wrapUp();
        return fittest();
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

    private void breed() {
        //final CountDownLatch latch = new CountDownLatch(eliteSize);
        //final ConcurrentLinkedQueue<Individual> allChildren = new ConcurrentLinkedQueue<>();
        final List<Individual> allChildren = new ArrayList<>(eliteSize * 2);
        final List<Individual> remainingParents = new ArrayList<>(individuals);
        for (final Individual eachIndividual : elite()) {
            final Individual partner = takeAnyFrom(remainingParents);
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
            final List<Individual> children = eachIndividual.mateWith(partner);
            allChildren.addAll(children); 
//                    latch.countDown();
//                }
//
//            });
        }
//        waitAllAt(latch);
        individuals.addAll(allChildren);
    }

    private Iterable<Individual> elite() {
        return individuals.subList(0, eliteSize);
    }

    private Individual takeAnyFrom(List<Individual> candidates) {
        assert !candidates.isEmpty() : "Error: Empty population!";
        int selected = random.nextInt(candidates.size());
        return candidates.remove(selected);
    }

    public void mutate() {
        final CountDownLatch latch = new CountDownLatch(individuals.size());
        final ConcurrentLinkedQueue<Individual> mutants = new ConcurrentLinkedQueue<>();
        for (final Individual eachIndividual : individuals) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Individual mutant = eachIndividual.cloneAndMutate();
                    if (mutant != null) {
                        mutants.add(mutant);
                    }
                    latch.countDown();
                }
            });
        }
        waitAllAt(latch);
        individuals.addAll(mutants);
    }

}
