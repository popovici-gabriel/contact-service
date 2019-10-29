package com.ionos.domains.contact.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import static java.util.Objects.requireNonNull;

@Service
public class DeleteContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteContactService.class);

    private final StateMachineService<DeleteContactState, DeleteContactEvent> deleteService;

    @Autowired
    public DeleteContactService(StateMachineService<DeleteContactState, DeleteContactEvent> deleteService) {
        this.deleteService = requireNonNull(deleteService);
    }

    public String delete(String instanceId) {
        LOGGER.info("------------------------------------");
        LOGGER.info("Starting MACHINE_ID = {}", instanceId);
        // @formatter:off
        final var stateMachine = deleteService.acquireStateMachine(instanceId, true);

        stateMachine.sendEvent(DeleteContactEvent.START);
        stateMachine.sendEvent(MessageBuilder
                .withPayload(DeleteContactEvent.CONTACT_REGISTRY_INITIATED)
                .setHeaderIfAbsent("continue", true)
                .build());
        stateMachine.sendEvent(MessageBuilder
                .withPayload(DeleteContactEvent.CONTACT_PERSISTENCE_INITIATED)
                .setHeaderIfAbsent("continue", true)
                .build());
        stateMachine.sendEvent(DeleteContactEvent.STOP);

        deleteService.releaseStateMachine(instanceId);
        return instanceId;
        // @formatter:on
    }

    public DeleteContactState getCurrentState(String instanceId) {
        return deleteService.acquireStateMachine(instanceId, false).getState().getId();
    }

}
