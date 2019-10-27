package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.springframework.statemachine.guard.Guard;

import java.util.Optional;


public class CreateChoiceGuard {

    static Guard<CreateContactState, CreateContactEvent> createRegistryChoice() {
        return context -> Optional
                .of(context
                        .getExtendedState()
                        .getVariables()
                        .get("success"))
                .filter(Boolean.class::isInstance)
                .map(Boolean.class::cast)
                .orElseThrow(IllegalAccessError::new);
    }


    static Guard<CreateContactState, CreateContactEvent> createPersistenceChoice() {
        return context -> Optional
                .of(context
                        .getExtendedState()
                        .getVariables()
                        .get("success"))
                .filter(Boolean.class::isInstance)
                .map(Boolean.class::cast)
                .orElseThrow(IllegalAccessError::new);
    }

}
