package com.ionos.domains.contact.update;

import com.ionos.domains.contact.model.NotFoundError;
import com.ionos.domains.contact.model.UpdateContactEvent;
import com.ionos.domains.contact.model.UpdateContactState;
import java.util.Optional;
import org.springframework.statemachine.guard.Guard;


public class UpdateChoiceGuard {

    static Guard<UpdateContactState, UpdateContactEvent> updateRegistryChoice() {
        return continueGuard();
    }


    static Guard<UpdateContactState, UpdateContactEvent> updatePersistenceChoice() {
        return continueGuard();
    }

    private static Guard<UpdateContactState, UpdateContactEvent> continueGuard() {
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
