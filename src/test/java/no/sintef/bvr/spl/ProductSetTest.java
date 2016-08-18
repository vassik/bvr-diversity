package no.sintef.bvr.spl;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ProductSet specifications
 */
public class ProductSetTest {

    private final Factory create;
    private final ProductSet ps1, ps2;

    public ProductSetTest() {
        create = new Factory(FeatureSet.fromDefaultTemplate(4));
        ps1 = create.productSetFromCodes(1, 2, 3, 4);
        ps2 = create.productSetFromCodes(3, 4, 5, 6);
    }

    @Test
    public void shouldComputeIntersection() {
        final ProductSet union = ps1.intersectionWith(ps2);

        final ProductSet expectation = create.productSetFromCodes(3, 4);
        assertEquals(expectation, union);
    }

    @Test
    public void shouldComputeDifference() {
        final ProductSet difference = ps1.differenceWith(ps2);

        final ProductSet expectation = create.productSetFromCodes(1, 2);
        assertEquals(expectation, difference);
    }

    @Test
    public void shouldBeExtensible() {
        final ProductSet extension = ps1.extendWith(create.productFromCode(5));

        final ProductSet expectation = create.productSetFromCodes(1, 2, 3, 4, 5);
        assertEquals(expectation, extension);
    }

    @Test
    public void extendWithShouldFilterOutDuplicate() {
        final ProductSet extension = ps1.extendWith(create.productFromCode(2));

        final ProductSet expectation = create.productSetFromCodes(1, 2, 3, 4);
        assertEquals(expectation, extension);
    }

}
