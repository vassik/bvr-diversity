package no.sintef.bvr.spl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import no.sintef.bvr.constraints.Evaluation;
import no.sintef.bvr.constraints.LogicalExpression;
import no.sintef.bvr.constraints.rewrite.UnconstrainedFeatureCollector;
import no.sintef.bvr.sampler.random.Generator;

/**
 *
 */
public class ConstrainedProductLine extends ProductLine {

    private final List<LogicalExpression> constraints;
    private ProductSet products;

    public ConstrainedProductLine(int featureCount) {
        this(FeatureSet.fromTemplate(featureCount, "f%d"), new ArrayList<LogicalExpression>(0));
    }

    public ConstrainedProductLine(int featureCount, LogicalExpression... constraints) {
        this(FeatureSet.fromTemplate(featureCount, "f%d"), Arrays.asList(constraints));
    }

    public ConstrainedProductLine(FeatureSet features, LogicalExpression... constraints) {
        this(features, Arrays.asList(constraints));
    }

    public ConstrainedProductLine(FeatureSet features, List<LogicalExpression> constraints) {
        super(features);
        this.constraints = constraints;
        computeProductSet();
    }

    private void computeProductSet() {
        List<Product> allProducts = new ArrayList<>();
        Iterator<Product> iterator = Generator.createFor(this);
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (contains(product)) {
                allProducts.add(product);
            }
        }
        this.products = new ProductSet(allProducts);
    }

    public List<LogicalExpression> constraints() {
        return constraints;
    }

    @Override
    public boolean contains(Product product) {
        for (LogicalExpression anyConstraint : constraints) {
            if (isViolated(anyConstraint, product)) {
                return false;
            }
        }
        return true;
    }

    private boolean isViolated(LogicalExpression anyConstraint, Product product) {
        return !anyConstraint.accept(new Evaluation(features(), product));
    }

    @Override
    public ProductSet products() {
        if (products == null) {
            computeProductSet();
        }
        return products;
    }

    Iterable<Feature> unconstrainedFeatures() {
        UnconstrainedFeatureCollector collector = new UnconstrainedFeatureCollector(features());
        for (LogicalExpression eachConstraint : constraints) {
            eachConstraint.accept(collector);
        }
        return collector.result();
    }

}
