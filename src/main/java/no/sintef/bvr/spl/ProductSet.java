package no.sintef.bvr.spl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A set of products
 */
public class ProductSet implements Iterable<Product> {

    protected final List<Product> products;

    public ProductSet(Product... products) {
        this(Arrays.asList(products)); 
    } 
    
    public ProductSet(Collection<Product> products) {
        this.products = new ArrayList<>(products);
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
        return products.contains(product);
    }

    @Override
    public Iterator<Product> iterator() {
        return products.iterator();
    }

    public void replace(Product target, Product replacement) {
        products.remove(target);
        products.add(replacement);
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("(");
        for(Product eachProduct: products) {
            buffer.append(eachProduct.code()).append(",");
        }
        buffer.append(")");
        return buffer.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.products);
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
        return Objects.equals(new HashSet<>(this.products), new HashSet<>(other.products));
    }
    
    
}
