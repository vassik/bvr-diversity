package no.sintef.bvr.constraints;

import static no.sintef.bvr.constraints.Builder.feature;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Behaviour of conjunction
 */
public class ConjunctionTest {

    private final LogicalExpression conjunction;

    public ConjunctionTest() {
        conjunction = feature("f1").and(feature("f2"));
    }

    @Test
    public void shouldEqualsTheSameDisjunction() {
        assertEquals(conjunction, feature("f1").and(feature("f2")));
    }

    @Test
    public void shouldNotEqualsNull() {
        assertNotEquals(conjunction, null);
    }

    @Test
    public void shouldEqualsItself() {
        assertEquals(conjunction, conjunction);
    }

    @Test
    public void shouldEqualsConjunctionWithADifferentLeftOperand() {
        assertNotEquals(conjunction, feature("f3").and(feature("f2")));
    }

    @Test
    public void shouldEqualsConjunctionWithADifferentRightOperand() {
        assertNotEquals(conjunction, feature("f1").and(feature("f3")));
    }

    @Test
    public void shouldHaveTheSameHashcodethanAnEquivalentDisjunction() {
        assertEquals(conjunction.hashCode(), feature("f1").and(feature("f2")).hashCode());
    }
}
