package no.sintef.bvr.spl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * A set of products
 */
public class ProductSet implements Iterable<Product> {

    private final HashMap<Integer, Product> products;
    private final HashMap<Product, Integer> indexes;

    public ProductSet(Product... products) {
        this(Arrays.asList(products));
    }

    public ProductSet(Collection<Product> products) {
        this.products = new HashMap<>();
        this.indexes = new HashMap<>();
        int index = 0;
        for (Product eachProduct : products) {
            validate(eachProduct);
            this.products.put(index, eachProduct);
            this.indexes.put(eachProduct, index);
            index++;
        }
    }

    private void validate(Product product) throws IllegalArgumentException {
        if (product == null) {
            throw new IllegalArgumentException("Invalid product (found 'null')");
        }
    }

    protected void add(Product product) {
        validate(product);
        final int index = products.size();
        products.put(index, product);
        indexes.put(product, index);
    }

    protected int indexOf(Product product) {
        if (!indexes.containsKey(product)) {
            final String message = String.format("Unknown product '%s'", product.toString());
            throw new IllegalArgumentException(message);
        }
        return indexes.get(product);
    }

    public Product withKey(int key) {
        return products.get(key);
    }

    public int size() {
        return products.size();
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public boolean contains(Product product) {
        return products.containsValue(product);
    }

    @Override
    public Iterator<Product> iterator() {
        return products.values().iterator();
    }

    public void replace(Product target, Product replacement) {
        validate(replacement);
        if (indexes.containsKey(target)) {
            int index = indexOf(target);
            products.remove(index);
            indexes.remove(target);
            if (!indexes.containsKey(replacement)) {
                products.put(index, replacement);
                indexes.put(replacement, index);
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("{");
        for (Product eachProduct : products.values()) {
            buffer.append(eachProduct.code()).append(",");
        }
        buffer.append("}");
        return buffer.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.indexes.keySet());
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
        final ProductSet other = (ProductSet) obj;
        return this.indexes.keySet().equals(other.indexes.keySet());
    }

    public ProductSet intersectionWith(ProductSet other) {
        final Set<Product> products = new HashSet<>();
        for (Product anyLocalProduct : this.products.values()) {
            if (other.contains(anyLocalProduct)) {
                products.add(anyLocalProduct);
            }
        }
        for (Product anyForeignProduct : other.products.values()) {
            if (this.contains(anyForeignProduct)) {
                products.add(anyForeignProduct);
            }
        }
        return new ProductSet(products);
    }

    public ProductSet differenceWith(ProductSet other) {
        final Set<Product> products = new HashSet<>();
        products.addAll(this.products.values());
        products.removeAll(other.products.values());
        return new ProductSet(products);
    }

    public ProductSet extendWith(Product newProduct) {
        final Set<Product> products = new HashSet<>();
        products.addAll(this.products.values());
        products.add(newProduct);
        return new ProductSet(products);
    }

}
