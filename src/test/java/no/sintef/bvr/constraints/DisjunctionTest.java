package no.sintef.bvr.constraints;

import static no.sintef.bvr.constraints.Builder.feature;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Disjunction behaviour
 */
public class DisjunctionTest {

    private final LogicalExpression disjunction;

    public DisjunctionTest() {
        disjunction = feature("f1").or(feature("f2"));
    }

    @Test
    public void shouldEqualsTheSameDisjunction() {
        assertEquals(disjunction, feature("f1").or(feature("f2")));
    }

    @Test
    public void shouldNotEqualsNull() {
        assertNotEquals(disjunction, null);
    }

    @Test
    public void shouldEqualsItself() {
        assertEquals(disjunction, disjunction);
    }

    @Test
    public void shouldEqualsDisjunctionnWithADifferentLeftOperand() {
        assertNotEquals(disjunction, feature("f3").or(feature("f2")));
    }

    @Test
    public void shouldEqualsDisjunctionWithADifferentRightOperand() {
        assertNotEquals(disjunction, feature("f1").or(feature("f3")));
    }

    @Test
    public void shouldHaveTheSameHashcodethanAnEquivalentDisjunction() {
        assertEquals(disjunction.hashCode(), feature("f1").or(feature("f2")).hashCode());
    }

}
