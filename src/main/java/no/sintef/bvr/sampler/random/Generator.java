
package no.sintef.bvr.sampler.random;

import java.util.Iterator;
import no.sintef.bvr.spl.Feature;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductLine;

public abstract class Generator implements Iterator<Product> {

    public static Generator createFor(ProductLine productLine) {
        Product template = new Product(productLine.features());
        Generator generator = new EmptyGenerator(template);
        for (Feature eachFeature : productLine.features()) {
            generator = new FeatureGenerator(generator, template, eachFeature);
        }
        return generator;
    }
    
    protected final Product product;

    public Generator(Product product) {
        this.product = product;
    }

    public abstract boolean hasNext();

    public abstract Product next();

    public abstract void reset();

}

class FeatureGenerator extends Generator {

    private static final int OPTIONS = 2;

    private final Generator delegate;
    private final Feature feature;
    private final Boolean value;
    private int alternatives;

    public FeatureGenerator(Generator delegate, Product product, Feature feature) {
        super(product);
        this.delegate = delegate;
        this.feature = feature;
        this.alternatives = OPTIONS;
        this.value = false;
        product.set(feature, value);
    }

    @Override
    public boolean hasNext() {
        return  alternatives > 1 || delegate.hasNext();
    }

    @Override
    public Product next() {
        if (delegate.hasNext()) {
            return delegate.next();
        } else if (alternatives > 0) {
            setUp();
            return delegate.next();
        } else {
            throw new IllegalStateException("No more alternative (feature '" + feature + "')");
        }
    }

    private void setUp() {
        alternatives--;
        product.set(feature, !value);
        delegate.reset();
    }

    @Override
    public void reset() {
        alternatives = OPTIONS;
        product.set(feature, value);
        delegate.reset();
    }

}

class EmptyGenerator extends Generator {

    private boolean done;

    public EmptyGenerator(Product product) {
        super(product);
        done = false;
    }

    @Override
    public boolean hasNext() {
        return !done;
    }

    @Override
    public Product next() {
        done = true;
        return product.copy();

    }

    @Override
    public void reset() {
        done = false;
    }

}
