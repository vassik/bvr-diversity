package no.sintef.bvr.constraints.rewrite;

import java.util.ArrayList;
import java.util.Collection;
import no.sintef.bvr.constraints.LogicalExpression;
import static no.sintef.bvr.constraints.Builder.feature;
import static no.sintef.bvr.constraints.Builder.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Specification of the dimacs converter
 */
@RunWith(Parameterized.class)
public class DimacsConverterTests {

    private final String description;
    private final int[] expectedClause;
    private final LogicalExpression input;
    private final boolean mustRaiseException;

    public DimacsConverterTests(String description, LogicalExpression input, int[] expectedClause, boolean mustRaiseException) {
        this.description = description;
        this.input = input;
        this.expectedClause = expectedClause;
        this.mustRaiseException = mustRaiseException;
    }

    @Test
    public void test() {
        try {
            int[] actualClause = rewrite(input).toDimacsClause();
            if (mustRaiseException) {
                fail("Should have raised an exception!");
            }
            verify(actualClause);

        } catch (Exception e) {
            if (!mustRaiseException) {
                fail("Should NOT have raised an exception!");
            }
        }
    }

    private static Rewrite rewrite(LogicalExpression input) {
        return new Rewrite(input);
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> examples() {
        Collection<Object[]> examples = new ArrayList<>();

        examples.add(new Object[]{
            "f1",
            feature(1),
            new int[]{2},
            MUST_SUCCEED
        });

        examples.add(new Object[]{
            "~f1",
            not(feature(1)),
            new int[]{-2},
            MUST_SUCCEED
        });

        examples.add(new Object[]{
            "f1 | f2",
            feature(1).or(feature(2)),
            new int[]{2, 3},
            MUST_SUCCEED
        });

        examples.add(new Object[]{
            "f1 | ~f2",
            feature(1).or(not(feature(2))),
            new int[]{2, -3},
            MUST_SUCCEED
        });

        examples.add(new Object[]{
            "f1 | ~f2 | f3",
            feature(1).or(not(feature(2))).or(feature(3)),
            new int[]{2, -3, 4},
            MUST_SUCCEED
        });

        examples.add(new Object[]{
            "fX",
            feature("X"),
            null,
            MUST_RAISE_EXCEPTION
        });

        examples.add(new Object[]{
            "f1 | f1",
            feature(1).or(not(feature(1))),
            null,
            MUST_RAISE_EXCEPTION
        });

        examples.add(new Object[]{
            "f1 & f2",
            feature(1).and(feature(1)),
            null,
            MUST_RAISE_EXCEPTION
        });

        examples.add(new Object[]{
            "f1 => f2",
            feature(1).implies(feature(1)),
            null,
            MUST_RAISE_EXCEPTION
        });

        return examples;
    }

    private static final boolean MUST_SUCCEED = false;
    private static final boolean MUST_RAISE_EXCEPTION = true;

    private void verify(int[] actualClause) {
        assertEquals("Wrong number of variables", expectedClause.length, actualClause.length);
        for (int index = 0; index < expectedClause.length; index++) {
            assertEquals("Wrong variable", expectedClause[index], actualClause[index]);
        }
    }

}
