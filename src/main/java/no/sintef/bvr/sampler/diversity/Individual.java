package no.sintef.bvr.sampler.diversity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import no.sintef.bvr.sampler.diversity.objective.Objective;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductSet;

/**
 * Individual being evolved by the genetic algorithm.
 */
public class Individual implements Comparable<Individual> {

    private final ProductSet products;
    private double fitness;

    public Individual(ProductSet products) {
        this.products = products;
        this.fitness = 0;
    }

    public ProductSet products() {
        return products;
    }

    void evaluate(Objective objective) {
        fitness = 10 * (1D / (1D + objective.distanceFrom(products)));
    }

    @Override
    public int compareTo(Individual other) {
        return Double.compare(other.fitness, fitness);
    }

    List<Individual> mateWith(Individual partner, int cutPoint) {
        final List<Product> childA = new LinkedList<>();
        final List<Product> childB = new LinkedList<>();

        for (int index = 0; index < products.size(); index++) {
            if (index < cutPoint) {
                childA.add(products.withKey(index).copy());
                childB.add(partner.products.withKey(index).copy());
            } else {
                childA.add(partner.products.withKey(index).copy());
                childB.add(products.withKey(index).copy());
            }
        }
        final ArrayList<Individual> children = new ArrayList<>();
        children.add(new Individual(new ProductSet(childA)));
        children.add(new Individual(new ProductSet(childB)));
        return children;
    }

    List<Individual> mateWithV2(Individual partner, Random random) {
        final List<Product> childA = new LinkedList<>();
        final List<Product> childB = new LinkedList<>();

        for (int index = 0; index < products.size(); index++) {
            if (random.nextBoolean()) {
                childA.add(products.withKey(index).copy());
                childB.add(partner.products.withKey(index).copy());
            } else {
                childA.add(partner.products.withKey(index).copy());
                childB.add(products.withKey(index).copy());
            }
        }
        final ArrayList<Individual> children = new ArrayList<>();
        children.add(new Individual(new ProductSet(childA)));
        children.add(new Individual(new ProductSet(childB)));
        return children;
    }

    public double fitness() {
        return this.fitness;
    }
    
    //
    
    public int size() {
        return products.size();
    }
    
    
    public boolean contains(Product product) {
        return products.contains(product);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.products);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Individual other = (Individual) obj;
        return Objects.equals(this.products, other.products);
    }
    
    
    @Override
    public String toString() {
        return products.toString();
    }
    

}
