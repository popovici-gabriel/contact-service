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
public class CreateAction extends CommonAction {

    @Autowired
    public CreateAction(OperationService operationService) {
        super(operationService);
    }

    void createContactEnd(StateContext<CreateContactState, CreateContactEvent> context) {
        final var event = (Event) context.getMessageHeader(EVENT.getHeader());
        operationService.updateRunningFlag(event.getOperationId());
    }
}
