package com.ionos.domains.contact.create;

import com.ionos.domains.contact.event.Event;
import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import com.ionos.domains.contact.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import static com.ionos.domains.contact.model.StateMachineHeaders.EVENT;

@Component
public class CreatePersistenceAction extends CommonAction {

    @Autowired
    public CreatePersistenceAction(OperationService operationService, CreateContactService createContactService) {
        super(operationService, createContactService);
    }

    void contactPersistenceInitiated(StateContext<CreateContactState, CreateContactEvent> context) {
        final var event = (Event) context.getMessageHeader(EVENT.header());
        updateState(event, context.getTarget().getId());
    }

    void contactPersistenceSuccess(StateContext<CreateContactState, CreateContactEvent> context) {
        final var event = (Event) context.getMessageHeader(EVENT.header());
        updateState(event, context.getTarget().getId());
    }

    void contactPersistenceError(StateContext<CreateContactState, CreateContactEvent> context) {
        final var event = (Event) context.getMessageHeader(EVENT.header());
        updateState(event, context.getTarget().getId());
        operationService.updateRunningFlag(event.getOperationId());
    }
}
