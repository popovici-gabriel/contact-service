package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import com.ionos.domains.contact.model.Transition;
import com.ionos.domains.contact.model.TransitionError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;

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

    String startCreateContact(String instanceId) {
        // @formatter:off
        final var stateMachine = stateMachineService
                .acquireStateMachine(instanceId, true);

        stateMachine.sendEvent(MessageBuilder
                .withPayload(CreateContactEvent.START)
                .setHeaderIfAbsent(INSTANCE_ID.name(), instanceId)
                .build());

        stateMachine.sendEvent(MessageBuilder
                .withPayload(CreateContactEvent.CONTACT_REGISTRY_INITIATED)
                .setHeaderIfAbsent(INSTANCE_ID.name(), instanceId)
                .build());

        return instanceId;
        // @formatter:on
    }

    public void signal(Transition transition) {
        // @formatter:off
        if (!stateMachineService
                .acquireStateMachine(transition.getInstanceId(), false)
                .sendEvent(MessageBuilder
                        .withPayload(transition.getEvent())
                        .setHeaderIfAbsent(INSTANCE_ID.name(), transition.getInstanceId())
                        .build())) {

            StringBuilder stringBuilder = new StringBuilder("Transition error from: ")
                    .append(transition.getFrom())
                    .append(" -> ")
                    .append(transition.getTo())
                    .append(" with event: ")
                    .append(transition.getEvent());
            throw new TransitionError(stringBuilder.toString());
        }
        // @formatter:on
    }
}
