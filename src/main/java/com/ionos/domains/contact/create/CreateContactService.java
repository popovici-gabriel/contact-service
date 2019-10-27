package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ionos.domains.contact.model.MessageHeaders.INSTANCE_ID;
import static java.util.Objects.requireNonNull;

@Service
public class CreateContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateContactService.class);

    private final StateMachineService<CreateContactState, CreateContactEvent> stateMachineService;

    @Autowired
    public CreateContactService(StateMachineService<CreateContactState, CreateContactEvent> stateMachineService) {
        this.stateMachineService = requireNonNull(stateMachineService);
    }

    String startCreateContact(String parameters, String tenant, String externalCorrelationId) {
        // @formatter:off
        final var instanceId = UUID.randomUUID().toString();
        stateMachineService
                .acquireStateMachine(instanceId, true)
                .sendEvent(MessageBuilder
                        .withPayload(CreateContactEvent.START)
                        .setHeaderIfAbsent(INSTANCE_ID.name(), instanceId)
                        .build());
        return instanceId;
        // @formatter:on
    }

    void signal(String instanceId, CreateContactEvent event) {
        if (!stateMachineService
                .acquireStateMachine(instanceId, false)
                .sendEvent(MessageBuilder
                        .withPayload(event)
                        .setHeaderIfAbsent(INSTANCE_ID.name(), instanceId)
                        .build())) {
            throw new RuntimeException("Transition was not possible");
        }
    }
}
