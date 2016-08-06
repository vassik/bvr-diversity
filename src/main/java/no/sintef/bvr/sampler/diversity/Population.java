package no.sintef.bvr.sampler.diversity;

import no.sintef.bvr.sampler.diversity.objective.MultiObjective;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductLine;
import no.sintef.bvr.spl.ProductSet;
import no.sintef.bvr.sampler.diversity.mutations.*;

public class Population {

    private final static ThreadLocal<Random> random = new ThreadLocal<>();

    private static final int WORKER_COUNT = Runtime.getRuntime().availableProcessors();
    private static final double BREEDING_FRACTION = 0.5;

    private final ProductLine productLine;
    private final ExecutorService executor;
    private final CompletionService<List<Individual>> tasks;
    private final int sampleSize;
    private final EvolutionListener listener;
    private final int capacity;
    private final int eliteSize;
    private final List<Individual> individuals;

    public Population(ProductLine productLine, int size, int sampleSize, EvolutionListener listener) {
        this.executor = Executors.newFixedThreadPool(WORKER_COUNT);
        this.tasks = new ExecutorCompletionService<>(executor);
        this.productLine = productLine;
        this.capacity = size;
        this.eliteSize = (int) (capacity * BREEDING_FRACTION);
        this.individuals = new ArrayList<>(capacity + 3 * eliteSize);
        this.sampleSize = sampleSize;
        this.listener = listener;
    }

    public ProductSet fittest() {
        assert !individuals.isEmpty() : "Invalid empty population!";
        return individuals.get(0).products();
    }

    private Random random() {
        if (random.get() == null) {
            random.set(new Random());
        }
        return random.get();
    }

    public ProductSet convergeTo(final MultiObjective goal, int MAX_EPOCH) {
        createIndividuals(goal);
        collectNextGeneration(tasks);
        Collections.sort(individuals);
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

    private void createIndividuals(final MultiObjective goal) {
        for (int individual = 0; individual < capacity; individual++) {
            tasks.submit(new Callable<List<Individual>>() {
                @Override
                public List<Individual> call() throws Exception {
                    final Individual newIndividual = spontaneousIndividual();
                    newIndividual.evaluate(goal);
                    return Collections.singletonList(newIndividual);
                }
            });
        }
    }

    private Individual spontaneousIndividual() {
        final List<Product> products = new ArrayList<>(sampleSize);
        for (int i = 0; i < sampleSize; i++) {
            final Product randomProduct = aRandomProduct();
            products.add(randomProduct);
        }
        return new Individual(new ProductSet(products));
    }

    private void startBuidlingNextGeneration(CompletionService<List<Individual>> tasks, final MultiObjective goal) {
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
        int selected = random().nextInt(candidates.size());
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

    public List<Individual> evolve(Individual individual, Individual partner, MultiObjective goal) {
        final List<Individual> children = individual.mateWith(partner, random().nextInt(sampleSize));
        //final List<Individual> children = individual.mateWithV2(partner, random());
        for (Individual eachChild : children) {
            mutate(eachChild);
            eachChild.evaluate(goal);
        }
        if (spontaneousGenerationOccurs()) {
            children.add(spontaneousIndividual());
        }
        return children;
    }

    private void mutate(Individual individual) {
        if (mutationOccurs()) {
            Product target = aRandomProductOf(individual);
            Product replacement = aRandomProduct();
            new SingleProductMutation(target, replacement).apply(individual);
        }
    }

    private Product aRandomProduct() {
        final int randomIndex = random().nextInt(productLine.products().size());
        return productLine.products().withKey(randomIndex);
    }

    private boolean mutationOccurs() {
        double draw = random().nextDouble();
        return draw < MUTATION_PROBABILITY;
    }

    private Product aRandomProductOf(Individual individual) {
        return individual.products().withKey(random().nextInt(sampleSize));
    }

    private static final double MUTATION_PROBABILITY = 0.1;

    private boolean spontaneousGenerationOccurs() {
        return random().nextDouble() < SPONTANEOUS_BIRTH_PROBABILITY;
    }

    private static final double SPONTANEOUS_BIRTH_PROBABILITY = 0.1;

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
