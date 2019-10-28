package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import com.ionos.domains.contact.model.MessageHeaders;
import com.ionos.domains.contact.model.Transition;
import com.ionos.domains.contact.service.OperationService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

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
        // @formatter:off
        context
                .getExtendedState()
                .getVariables()
                .putIfAbsent("success", true);
        signalEvent(context, CreateContactEvent.CONTACT_REGISTRY_SUCCESS);
        // @formatter:on
    }

    void contactRegistryError(StateContext<CreateContactState, CreateContactEvent> context) {
        // @formatter:off
        context
                .getExtendedState()
                .getVariables()
                .putIfAbsent("success", false);
        signalEvent(context, CreateContactEvent.CONTACT_REGISTRY_ERROR);
    }

    public void postContact(StateContext<CreateContactState, CreateContactEvent> context) {
    }

    public void sendContactRegistryInitiated(StateContext<CreateContactState, CreateContactEvent> context) {
        signalEvent(context, CreateContactEvent.CONTACT_REGISTRY_INITIATED);
    }

    public void end(StateContext<CreateContactState, CreateContactEvent> context) {
        signalEvent(context, CreateContactEvent.STOP);
    }

    private void signalEvent(StateContext<CreateContactState, CreateContactEvent> context, CreateContactEvent event) {
        // @formatter:off
        final var instanceId = Optional
                .of(context.getMessageHeader(MessageHeaders.INSTANCE_ID))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElseThrow(() -> new RuntimeException("header is not present"));

        final var transition = Transition
                .builder()
                .from(context.getSource().getId())
                .to(context.getTarget().getId())
                .event(event)
                .instanceId(instanceId)
                .build();
        createContactService.signal(transition);
        // @formatter:on
    }

}
