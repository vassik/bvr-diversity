package no.sintef.bvr.constraints;

import static no.sintef.bvr.constraints.Builder.feature;
import static no.sintef.bvr.constraints.Builder.not;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Negations behaviour
 */
public class NegationTest {

    private final LogicalExpression negation;
    private final LogicalExpression operand;

    public NegationTest() {
        operand = feature(1);
        negation = not(operand);
    }

    @Test
    public void shouldDisplayProperly() {
        assertEquals("(not " + operand.toString()+")", negation.toString());
    }
    
    @Test
    public void shouldNotEqualNull() {
        assertNotEquals(negation, null);
    }
    
    @Test
    public void shouldEqualsItself() {
        assertEquals(negation, negation);
    }
    
    @Test
    public void shouldNotEqualANegationWithADifferentOperand() {
        assertNotEquals(negation, not(feature(2)));
    }
    
    @Test
    public void shouldHaveTheSameHashCodeThanAnEquivalentNegation() {
        assertEquals(negation.hashCode(), not(feature(1)).hashCode());
    }

    @Test
    public void shouldNotEqualOtherDataType() {
        assertNotEquals(negation, new Object[]{});
    }
    
}
