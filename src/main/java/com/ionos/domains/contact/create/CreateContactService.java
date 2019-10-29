package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import static java.util.Objects.requireNonNull;
import static org.springframework.messaging.support.MessageBuilder.withPayload;

@Service
public class CreateContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateContactService.class);

    private final StateMachineService<CreateContactState, CreateContactEvent> stateMachineService;

    @Autowired
    public CreateContactService(StateMachineService<CreateContactState, CreateContactEvent> stateMachineService) {
        this.stateMachineService = requireNonNull(stateMachineService);
    }

    public String startCreateContact(String instanceId) {
        // @formatter:off
        final var stateMachine = stateMachineService.acquireStateMachine(instanceId, true);

        stateMachine.sendEvent(withPayload(CreateContactEvent.START).build());
        stateMachine.sendEvent(withPayload(CreateContactEvent.CONTACT_REGISTRY_INITIATED).build());
        stateMachine.sendEvent(withPayload(CreateContactEvent.CONTACT_PERSISTENCE_INITIATED).build());
        stateMachine.sendEvent(withPayload(CreateContactEvent.STOP).build());

        return instanceId;
        // @formatter:on
    }

    public CreateContactState getCurrentState(String instanceId) {
        return stateMachineService.acquireStateMachine(instanceId, false).getState().getId();
    }

}
