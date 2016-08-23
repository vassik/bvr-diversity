package no.sintef.bvr.sampler.diversity;

import java.util.Objects;
import no.sintef.bvr.sampler.diversity.evolution.Individual;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductSet;

/**
 * A individual dedicated for product sets
 */
public class ProductSetIndividual extends Individual {

    private final ProductSet products;

    public ProductSetIndividual(ProductSet products) {
        super();
        this.products = products;
    }

    public ProductSet products() {
        return products;
    }

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
        final ProductSetIndividual other = (ProductSetIndividual) obj;
        return Objects.equals(this.products, other.products);
    }

    @Override
    public String toString() {
        return products.toString() + String.format("[F=%.4f]", fitness());
    }

}
