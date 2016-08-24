package no.sintef.bvr.constraints.rewrite;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Abstract behaviour visitor
 */
public class AbstractVisitorTest {

    private final AbstractVisitor<String> visitor;

    public AbstractVisitorTest() {
        visitor = new AbstractVisitor<>();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectOnConjunction() {
        String s = visitor.onConjunction(null, null);
        assertNull(s);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectOnDisjunction() {
        visitor.onDisjunction(null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectOnImplication() {
        visitor.onImplication(null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectOnNegation() {
        visitor.onNegation(null);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectOnFeatureByIndex() {
        visitor.onFeatureByIndex(1);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectOnFeatureByName() {
        visitor.onFeatureByName("foo");
    }

}
