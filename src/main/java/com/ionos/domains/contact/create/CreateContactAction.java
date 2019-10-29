package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class CreateContactAction {

    void create(StateContext<CreateContactState, CreateContactEvent> context) {
        addContinueVariable(context);
    }

    private void addContinueVariable(StateContext<CreateContactState, CreateContactEvent> context) {
        // @formatter:off
        context
                .getExtendedState()
                .getVariables()
                .putIfAbsent("continue", context.getMessageHeader("continue"));
        // @formatter:on
    }
}
