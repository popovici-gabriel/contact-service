package com.ionos.domains.contact.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class CreateContactAdapter extends StateMachineListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateContactAdapter.class);

    @Override
    public void stateChanged(State from, State to) {
        super.stateChanged(from, to);
        LOGGER.info("Transitioned from: [{}] to: [{}]", from.getId(), to.getId());
    }
}
