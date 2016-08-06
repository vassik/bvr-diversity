package no.sintef.bvr.generator;

import static junit.framework.Assert.assertEquals;
import no.sintef.bvr.spl.ConstrainedProductLine;
import no.sintef.bvr.spl.EnumeratedProductLine;
import no.sintef.bvr.constraints.LogicalExpression;
import org.junit.Test;

/**
 *
 */
public class TreeBasedGeneratorTest {

    private final ProductLineGenerator generate;

    public TreeBasedGeneratorTest() {
        generate = new ProductLineGenerator();
    }

    @Test
    public void generateAOneFeatureProductLine() {
        final int featureCount = 1;

        ConstrainedProductLine result = generate.oneProductLine(featureCount);

        assertEquals(featureCount, result.features().count());
        assertEquals(0, result.constraints().size());
    }

    @Test
    public void generateATwoFeatureProductLine() {
        final int featureCount = 2;

        ConstrainedProductLine result = generate.oneProductLine(featureCount);

        show(result);
        
        assertEquals(featureCount, result.features().count());
        assertEquals(1, result.constraints().size());
    }
    
    @Test
    public void bidon() {
        final int featureCount = 16;

        ConstrainedProductLine result = generate.oneProductLine(featureCount);

        show(result);
    }

    private void show(ConstrainedProductLine result) {
        System.out.println("------");
        for(LogicalExpression eachConstraint: result.constraints()) {
            System.out.println(eachConstraint);
        }
    }

}
