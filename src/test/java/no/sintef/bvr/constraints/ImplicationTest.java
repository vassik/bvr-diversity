package no.sintef.bvr.constraints;

import static no.sintef.bvr.constraints.Builder.feature;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Expected behaviour for implications
 */
public class ImplicationTest {

    private final LogicalExpression implication;

    public ImplicationTest() {
        implication = feature("f1").implies(feature("f2"));
    }

    @Test
    public void shouldEqualsTheSameImplication() {
        assertEquals(implication, feature("f1").implies(feature("f2")));
    }

    @Test
    public void shouldNotEqualsNull() {
        assertNotEquals(implication, null);
    }

    @Test
    public void shouldEqualsItself() {
        assertEquals(implication, implication);
    }

    @Test
    public void shouldEqualsImplicationWithADifferentLeftOperand() {
        assertNotEquals(implication, feature("f3").implies(feature("f2")));
    }

    @Test
    public void shouldEqualsImplicationWithADifferentRightOperand() {
        assertNotEquals(implication, feature("f1").implies(feature("f3")));
    }

    @Test
    public void shouldHaveTheSameHashcodethanAnEquivalentDisjunction() {
        assertEquals(implication.hashCode(), feature("f1").implies(feature("f2")).hashCode());
    }

}
