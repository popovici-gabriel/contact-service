package com.ionos.domains.contact.delete;

import com.ionos.domains.contact.model.NotFoundError;
import java.util.Optional;
import org.springframework.statemachine.guard.Guard;


public class DeleteChoiceGuard {

    static Guard<DeleteContactState, DeleteContactEvent> deleteRegistryChoice() {
        return continueGuard();
    }


    static Guard<DeleteContactState, DeleteContactEvent> deletePersistenceChoice() {
        return continueGuard();
    }

    private static Guard<DeleteContactState, DeleteContactEvent> continueGuard() {
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
