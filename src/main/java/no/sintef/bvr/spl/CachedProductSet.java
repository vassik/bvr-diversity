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
            add(generator.next());
        }
        return super.size(); 
    }    

    @Override
    public Iterator<Product> iterator() {
        return new Iterator<Product>() {

            private final Iterator<Product> cachedProducts = iterator();

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
                    add(newProduct);
                    return newProduct;
                }
                throw new IllegalStateException("There is no more products");
            }

        };
    }

    @Override
    public Product withKey(int key) {
        if (key < super.size()) {
            return withKey(key);
        }
        while (generator.hasNext()) {
            final Product newProduct = generator.next();
            add(newProduct);
            if (key < super.size()) {
                return withKey(key);
            }
        }
        throw new IllegalArgumentException("No product with index " + key + "(only " + super.size() + " products)");
    }

    @Override
    public boolean contains(Product product) {
        if (!super.contains(product)) {
            while (generator.hasNext()) {
                final Product newProduct = generator.next();
                add(newProduct);
            }
        }
        return super.contains(product);
    }

}
