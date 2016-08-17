package no.sintef.bvr.spl;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the equality of two product sets
 */
public class ProductSetEqualityTest {

    private final Factory create;

    public ProductSetEqualityTest() {
        create = new Factory(FeatureSet.fromDefaultTemplate(4));
    }

    @Test
    public void twoSimilarProductSetsShouldBeEqual() {
        ProductSet productSet = create.productSetFromCodes(1, 2, 3, 4);
        ProductSet copy = create.productSetFromCodes(1, 2, 3, 4);

        assertEquals(productSet, copy);
    }
    
    @Test
    public void twoProductSetsWhoseProductOrderDifferShouldBeEqual() {
        ProductSet productSet = create.productSetFromCodes(1, 2, 3, 4);
        ProductSet shuffledCopy = create.productSetFromCodes(4, 3, 2, 1);

        assertEquals(productSet, shuffledCopy);
    }

    @Test
    public void twoDifferentProductSetsShouldBeEquals() {
        ProductSet productSet = create.productSetFromCodes(1, 2, 3, 4);
        ProductSet anotherProductSet = create.productSetFromCodes(6, 7, 8, 9);

        assertNotEquals(productSet, anotherProductSet);
    }
    
    @Test
    public void listsShouldDetectWhetherTheyContainAGivenProductSet() {
        List<ProductSet> list = Arrays.asList(new ProductSet[]{
            create.productSetFromCodes(1, 2),
            create.productSetFromCodes(0, 3),
        });
        
        assertTrue(list.contains(create.productSetFromCodes(2, 1)));
    }
}
