
package no.sintef.bvr.metrics;

/**
 *
 */
public interface Metric<T> {

    double of(T object);
    
}
