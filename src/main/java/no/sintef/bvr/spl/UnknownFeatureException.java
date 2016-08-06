package no.sintef.bvr.spl;

/**
 * Raised when accessing a feature that does not exist
 */
public class UnknownFeatureException extends RuntimeException {

    public UnknownFeatureException(String message) {
        super(message);
    }
}
