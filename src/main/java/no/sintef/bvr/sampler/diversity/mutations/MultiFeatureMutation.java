package no.sintef.bvr.sampler.diversity.mutations;

import java.util.BitSet;
import java.util.Random;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductLine;

/**
 * Try to flip a given number of feature in a product. The mutation may not
 * succeed.
 */
public class MultiFeatureMutation implements ProductMutation {

    private final ProductLine productLine;
    private final Random random;
    private final int strength;

    public MultiFeatureMutation(ProductLine productLine, int strength) {
        this(productLine, strength, new Random());
    }

    public MultiFeatureMutation(ProductLine productLine, int strength, Random random) {
        this.productLine = productLine;
        this.random = random;
        this.strength = strength;
    }

    @Override
    public boolean applyTo(Product product) {
        for (int attempt = 0; attempt < MAXIMUM_ATTEMPT; attempt++) {
            Product mutations = mutate(product);
            if (productLine.contains(product)) {
                return true;
            }
            product.toggle(mutations);
        }
        return false;
    }

    private Product mutate(Product product) {
        Product selectedFeatures = randomFeatures(product);
        product.toggle(selectedFeatures);
        return selectedFeatures;
    }

    private Product randomFeatures(Product product) {
        final byte[] asByteArray = product.asBitSet().toByteArray();
        random.nextBytes(asByteArray);
        BitSet features = BitSet.valueOf(asByteArray);
        Product selectedFeatures = new Product(features);
        return selectedFeatures;
    }

    private static final int MAXIMUM_ATTEMPT = 100;

}
