package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import static java.util.Objects.requireNonNull;

@Service
public class CreateContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateContactService.class);

    private final StateMachineService<CreateContactState, CreateContactEvent> stateMachineService;

    @Autowired
    public CreateContactService(StateMachineService<CreateContactState, CreateContactEvent> stateMachineService) {
        this.stateMachineService = requireNonNull(stateMachineService);
    }

    public String startCreateContact(String instanceId) {
        LOGGER.info("Starting create contact operation with id [{}]", instanceId);
        // @formatter:off
        final var stateMachine = stateMachineService.acquireStateMachine(instanceId, true);

        stateMachine.sendEvent(CreateContactEvent.START);
        stateMachine.sendEvent(MessageBuilder
                .withPayload(CreateContactEvent.CONTACT_REGISTRY_INITIATED)
                .setHeaderIfAbsent("continue", true)
                .build());
        stateMachine.sendEvent(MessageBuilder
                .withPayload(CreateContactEvent.CONTACT_PERSISTENCE_INITIATED)
                .setHeaderIfAbsent("continue", true)
                .build());
        stateMachine.sendEvent(CreateContactEvent.STOP);

        stateMachineService.releaseStateMachine(instanceId);
        return instanceId;
        // @formatter:on
    }

    public CreateContactState getCurrentState(String instanceId) {
        return stateMachineService.acquireStateMachine(instanceId, false).getState().getId();
    }

}
