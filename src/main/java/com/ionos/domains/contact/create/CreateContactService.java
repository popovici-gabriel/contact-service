package com.ionos.domains.contact.create;

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

    private final StateMachineService<CreateContactState, CreateContactEvent> createService;

    @Autowired
    public CreateContactService(StateMachineService<CreateContactState, CreateContactEvent> createService) {
        this.createService = requireNonNull(createService);
    }

    public String create(String instanceId) {
        LOGGER.info("Starting create contact operation with id [{}]", instanceId);
        // @formatter:off
        final var stateMachine = createService.acquireStateMachine(instanceId, true);

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

        createService.releaseStateMachine(instanceId);
        return instanceId;
        // @formatter:on
    }

    public CreateContactState getCurrentState(String instanceId) {
        return createService.acquireStateMachine(instanceId, false).getState().getId();
    }

}
