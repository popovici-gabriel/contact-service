package com.ionos.domains.contact.create;

import com.ionos.domains.contact.event.Event;
import com.ionos.domains.contact.model.CreateContactState;
import com.ionos.domains.contact.model.Operation;
import com.ionos.domains.contact.service.OperationService;

import java.util.Date;

import static java.util.Objects.requireNonNull;

public abstract class CommonAction {

    protected final OperationService operationService;

    protected CommonAction(OperationService operationService) {
        this.operationService = requireNonNull(operationService);
    }

    protected Operation updateState(final Event event, CreateContactState toState) {
        requireNonNull(event);
        requireNonNull(toState);

        final var operation = operationService.deactivateOperation(event.getOperationId());
        // add a new operation
        operation.setId(null);
        operation.setState(toState);
        operation.setActive(true);
        operation.setUpdatedAt(new Date());
        operation.setErrorJobId(event.getErrorJobId());

        return operationService.insert(operation);
    }
}
