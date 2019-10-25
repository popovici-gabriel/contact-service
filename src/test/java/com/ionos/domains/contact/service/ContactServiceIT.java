package com.ionos.domains.contact.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.StopWatch;
import com.ionos.domains.contact.event.Event;
import com.ionos.domains.contact.event.EventConsumer;
import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@EmbeddedKafka(topics = "contact")
@DirtiesContext
class ContactServiceIT {

	@Autowired
	private ContactService contactService;

	@Autowired
	private StateMachineService<CreateContactState, CreateContactEvent> stateMachineService;

	@Autowired
	private OperationService operationService;

	@Autowired
	private EventConsumer eventProcessor;

	@Test
	void givenParameters_whenCreateContact_thenOperationIsNotEmpty() {
		// when
		final var operation = contactService.createContactOperation("", "oneandone", UUID.randomUUID().toString());

		// then
		assertThat(operation).isNotNull();
		assertThat(operation.getCorrelationId()).isNotNull();

		final var stateMachine = stateMachineService.acquireStateMachine(operation.getCorrelationId());
		assertThat(stateMachine.getState().getId()).isEqualTo(CreateContactState.CONTACT_REGISTRY_INITIATED);

		// cleanup
		stateMachineService.releaseStateMachine(operation.getCorrelationId());
	}

	@Test
	void givenParameters_whenCreateContact_thenStateOperationIsContactRegistryInitiated() {
		// when
		final var operation = contactService.createContactOperation("", "oneandone", UUID.randomUUID().toString());

		// then
		final var stateMachine = stateMachineService.acquireStateMachine(operation.getCorrelationId());

		assertThat(stateMachine.getState().getId()).isEqualTo(CreateContactState.CONTACT_REGISTRY_INITIATED);
		assertThat(operationService.getActiveOperation(operation.getCorrelationId()).getState())
				.isEqualTo(CreateContactState.CONTACT_REGISTRY_INITIATED);

		// cleanup
		stateMachineService.releaseStateMachine(operation.getCorrelationId());
	}

	@Test
	void givenCreateContactIsStarted_whenSendingRegistryError_thenStateOperationIsRegistryError() {
		// given
		var operation = contactService.createContactOperation("", "oneandone", UUID.randomUUID().toString());
		final var stateMachine = stateMachineService.acquireStateMachine(operation.getCorrelationId());

		// when
		Event event = new Event();
		event.setOperationId(operation.getCorrelationId());
		event.setCreateContactEvent(CreateContactEvent.CONTACT_REGISTRY_ERROR);
		event.setErrorJobId(1_234L);
		eventProcessor.consumeEvent(event);

		// then
		operation = operationService.getActiveOperation(operation.getCorrelationId());
		assertThat(stateMachine.getState().getId()).isEqualTo(CreateContactState.CONTACT_REGISTRY_ERROR);
		assertThat(operation.getState()).isEqualTo(CreateContactState.CONTACT_REGISTRY_ERROR);
		assertThat(operation.isRunning()).isFalse();
		assertThat(operation.getErrorJobId()).isEqualTo(1_234L);

		// cleanup
		stateMachineService.releaseStateMachine(operation.getCorrelationId());
	}

	@Test
	// @RepeatedTest(200)
	void givenCreateContactIsStarted_whenSendingRegistrySuccessAndPersistenceSuccessEvent_thenStateIsContactPersistenceSuccess() {
		// given
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		var operation = contactService.createContactOperation("", "oneandone", UUID.randomUUID().toString());
		final var stateMachine = stateMachineService.acquireStateMachine(operation.getCorrelationId());

		Event registrySuccess = new Event();
		registrySuccess.setOperationId(operation.getCorrelationId());
		registrySuccess.setCreateContactEvent(CreateContactEvent.CONTACT_REGISTRY_SUCCESS);

		Event persistenceSuccess = new Event();
		persistenceSuccess.setOperationId(operation.getCorrelationId());
		persistenceSuccess.setCreateContactEvent(CreateContactEvent.CONTACT_PERSISTENCE_SUCCESS);

		// when
		eventProcessor.consumeEvent(registrySuccess);
		eventProcessor.consumeEvent(persistenceSuccess);
		operation = operationService.getActiveOperation(operation.getCorrelationId());

		// then
		assertThat(stateMachine.getState().getId()).isEqualTo(CreateContactState.END);
		assertThat(operation.getState()).isEqualTo(CreateContactState.CONTACT_PERSISTENCE_SUCCESS);
		assertThat(operation.isRunning()).isFalse();

		// cleanup
		stateMachineService.releaseStateMachine(operation.getCorrelationId());
		stopWatch.stop();
		System.out.println(" Time:  " + stopWatch);
	}

	@AfterEach
	void cleanUp() {
		operationService.deleteAll();
	}

}
