/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import no.sintef.bvr.Product;
import no.sintef.bvr.ProductLine;

/**
 *
 * @author franckc
 */
public class Sample implements Iterable<Product> {

    private final ProductLine productLine;
    private final List<Product> products;

    public Sample(ProductLine productLine, Product... products) {
        this.productLine = productLine;
        this.products = new ArrayList<>();
        for (Product eachGivenProduct : products) {
            if (eachGivenProduct.belongsTo(productLine)) {
                this.products.add(eachGivenProduct);
            } else {
                throw new IllegalArgumentException("Invalid product line!");
            }
        }
    }

    public Sample duplicate() {
        final Product[] productsCopy = new Product[products.size()];
        for (int index=0 ; index<products.size() ; index++) {
            productsCopy[index] = new Product(products.get(index));
        }
        return new Sample(productLine, productsCopy);
    }

    public void addProduct(boolean... features) {
        products.add(new Product(productLine, features));
    }

    public void add(Product product) {
        assert productLine == product.productLine() : "Invalid product line!";
        products.add(product);
    }

    public Product productAt(int index) {
        return products.get(index);
    }

    public boolean contains(Product product) {
        return products.contains(product);
    }

    @Override
    public Iterator<Product> iterator() {
        return products.iterator();
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public ProductLine productLine() {
        return this.productLine;
    }

    public int size() {
        return products.size();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.productLine);
        hash = 53 * hash + Objects.hashCode(this.products);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sample other = (Sample) obj;
        if (!Objects.equals(this.productLine, other.productLine)) {
            return false;
        }
        if (!Objects.equals(new HashSet<>(this.products), new HashSet<>(other.products))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        for(Product eachProduct: products) {
            buffer.append(eachProduct).append(System.lineSeparator());
        }
        return buffer.toString();
    }

    
}
