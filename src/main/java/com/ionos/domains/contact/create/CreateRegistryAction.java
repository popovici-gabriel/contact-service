package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import com.ionos.domains.contact.model.MessageHeaders;
import com.ionos.domains.contact.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreateRegistryAction extends CommonAction {

    @Autowired
    public CreateRegistryAction(OperationService operationService,
                                CreateContactService createContactService) {
        super(operationService, createContactService);
    }

    void contactRegistryInitiated(StateContext<CreateContactState, CreateContactEvent> context) {
    }

    void contactRegistrySuccess(StateContext<CreateContactState, CreateContactEvent> context) {
    }

    void contactRegistryError(StateContext<CreateContactState, CreateContactEvent> context) {
    }

    public void postContact(StateContext<CreateContactState, CreateContactEvent> context) {
    }

    public void sendContactRegistryInitiated(StateContext<CreateContactState, CreateContactEvent> context) {
        var instanceId = Optional.of(context.getMessageHeader(MessageHeaders.INSTANCE_ID))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElseThrow(() -> new RuntimeException("header is not present"));
        createContactService.signal(instanceId, CreateContactEvent.CONTACT_REGISTRY_INITIATED);
    }
}
