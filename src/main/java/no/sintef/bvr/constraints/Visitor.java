
package no.sintef.bvr.constraints;

/**
 * Interface for Traversing logical expression and generating something
 */
public interface Visitor<T> {
  
    T onConjunction(LogicalExpression leftOperand, LogicalExpression rightOperand);
    
    T onDisjunction(LogicalExpression leftOperand, LogicalExpression rightOperand);
    
    T onImplication(LogicalExpression leftOperand, LogicalExpression rightOperand);
    
    T onNegation(LogicalExpression operand);
    
    T onFeatureByIndex(int featureIndex);
    
    T onFeatureByName(String featureName);
   
}
