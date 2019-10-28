package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class CreateRegistryAction {

    void contactRegistrySuccess(StateContext<CreateContactState, CreateContactEvent> context) {
        // @formatter:off
        context
                .getExtendedState()
                .getVariables()
                .putIfAbsent("success", true);
        // @formatter:on
    }

    void contactRegistryError(StateContext<CreateContactState, CreateContactEvent> context) {
        context
                .getExtendedState()
                .getVariables()
                .putIfAbsent("success", false);
    }

    public void error(StateContext<CreateContactState, CreateContactEvent> context) {
        System.out.println("test");
    }

    public void test(StateContext<CreateContactState, CreateContactEvent> context) {
        System.out.println("test");

    }
}
