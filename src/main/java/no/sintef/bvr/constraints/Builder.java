
package no.sintef.bvr.constraints;

/**
 *
 * @author franckc
 */
public class Builder {
    
    public static FeatureByIndex feature(int index) {
        return new FeatureByIndex(index);
    }
    
    public static Negation not(LogicalExpression expression) {
        return new Negation(expression);
    }
       
}
