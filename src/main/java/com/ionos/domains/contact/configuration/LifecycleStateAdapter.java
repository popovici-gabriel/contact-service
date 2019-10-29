package com.ionos.domains.contact.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class LifecycleStateAdapter extends StateMachineListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleStateAdapter.class);

    @Override
    public void stateChanged(State from, State to) {
        super.stateChanged(from, to);
        if (from == null) {
            LOGGER.info("---------------------");
            LOGGER.info("Transitioned to: [{}]", to.getId());
        } else {
            LOGGER.info("Transitioned from: [{}] to: [{}]", from.getId(), to.getId());
        }
    }
}
