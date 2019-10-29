package com.ionos.domains.contact.update;

import com.ionos.domains.contact.model.UpdateContactEvent;
import com.ionos.domains.contact.model.UpdateContactState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import static java.util.Objects.requireNonNull;

@Service
public class UpdateContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateContactService.class);

    private final StateMachineService<UpdateContactState, UpdateContactEvent> updateService;

    @Autowired
    public UpdateContactService(StateMachineService<UpdateContactState, UpdateContactEvent> updateService) {
        this.updateService = requireNonNull(updateService);
    }

    public String updateContact(String instanceId) {
        LOGGER.info("Starting create contact operation with id [{}]", instanceId);
        // @formatter:off
        final var stateMachine = updateService.acquireStateMachine(instanceId, true);

        stateMachine.sendEvent(UpdateContactEvent.START);
        stateMachine.sendEvent(MessageBuilder
                .withPayload(UpdateContactEvent.CONTACT_REGISTRY_INITIATED)
                .setHeaderIfAbsent("continue", true)
                .build());
        stateMachine.sendEvent(MessageBuilder
                .withPayload(UpdateContactEvent.CONTACT_PERSISTENCE_INITIATED)
                .setHeaderIfAbsent("continue", true)
                .build());
        stateMachine.sendEvent(UpdateContactEvent.STOP);

        updateService.releaseStateMachine(instanceId);
        return instanceId;
        // @formatter:on
    }

    public UpdateContactState getCurrentState(String instanceId) {
        return updateService.acquireStateMachine(instanceId, false).getState().getId();
    }

}
