package no.sintef.bvr.sampler.diversity.evolution;

import java.util.Objects;
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

    void evaluateAgainst(Objective objective) {
        fitness = 10 * (1D / (1D + objective.distanceFrom(this)));
    }

    @Override
    public int compareTo(Individual other) {
        return Double.compare(other.fitness, fitness);
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
