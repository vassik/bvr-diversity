package no.sintef.bvr.experiments;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import no.sintef.bvr.spl.ConstrainedProductLine;
import no.sintef.bvr.spl.EnumeratedProductLine;
import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.ProductLine;
import no.sintef.bvr.spl.ProductSet;
import no.sintef.bvr.metrics.Coverage;
import no.sintef.bvr.metrics.Diversity;
import no.sintef.bvr.sampler.Sampler;
import no.sintef.bvr.sampler.random.RandomSampler;
import no.sintef.bvr.constraints.LogicalExpression;
import no.sintef.bvr.sampler.diversity.DiversitySampler;
import no.sintef.bvr.sampler.diversity.ObjectiveFactory;

/**
 *
 * Compare random sampling with diversity base sampling on several product line,
 * including:
 *
 * - PL1: A product line with few features, and few valid products (highly
 * constrained)
 *
 * - PL2: A product line with many features, but few valid products (highly
 * constrained)
 *
 * - PL3: A product line with few features, but many valid products (no
 * constraints)
 *
 * - PL4: A product line with many features and many valid products (no
 * constraints
 *
 */
public class SamplerComparison {

    public static void main(String args[]) throws FileNotFoundException {
        final FeatureSet features = FeatureSet.fromTemplate(3, "f%d");

        // Small and Open
        ProductLine pl_1
                = new ConstrainedProductLine(
                        features,
                        new ArrayList<LogicalExpression>());

        // Small and closed
        ProductLine pl_2
                = new EnumeratedProductLine(features,
                        new ProductSet(
                                new Product(features, true, true, true),
                                new Product(features, false, true, true),
                                new Product(features, true, false, true),
                                new Product(features, true, true, false))
                );

        // Larger and Open
        ProductLine pl_3
                = new ConstrainedProductLine(
                        FeatureSet.fromTemplate(15, "f%d"),
                        new ArrayList<LogicalExpression>());

        // Larger and Constrained
        FeatureSet feature10 = FeatureSet.fromDefaultTemplate(10);
        ProductLine pl_4
                = new EnumeratedProductLine(feature10,
                        new ProductSet(
                                new Product(feature10, true, true, true, true, true, false, false, false, false, false),
                                new Product(feature10, false, false, false, false, false, true, true, true, true, true),
                                new Product(feature10, true, false, true, false, true, false, true, false, true, false),
                                new Product(feature10, false, true, false, true, false, true, false, true, false, true))
                );

        try (PrintStream output = new PrintStream(new FileOutputStream("results.csv"))) {
            showHeader(output);
            evaluateRandomSampling(pl_1, output, "pl_1");
            evaluateGASampling(pl_1, output, "pl_1");

            evaluateRandomSampling(pl_2, output, "pl_2"); 
            evaluateGASampling(pl_2, output, "pl_2");

            evaluateRandomSampling(pl_3, output, "pl_3");
            evaluateGASampling(pl_3, output, "pl_3");

            evaluateRandomSampling(pl_4, output, "pl_4");
            evaluateGASampling(pl_4, output, "pl_4");
        }

    }

    private static void evaluateRandomSampling(ProductLine productLine, final PrintStream output, final String productLineName) {
        RandomSampler sampler = new RandomSampler(productLine);
        runFor(output, "random", productLineName, productLine, sampler);
    }

    private static void evaluateGASampling(ProductLine productLine, final PrintStream output, final String productLineName) {
        DiversitySampler sampler = new DiversitySampler(productLine, ObjectiveFactory.maximiseDiversityAndCoverage(productLine.features()));
        runFor(output, "ga", productLineName, productLine, sampler);
    }

    private static void showHeader(final PrintStream output) {
        System.out.printf("%10s,%10s,%10s,%10s,%10s,%10s,%10s\r", "pl", "kind", "run", "features", "products", "diversity", "coverage");
        output.printf("%10s,%10s,%10s,%10s,%10s,%10s,%10s\n", "pl", "kind", "run", "features", "products", "diversity", "coverage");
    }

    private static void runFor(PrintStream output, String kind, String productLineName, ProductLine productLine, Sampler sampler) {
        for (int run = 1; run <= MAX_RUN; run++) {
            final Coverage coverage = new Coverage(productLine.features());
            final Diversity diversity = new Diversity();
            ProductSet sample = sampler.sample(3);
            output.printf("%10s,%10s,%10d,%10d,%10d,%10.3f,%10.3f\n", productLineName, kind, run, productLine.features().count(), productLine.products().size(), diversity.of(sample), coverage.of(sample));
            System.out.printf("%10s,%10s,%10d,%10d,%10d,%10.3f,%10.3f\r", productLineName, kind, run, productLine.features().count(), productLine.products().size(), diversity.of(sample), coverage.of(sample));
        }

    }

    private static final int MAX_RUN = 100;

}
