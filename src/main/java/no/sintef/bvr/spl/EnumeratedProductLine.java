package no.sintef.bvr.spl;

/**
 * Product Line built from a set of products;
 */
public class EnumeratedProductLine extends ProductLine {

    private final ProductSet validProducts;
    
    public EnumeratedProductLine(FeatureSet features, ProductSet validProducts) {
        super(features);
        validProducts(validProducts);
        this.validProducts = validProducts;
    }

    private void validProducts(ProductSet products) throws IllegalArgumentException {
        for (Product anyProduct : products) {
            if (!anyProduct.satisfies(features())) {
                final String error = String.format(INVALID_PRODUCT, features(), anyProduct);
                throw new IllegalArgumentException(error);
            }
        }
    }

    private static final String INVALID_PRODUCT = "Invalid product's features (expected '%s', found '%s')";

    @Override
    public boolean contains(Product product) {
        return validProducts.contains(product);
    }

    @Override
    public ProductSet products() {
        return validProducts;
    }

}
