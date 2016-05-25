/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import no.sintef.bvr.Product;
import no.sintef.bvr.ProductLine;

/**
 *
 * @author franckc
 */
public class Sample implements Iterable<Product> {

    private final ProductLine productLine;
    private final List<Product> products;

    public Sample(ProductLine productLine) {
        this.productLine = productLine;
        this.products = new ArrayList<>();
    }

    public void addProduct(boolean... features) {
        products.add(new Product(productLine, features));
    }

    public void add(Product product) {
        assert productLine == product.productLine() : "Invalid product line!";
        products.add(product);
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

    List<Product> asList() {
        return Collections.unmodifiableList(products);
    }

}
