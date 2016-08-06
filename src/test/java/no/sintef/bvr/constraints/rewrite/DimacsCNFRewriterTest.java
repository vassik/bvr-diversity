package no.sintef.bvr.constraints.rewrite;

import java.util.ArrayList;
import java.util.Collection;
import no.sintef.bvr.constraints.LogicalExpression;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static no.sintef.bvr.constraints.Builder.not;
import static no.sintef.bvr.constraints.Builder.feature;

/**
 * Specification of the Dimacs CNF rewriter
 */
@RunWith(Parameterized.class)
public class DimacsCNFRewriterTest {

    private final String explanation;
    private final LogicalExpression input;
    private final int[][] expectedOutput;

    public DimacsCNFRewriterTest(String explanation, LogicalExpression input, int[][] expectedOutput) {
        this.explanation = explanation;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> examples() {
        Collection<Object[]> examples = new ArrayList<>(20);

        examples.add(new Object[]{
            "f0 => f1",
            feature(0).implies(feature(1)),
            new int[][]{{-1, 2}}
        });

        examples.add(new Object[]{
            "f0 and f1",
            feature(0).and(feature(1)),
            new int[][]{
                {1},
                {2}
            }
        });

        examples.add(new Object[]{
            "f0 or f1",
            feature(0).or(feature(1)),
            new int[][]{
                {1, 2}
            }
        });

        examples.add(new Object[]{
            "f0 & (f1 | f2)",
            feature(0).and(feature(1).or(feature(2))),
            new int[][]{
                {1},
                {2, 3}
            }
        });

        examples.add(new Object[]{
            "f0 & (f1 | ~f2)",
            feature(0).and(feature(1).or(not(feature(2)))),
            new int[][]{
                {1},
                {2, -3}
            }
        });

        examples.add(new Object[]{
            "f0 | (f1 or ~f2)",
            feature(0).or(feature(1).and(not(feature(2)))),
            new int[][]{
                {1, 2},
                {1, -3}
            }
        });

        return examples;
    }

    @Test
    public void integratedTest() {
        int[][] actualOutput = new Rewrite(input).toDimacsCNF();

        verifyDimacsEquality(expectedOutput, actualOutput);
    }

    private void verifyDimacsEquality(int[][] expectedOutput, int[][] actualOutput) {
        assertEquals("Wrong number of clauses!", expectedOutput.length, actualOutput.length);
        for (int i = 0; i < expectedOutput.length; i++) {
            verifyArrayEquality(expectedOutput[i], actualOutput[i]);
        }
    }

    private void verifyArrayEquality(int[] expected, int[] actual) {
        assertEquals("Wrong number of variables", expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals("Wrong variable", expected[i], actual[i]);
        }
    }

}
