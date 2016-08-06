package no.sintef.bvr.spl;

import java.util.Iterator;

/**
 * Product Line as the set of products whose features are similar
 */ 
public abstract class ProductLine implements Iterable<Product> {

    private final FeatureSet features;
      
    public ProductLine(FeatureSet features) {
        this.features = features;
    }

    public FeatureSet features() { 
        return this.features;
    }
 
    public abstract boolean contains(Product product);

    public abstract ProductSet products();

    @Override
    public Iterator<Product> iterator() {
        return products().iterator();
    }

}
