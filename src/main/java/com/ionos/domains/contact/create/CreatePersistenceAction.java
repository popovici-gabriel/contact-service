package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class CreatePersistenceAction {

    void persistenceSuccess(StateContext<CreateContactState, CreateContactEvent> context) {
        // @formatter:off
        context
                .getExtendedState()
                .getVariables()
                .putIfAbsent("continue", true);
        // @formatter:on
    }

    void persistenceError(StateContext<CreateContactState, CreateContactEvent> context) {
        // @formatter:off
        context
                .getExtendedState()
                .getVariables()
                .putIfAbsent("continue", false);
        // @formatter:on
    }
}
