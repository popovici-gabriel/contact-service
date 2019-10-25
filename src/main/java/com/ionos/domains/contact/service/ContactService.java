package com.ionos.domains.contact.service;

import static com.ionos.domains.contact.model.CreateContactEvent.CONTACT_PERSISTENCE_INITIATED;
import static com.ionos.domains.contact.model.CreateContactEvent.STOP;
import static com.ionos.domains.contact.model.StateMachineHeaders.EVENT;
import static com.ionos.domains.contact.model.StateMachineHeaders.OPERATION;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import com.ionos.domains.contact.event.Event;
import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import com.ionos.domains.contact.model.Operation;
import com.ionos.domains.contact.model.OperationBuilder;
import com.ionos.domains.contact.model.Type;

@Service
public class ContactService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

	private final StateMachineService<CreateContactState, CreateContactEvent> stateMachineService;

	@Autowired
	public ContactService(StateMachineService<CreateContactState, CreateContactEvent> stateMachineService) {
		this.stateMachineService = requireNonNull(stateMachineService);
	}

	public Operation createContactOperation(String parameters, String tenant, String externalCorrelationId) {
		// @formatter:off
		final var operation = OperationBuilder.builder().tenant(tenant).parameters(parameters).type(Type.CRATE_CONTACT)
				.state(CreateContactState.CONTACT_REGISTRY_INITIATED).externalCorrelationId(externalCorrelationId)
				.build();
		LOGGER.info("About to start operation: {}", operation);
		stateMachineService.acquireStateMachine(operation.getCorrelationId(), true).sendEvent(MessageBuilder
				.withPayload(CreateContactEvent.START).setHeaderIfAbsent(OPERATION.getHeader(), operation).build());
		return operation;
		// @formatter:on
	}

	public void processEvent(Event event) {
		switch (event.getCreateContactEvent()) {
			case CONTACT_REGISTRY_ERROR :
			case CONTACT_PERSISTENCE_ERROR :
				sendEvent(event);
				stateMachineService.releaseStateMachine(event.getOperationId());
				break;
			case CONTACT_REGISTRY_SUCCESS :
				sendEvent(event);
				sendEvent(event.getOperationId(), CONTACT_PERSISTENCE_INITIATED);
				break;
			case CONTACT_PERSISTENCE_SUCCESS :
				sendEvent(event);
				sendEvent(event.getOperationId(), STOP);
				stateMachineService.releaseStateMachine(event.getOperationId());
				break;
			default :
				throw new IllegalStateException(format("State %s not implemented", event.getCreateContactEvent()));
		}
	}

	private void sendEvent(Event event) {
		// @formatter:off
		if (!stateMachineService.acquireStateMachine(event.getOperationId(), false).sendEvent(MessageBuilder
				.withPayload(event.getCreateContactEvent()).setHeaderIfAbsent(EVENT.getHeader(), event).build())) {
			LOGGER.error("Operation Id [{}] in state [{}] not processed", event.getOperationId(),
					event.getCreateContactEvent());
		}
		// @formatter:on
	}

	private void sendEvent(String operationId, CreateContactEvent contactEvent) {
		requireNonNull(operationId);
		requireNonNull(contactEvent);

		Event event = new Event();
		event.setOperationId(operationId);
		event.setCreateContactEvent(contactEvent);
		sendEvent(event);
	}

}
