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

    @Test
    public void replaceShouldUpdateASingleProduct() {
        ps1.replace(
                create.productFromCode(3),
                create.productFromCode(5));

        assertEquals(ps1, create.productSetFromCodes(1, 2, 5, 4));
    }

    @Test
    public void replaceByAProductThatAlreadyExistShouldReduceTheSizeByOne() {
        int size = ps1.size();
        ps1.replace(
                create.productFromCode(3),
                create.productFromCode(2));

        assertEquals(size - 1, ps1.size());
    }

    @Test
    public void replaceAProductThatDoesNotExistShouldHaveNoEffect() {
        ps1.replace(
                create.productFromCode(6),
                create.productFromCode(8));

        assertEquals(create.productSetFromCodes(1, 2, 3, 4), ps1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addShouldRejectNullProducts() {
        ps1.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void replaceShouldRejectNullAsReplacement() {
        ps1.replace(
                create.productFromCode(2),
                null
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectNullProducts() {
        new ProductSet(create.productFromCode(1), null);
    }

    @Test
    public void addShouldExtendTheProductSet() {
        ps1.add(create.productFromCode(5));

        assertEquals(ps1, create.productSetFromCodes(1, 2, 3, 4, 5));
    }

}
