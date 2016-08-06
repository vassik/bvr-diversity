
package no.sintef.bvr.constraints.rewrite;

import java.util.HashMap;
import java.util.Map;
import no.sintef.bvr.constraints.LogicalExpression;
import no.sintef.bvr.constraints.Visitor;

/**
 * Convert a given CNF clause (an AND branch) into the Dimacs format
 */
public class DimacsConverter implements Visitor<Map<Integer, Boolean>> {

    @Override
    public Map<Integer, Boolean> onConjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        throw new IllegalStateException("Not a CNF expression (Conjunction found!)");
    }

    @Override
    public Map<Integer, Boolean> onDisjunction(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        final Map<Integer, Boolean> result = new HashMap<>();
        aggregate(result, leftOperand);
        aggregate(result, rightOperand);
        return result;
    }

    private void aggregate(final Map<Integer, Boolean> result, LogicalExpression expression) throws IllegalStateException {
        final Map<Integer, Boolean> selectedFeature = expression.accept(this);
        for (int anyFeatureIndex : selectedFeature.keySet()) {
            if (result.containsKey(anyFeatureIndex)) {
                throw new IllegalStateException("Not a CNF expression (variable #" + anyFeatureIndex + " is used multiple times)");
            }
            result.put(anyFeatureIndex, selectedFeature.get(anyFeatureIndex));
        }
    }

    @Override
    public Map<Integer, Boolean> onImplication(LogicalExpression leftOperand, LogicalExpression rightOperand) {
        throw new IllegalStateException("Not a CNF expression (Conjunction found!)");
    }

    @Override
    public Map<Integer, Boolean> onNegation(LogicalExpression operand) {
        final Map<Integer, Boolean> result = new HashMap<>();
        final Map<Integer, Boolean> selectedFeature = operand.accept(this);
        for (int anyFeatureIndex : selectedFeature.keySet()) {
            if (result.containsKey(anyFeatureIndex)) {
                throw new IllegalStateException("Not a CNF expression (variable #" + anyFeatureIndex + " is used multiple times)");
            }
            result.put(anyFeatureIndex, !selectedFeature.get(anyFeatureIndex));
        }
        return result;
    }

    @Override
    public Map<Integer, Boolean> onFeatureByIndex(int featureIndex) {
        final Map<Integer, Boolean> result = new HashMap<>();
        result.put(featureIndex, true);
        return result;
    }

    @Override
    public Map<Integer, Boolean> onFeatureByName(String featureName) {
        throw new IllegalStateException("Not a CNF expression (named variable found!)");
    }

}
