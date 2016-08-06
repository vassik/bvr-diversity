package no.sintef.bvr.sampler.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductLine;
import no.sintef.bvr.spl.ProductSet;
import no.sintef.bvr.sampler.Sampler;

/**
 * Reservoir sampling
 */
public class RandomSampler implements Sampler {

    private final ProductLine productLine;
    private final Random random;

    public RandomSampler(ProductLine productLine) {
        this(productLine, new Random());
    }

    public RandomSampler(ProductLine productLine, Random random) {
        this.productLine = productLine;
        this.random = random;
    }

    public ProductSet sample(int sampleSize) {
        final List<Product> selectedProducts = new ArrayList<>(sampleSize);

        List<Integer> availableIndexes = allPossibleIndexes();
        for (int i = 0; i < sampleSize; i++) {
            abortIfNoMoreIndexes(availableIndexes, sampleSize);
            int selectedIndex = availableIndexes.remove(random.nextInt(availableIndexes.size()));
            selectedProducts.add(productLine.products().withKey(selectedIndex));
        }
        
        return new ProductSet(selectedProducts);
    }

    private void abortIfNoMoreIndexes(List<Integer> availableIndexes, int sampleSize) throws IllegalArgumentException {
        if (availableIndexes.isEmpty()) {
            final String error
                    = String.format("Cannot choose %d products out of %d!", sampleSize, productLine.products().size());
            throw new IllegalArgumentException(error);
        }
    }

    private List<Integer> allPossibleIndexes() {
        List<Integer> availableIndex = new ArrayList<>(productLine.products().size());
        for(int i=0 ; i<productLine.products().size() ; i++) {
            availableIndex.add(i);
        }
        return availableIndex;
    }

}
