package com.ionos.domains.contact.update;

import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class UpdateContactAction {

    void update(StateContext<UpdateContactState, UpdateContactEvent> context) {
        addContinueVariable(context);
    }

    private void addContinueVariable(StateContext<UpdateContactState, UpdateContactEvent> context) {
        // @formatter:off
        context
                .getExtendedState()
                .getVariables()
                .putIfAbsent("continue", context.getMessageHeader("continue"));
        // @formatter:on
    }
}
