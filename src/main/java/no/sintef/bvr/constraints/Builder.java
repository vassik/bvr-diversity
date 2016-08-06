
package no.sintef.bvr.constraints;

/**
 * Fluent interface to build logical expression
 */
public class Builder {
    
    public static LogicalExpression feature(int index) {
        return new FeatureByIndex(index);
    }
    
    public static LogicalExpression feature(String name) {
        return new FeatureByName(name);
    }
    
    public static LogicalExpression not(LogicalExpression expression) {
        return new Negation(expression);
    }
       
}
