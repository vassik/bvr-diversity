/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import no.sintef.bvr.constraints.LogicalExpression;
import no.sintef.bvr.diversity.ProductLineBaseVisitor;
import no.sintef.bvr.diversity.ProductLineParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import static no.sintef.bvr.constraints.Builder.not;
import no.sintef.bvr.constraints.FeatureByName;
import no.sintef.bvr.diversity.ProductLineLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author franckc
 */
public class ProductLineReader {

    public ProductLine from(String text) throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(text.getBytes());
        
        ProductLineLexer lexer = new ProductLineLexer(new ANTLRInputStream(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProductLineParser parser = new ProductLineParser(tokens);
        ParseTree tree = parser.productLine();
        return tree.accept(new ProductLineBuilder());

    }
    
}


class ProductLineBuilder  extends ProductLineBaseVisitor<ProductLine> {

    @Override
    public ProductLine visitProductLine(ProductLineParser.ProductLineContext ctx) {
        List<String> features = ctx.features().accept(new FeatureBuilder());
        List<LogicalExpression> constraints = ctx.constraints().accept(new ConstraintListBuilder());
        
        return new ProductLine(features, constraints); 
    }

}


class FeatureBuilder extends ProductLineBaseVisitor<List<String>> {

    @Override
    public List<String> visitFeatures(ProductLineParser.FeaturesContext ctx) {
        List<String> features = new LinkedList<>();
        for (TerminalNode eachFeature: ctx.ID()) {
            features.add(eachFeature.getText());
        }
        return features;
    }
    
}

class ConstraintListBuilder extends ProductLineBaseVisitor<List<LogicalExpression>> {
   
    @Override
    public List<LogicalExpression> visitConstraints(ProductLineParser.ConstraintsContext ctx) {
        List<LogicalExpression> constraints = new LinkedList<>();
        for(ProductLineParser.ConstraintContext eachConstraint: ctx.constraint()) {
            constraints.add(eachConstraint.accept(new ConstraintBuilder()));
        }
        return constraints;
    }
   
}


class ConstraintBuilder extends ProductLineBaseVisitor<LogicalExpression> {

    @Override
    public LogicalExpression visitFeatureReference(ProductLineParser.FeatureReferenceContext ctx) {
        return new FeatureByName(ctx.ID().getText()); 
    }

    @Override
    public LogicalExpression visitImplication(ProductLineParser.ImplicationContext ctx) {
        final LogicalExpression left = ctx.left.accept(this);
        final LogicalExpression right = ctx.right.accept(this);
        return left.implies(right);
    }

    @Override
    public LogicalExpression visitConjunction(ProductLineParser.ConjunctionContext ctx) {
        final LogicalExpression left = ctx.left.accept(this);
        final LogicalExpression right = ctx.right.accept(this);
        return left.and(right);
    }

    @Override
    public LogicalExpression visitNegation(ProductLineParser.NegationContext ctx) {
        final LogicalExpression operand = ctx.operand.accept(this);
        return not(operand); 
    }

    @Override
    public LogicalExpression visitWithParentheses(ProductLineParser.WithParenthesesContext ctx) {
        return ctx.constraint().accept(this);
    }

    @Override
    public LogicalExpression visitDisjunction(ProductLineParser.DisjunctionContext ctx) {
        final LogicalExpression left = ctx.left.accept(this);
        final LogicalExpression right = ctx.right.accept(this);
        return left.or(right);
    }
    
    
}