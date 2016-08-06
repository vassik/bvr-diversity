package no.sintef.bvr.spl;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 */
class CachedProductSet extends ProductSet {

    private final Iterator<Product> generator;

    public CachedProductSet(Iterator<Product> productIterator) {
        super(new ArrayList<Product>());
        this.generator = productIterator;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        while (generator.hasNext()) {
            products.add(generator.next());
        }
        return products.size(); 
    }    

    @Override
    public Iterator<Product> iterator() {
        return new Iterator<Product>() {

            private final Iterator<Product> cachedProducts = new ArrayList<>(products).iterator();

            @Override
            public boolean hasNext() {
                return cachedProducts.hasNext() || generator.hasNext();
            }

            @Override
            public Product next() {
                if (cachedProducts.hasNext()) {
                    return cachedProducts.next();
                } else if (generator.hasNext()) {
                    final Product newProduct = generator.next();
                    products.add(newProduct);
                    return newProduct;
                }
                throw new IllegalStateException("There is no more products");
            }

        };
    }

    @Override
    public Product withKey(int key) {
        if (key < products.size()) {
            return products.get(key);
        }
        while (generator.hasNext()) {
            final Product newProduct = generator.next();
            products.add(newProduct);
            if (key < products.size()) {
                return products.get(key);
            }
        }
        throw new IllegalArgumentException("No product with index " + key + "(only " + products.size() + " products)");
    }

    @Override
    public boolean contains(Product product) {
        if (!products.contains(product)) {
            while (generator.hasNext()) {
                final Product newProduct = generator.next();
                products.add(newProduct);
            }
        }
        return products.contains(product);
    }

}
