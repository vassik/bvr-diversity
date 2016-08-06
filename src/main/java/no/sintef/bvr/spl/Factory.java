package no.sintef.bvr.spl;

import java.util.Random;

/**
 * Help with the creation of product-lines, product sets, and products
 */
public class Factory {

    private final FeatureSet features;

    public Factory(FeatureSet features) {
        this.features = features;
    }

    public ProductSet createProductSet(boolean[][] isActive) {
        final Product[] products = new Product[isActive.length];
        for (int index = 0; index < isActive.length; index++) {
            products[index] = aProductWith(isActive[index]);
        }
        return new ProductSet(products);
    }

    public ProductSet productSetFromCodes(int... codes) {
        final Product[] products = new Product[codes.length];
        for (int index = 0; index < codes.length; index++) {
            products[index] = productFromCode(codes[index]);
        }
        return new ProductSet(products);
    }

    public Product anEmptyProduct() {
        return aProductWith(new boolean[features.count()]);
    }

    public Product aFullProduct() {
        boolean[] isActive = new boolean[features.count()];
        for (int index = 0; index < features.count(); index++) {
            isActive[index] = true;
        }
        return aProductWith(isActive);
    }

    public Product aRandomProduct() {
        final Random random = new Random();
        final boolean[] isActive = new boolean[features.count()];
        for (int index = 0; index < features.count(); index++) {
            isActive[index] = random.nextBoolean();
        }
        return aProductWith(isActive);
    }

    public Product aProductWith(boolean... isActive) {
        return new Product(features, isActive);
    }

    public Product productFromCode(long code) {
        if (code < 0) {
            throw new IllegalArgumentException("Negative product codes are not allowed!");
        }

        final boolean[] activeFeatures = new boolean[features.count()];
        for (Feature eachFeature : features) {
            final long index = features.indexOf(eachFeature);
            final long mask = 0x01 << index;
            activeFeatures[(int) index] = (code & mask) == mask;
        }

        return aProductWith(activeFeatures);
    }

}
