
package no.sintef.bvr.constraints;

/**
 *
 * @author franckc
 */
public class Builder {
    
    public static FeatureByIndex feature(int index) {
        return new FeatureByIndex(index);
    }
    
    public static FeatureByName feature(String name) {
        return new FeatureByName(name);
    }
    
    public static Negation not(LogicalExpression expression) {
        return new Negation(expression);
    }
       
}
