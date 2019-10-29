package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.NotFoundError;
import java.util.Optional;
import org.springframework.statemachine.guard.Guard;


public class CreateChoiceGuard {

    static Guard<CreateContactState, CreateContactEvent> createRegistryChoice() {
        return continueGuard();
    }


    static Guard<CreateContactState, CreateContactEvent> createPersistenceChoice() {
        return continueGuard();
    }

    private static Guard<CreateContactState, CreateContactEvent> continueGuard() {
        return context -> Optional
                .of(context
                        .getExtendedState()
                        .getVariables()
                        .get("continue"))
                .filter(Boolean.class::isInstance)
                .map(Boolean.class::cast)
                .orElseThrow(() -> new NotFoundError("success not found in context variables"));
    }

}
