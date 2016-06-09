package no.sintef.bvr.sampler.diversity;

import java.util.LinkedList;
import java.util.List;

/**
 * Dispatch events to other listeners
 */
public class EvolutionDispatcher extends EvolutionListener {

    private final List<EvolutionListener> listeners;

    public EvolutionDispatcher() {
        listeners = new LinkedList<>();
    }

    public void register(EvolutionListener newListener) {
        if (!listeners.contains(newListener)) {
            listeners.add(newListener);
        }
    }

    @Override
    public void complete() {
        for (EvolutionListener eachListener : listeners) {
            eachListener.complete();
        }
    }

    @Override
    public void epoch(int epoch, int MAX_EPOCH, double fitness) {
        for (EvolutionListener eachListener : listeners) {
            eachListener.epoch(epoch, MAX_EPOCH, fitness);
        }
    }

}
