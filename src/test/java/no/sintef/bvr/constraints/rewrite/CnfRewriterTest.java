package no.sintef.bvr.constraints.rewrite;

import no.sintef.bvr.constraints.rewrite.Rewrite;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import no.sintef.bvr.constraints.LogicalExpression;
import static no.sintef.bvr.constraints.Builder.not;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static no.sintef.bvr.constraints.Builder.feature;

@RunWith(Parameterized.class)
public class CnfRewriterTest {

    private final String hint;
    private final LogicalExpression input;
    private final LogicalExpression expectedOutput;

    public CnfRewriterTest(String hint, LogicalExpression input, LogicalExpression expectedOutput) {
        this.hint = hint;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Test
    public void doTest() {
        LogicalExpression cnf = new Rewrite(input).toCNF();
        assertEquals(expectedOutput, cnf);
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> exampleTable() {
        List<Object[]> examples = new LinkedList<>();

        examples.add(new Object[]{
            "A => B >>> ~A | B",
            feature(0).implies(feature(1)),
            not(feature(0)).or(feature(1))
        });

        examples.add(new Object[]{
            "A => (B => C) >>> ~A | (~B |C)",
            feature(0).implies(feature(1).implies(feature(2))),
            not(feature(0)).or(not(feature(1)).or(feature(2)))
        });

        examples.add(new Object[]{
            "~~A >>> A",
            not(not(feature(0))),
            feature(0)
        });

        examples.add(new Object[]{
            "(~A) => B >>> A | B",
            not(feature(0)).implies(feature(1)),
            feature(0).or(feature(1))
        });

        examples.add(new Object[]{
            "~(A | B) >>> (~A & ~B)",
            not(feature(0).or(feature(1))),
            not(feature(0)).and(not(feature(1)))
        });

        examples.add(new Object[]{
            "~(A & B) >>> (~A | ~B)",
            not(feature(0).and(feature(1))),
            not(feature(0)).or(not(feature(1)))
        });

        examples.add(new Object[]{
            "A | (B & C) >>> (A|B) & (A|C)",
            feature(0).or(feature(1).and(feature(2))),
            feature(0).or(feature(1)).and(feature(0).or(feature(2)))
        });

        examples.add(new Object[]{
            " ~((A & B) | (~C | D)) >>> (~A | B) & (c & ~d)",
            not(feature(0).and(feature(1)).or(not(feature(2)).or(feature(3)))),
            not(feature(0)).or(not(feature(1))).and(feature(2).and(not(feature(3))))
        });

        examples.add(new Object[]{
            " ~((A & B) & C)",
            not(feature(0).and(feature(1)).and(feature(2))),
            not(feature(0)).or(not(feature(1))).or(not(feature(2)))
        });

        return examples;
    }

}
