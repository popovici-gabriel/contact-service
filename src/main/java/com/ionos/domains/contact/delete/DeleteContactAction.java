package com.ionos.domains.contact.delete;

import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class DeleteContactAction {

    void delete(StateContext<DeleteContactState, DeleteContactEvent> context) {
        addContinueVariable(context);
    }

    private void addContinueVariable(StateContext<DeleteContactState, DeleteContactEvent> context) {
        // @formatter:off
        context
                .getExtendedState()
                .getVariables()
                .putIfAbsent("continue", context.getMessageHeader("continue"));
        // @formatter:on
    }
}
