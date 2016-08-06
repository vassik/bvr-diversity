/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.constraints;

import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.constraints.rewrite.NameResolver;

/**
 * Evaluate expression for a given product and a given set of features
 */
public class Evaluation extends NameResolver<Boolean> {

    private final Product product;

    public Evaluation(FeatureSet features, Product product) {
        super(features);
        this.product = product;
    }

    @Override
    public Boolean onConjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return leftOperand.accept(this) && rightOperand.accept(this);
    }

    @Override
    public Boolean onDisjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return leftOperand.accept(this) || rightOperand.accept(this);
    }

    @Override
    public Boolean onImplication(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        return !leftOperand.accept(this) || rightOperand.accept(this);
    }

    @Override
    public Boolean onNegation(LogicalExpression operand) {
        return !operand.accept(this);
    }

    @Override
    public Boolean onFeatureByIndex(int index) {
        return product.offers(features().withIndex(index));
    }

}
