package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import com.ionos.domains.contact.model.NotFoundError;
import java.util.Optional;
import org.springframework.statemachine.guard.Guard;


public class CreateChoiceGuard {

    static Guard<CreateContactState, CreateContactEvent> createRegistryChoice() {
        return context -> true;
    }


    static Guard<CreateContactState, CreateContactEvent> createPersistenceChoice() {
        return context -> Optional
                .of(context
                        .getExtendedState()
                        .getVariables()
                        .get("success"))
                .filter(Boolean.class::isInstance)
                .map(Boolean.class::cast)
                .orElseThrow(() -> new NotFoundError("success not found in context variables"));
    }

}
