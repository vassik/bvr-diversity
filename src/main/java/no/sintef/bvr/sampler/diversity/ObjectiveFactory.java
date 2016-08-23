package no.sintef.bvr.sampler.diversity;

import no.sintef.bvr.metrics.Coverage;
import no.sintef.bvr.metrics.PairWiseDistanceError;
import no.sintef.bvr.sampler.diversity.DesiredValue;
import no.sintef.bvr.sampler.diversity.evolution.MultiObjective;
import no.sintef.bvr.sampler.diversity.evolution.Objective;
import no.sintef.bvr.spl.FeatureSet;

/**
 * Create predefined objectives
 */
public class ObjectiveFactory {

    public static Objective maximiseDiversity() {
        return new DesiredValue(new PairWiseDistanceError(1), 0);
    }

    public static Objective minimiseDiversity() {
        return new DesiredValue(new PairWiseDistanceError(0), 0);
    }

    public static Objective maximiseCoverage(FeatureSet features) {
        return new DesiredValue(new Coverage(features), 1);
    }

    public static Objective minimiseCoverage(FeatureSet features) {
        return new DesiredValue(new Coverage(features), 0);
    }

    public static Objective maximiseDiversityAndCoverage(FeatureSet features) {
        return new MultiObjective(
                maximiseDiversity(),
                maximiseCoverage(features)
        );
    }

}
