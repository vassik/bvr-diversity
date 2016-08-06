package no.sintef.bvr.spl;



/**
 * Raise when a feature set contains two feature with the same name
 */
public class DuplicatedFeatureException extends RuntimeException {

    public DuplicatedFeatureException(String msg) {
        super(msg);
    }
}
