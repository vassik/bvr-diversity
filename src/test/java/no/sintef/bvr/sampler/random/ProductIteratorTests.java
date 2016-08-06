package no.sintef.bvr.sampler.random;

import no.sintef.bvr.spl.ProductIterator;
import java.util.Iterator;
import no.sintef.bvr.spl.ConstrainedProductLine;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.EnumeratedProductLine;
import static no.sintef.bvr.constraints.Builder.not;
import org.junit.Test;
import static org.junit.Assert.*;
import org.sat4j.specs.ContradictionException;
import static no.sintef.bvr.constraints.Builder.feature;

/**
 *
 */
public class ProductIteratorTests {

    @Test
    public void test2FeaturesWithoutConstraint() throws ContradictionException {
        ConstrainedProductLine productLine = new ConstrainedProductLine(2);
        Iterator<Product> iterator = new ProductIterator(productLine);

        int count = countAllProducts(iterator);

        assertEquals(4, count);
    }

    @Test
    public void test5FeaturesWithoutConstraint() throws ContradictionException {
        ConstrainedProductLine productLine = new ConstrainedProductLine(2);
        Iterator<Product> iterator = new ProductIterator(productLine);

        int count = countAllProducts(iterator);

        assertEquals(32, count);
    }

    private int countAllProducts(Iterator<Product> iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }

    @Test
    public void testWithDisjunction() throws ContradictionException {
        ConstrainedProductLine productLine
                = new ConstrainedProductLine(2, feature(0).or(feature(1)));
        Iterator<Product> iterator = new ProductIterator(productLine);

        int count = countAllProducts(iterator);

        assertEquals(3, count);
    }

    @Test
    public void testWithImplication() throws ContradictionException {
        ConstrainedProductLine productLine
                = new ConstrainedProductLine(2, feature(0).implies(feature(1)));
        Iterator<Product> iterator = new ProductIterator(productLine);

        int count = countAllProducts(iterator);

        assertEquals(3, count);
    }

    @Test
    public void testWithLargeConstraints() throws ContradictionException {
        ConstrainedProductLine productLine 
                = new ConstrainedProductLine(5, not(feature(0).and(feature(1)).and(feature(2))));
        Iterator<Product> iterator = new ProductIterator(productLine);

        int count = countAllProducts(iterator);

        assertEquals(32 - 4, count);
    }
}
