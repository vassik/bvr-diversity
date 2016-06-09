/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.sintef.bvr.sampler.diversity;

import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.Matchers;

/**
 * Specification of the EvolutionDispatcher
 */
public class EvolutionDispatcherTests {

    private final EvolutionDispatcher dispatcher;

    public EvolutionDispatcherTests() {
        dispatcher = new EvolutionDispatcher();
    }

    @Test
    public void shouldDispatchOnceDespiteMultipleRegistration() {
        EvolutionListener fakeListener = mock(EvolutionListener.class);

        for (int i = 0; i < 10; i++) {
            dispatcher.register(fakeListener);
        }

        sendEvents();

        verifyReception(fakeListener);

    }

    private void verifyReception(EvolutionListener fakeListener) {
        verify(fakeListener, times(1)).epoch(Matchers.eq(1), Matchers.eq(10000), Matchers.eq(0.5));
        verify(fakeListener, times(1)).complete();
    }

    private void sendEvents() {
        dispatcher.epoch(1, 10000, 0.5);
        dispatcher.complete();
    }

    @Test
    public void shouldDispatchToAllListeners() {
        EvolutionListener listener1 = mock(EvolutionListener.class);
        EvolutionListener listener2 = mock(EvolutionListener.class);

        dispatcher.register(listener1);
        dispatcher.register(listener2);

        sendEvents();

        verifyReception(listener1);
        verifyReception(listener2);
    }

}
