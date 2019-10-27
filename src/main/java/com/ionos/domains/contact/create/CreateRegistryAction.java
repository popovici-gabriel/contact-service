package com.ionos.domains.contact.create;

import com.ionos.domains.contact.event.Event;
import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import com.ionos.domains.contact.model.Operation;
import com.ionos.domains.contact.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import static com.ionos.domains.contact.model.StateMachineHeaders.EVENT;
import static com.ionos.domains.contact.model.StateMachineHeaders.OPERATION;

@Component
public class CreateRegistryAction extends CommonAction {

    @Autowired
    public CreateRegistryAction(OperationService operationService) {
        super(operationService);
    }

    void contactRegistryInitiated(StateContext<CreateContactState, CreateContactEvent> context) {
        final var operation = (Operation) context.getMessageHeader(OPERATION.getHeader());
        operationService.insert(operation);
    }

    void contactRegistrySuccess(StateContext<CreateContactState, CreateContactEvent> context) {
        final var event = (Event) context.getMessageHeader(EVENT.getHeader());
        updateState(event, context.getTarget().getId());
    }

    void contactRegistryError(StateContext<CreateContactState, CreateContactEvent> context) {
        final var event = (Event) context.getMessageHeader(EVENT.getHeader());
        updateState(event, context.getTarget().getId());
        operationService.updateRunningFlag(event.getOperationId());
    }

}
