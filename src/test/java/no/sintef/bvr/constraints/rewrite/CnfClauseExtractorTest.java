
package no.sintef.bvr.constraints.rewrite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import no.sintef.bvr.constraints.LogicalExpression;
import static no.sintef.bvr.constraints.Builder.not;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static no.sintef.bvr.constraints.Builder.feature;

/**
 * Specification of the CnfClauseExtractor
 */
@RunWith(Parameterized.class)
public class CnfClauseExtractorTest {

    private final String description;
    private final LogicalExpression input;
    private final List<LogicalExpression> expectedClauses;
    private final boolean failureExpected;

    public CnfClauseExtractorTest(String description, LogicalExpression input, List<LogicalExpression> expectedClauses, boolean failureExpected) {
        this.description = description;
        this.input = input;
        this.expectedClauses = expectedClauses;
        this.failureExpected = failureExpected;
    }

    @Test
    public void doTest() {
        CnfClauseExtractor extractor = new CnfClauseExtractor();

        try {
            List<LogicalExpression> clauses = input.accept(extractor);
            if (failureExpected) {
                fail("Expecting failure!");
            }
            verifyClauses(expectedClauses, clauses);

        } catch (Exception e) {
            if (!failureExpected) {
                throw e;
            }
        }
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> examples() {
        final Collection<Object[]> examples = new ArrayList<>(20);

        examples.add(new Object[]{
            "~f1 | f2",
            not(feature(1)).or(feature(2)),
            Arrays.asList(new LogicalExpression[]{
                not(feature(1)).or(feature(2))
            }),
            MUST_SUCCEED
        });

        examples.add(new Object[]{
            "~f1 & f2",
            not(feature(1)).and(feature(2)),
            Arrays.asList(new LogicalExpression[]{
                not(feature(1)),
                feature(2)
            }),
            MUST_SUCCEED
        });

        examples.add(new Object[]{
            "f1 & f2 & f3",
            feature(1).and(feature(2)).and(feature(3)),
            Arrays.asList(new LogicalExpression[]{
                feature(1),
                feature(2),
                feature(3)
            }),
            MUST_SUCCEED
        });

        examples.add(new Object[]{
            "f1 & (f2 | f3)",
            feature(1).and(feature(2).or(feature(3))),
            Arrays.asList(new LogicalExpression[]{
                feature(1),
                feature(2).or(feature(3))
            }),
            MUST_SUCCEED
        });

        examples.add(new Object[]{
            "f1 => f2",
            feature(1).implies(feature(2)),
            null,
            MUST_RAISE_EXCEPTION
        });

        return examples;
    }
    private static final boolean MUST_SUCCEED = false;
    private static final boolean MUST_RAISE_EXCEPTION = true;

    private void verifyClauses(List<LogicalExpression> expectedClauses, List<LogicalExpression> clauses) {
        assertEquals("Wrong number of clauses", expectedClauses.size(), clauses.size());
        for (int index = 0; index < expectedClauses.size(); index++) {
            assertEquals("Wrong clause", expectedClauses.get(index), clauses.get(index));
        }
    }

}
