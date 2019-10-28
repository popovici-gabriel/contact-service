package com.ionos.domains.contact.configuration;

import static com.ionos.domains.contact.model.StateMachineHeaders.EVENT;
import static com.ionos.domains.contact.model.StateMachineHeaders.OPERATION;
import static java.util.Objects.requireNonNull;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;
import com.ionos.domains.contact.event.Event;
import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import com.ionos.domains.contact.model.Operation;
import com.ionos.domains.contact.service.OperationService;

//@Configuration
//@EnableStateMachineFactory
public class CreateContactStateMachineConfiguration
		extends
			EnumStateMachineConfigurerAdapter<CreateContactState, CreateContactEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateContactStateMachineConfiguration.class);

	@Autowired
	private StateMachineRuntimePersister<CreateContactState, CreateContactEvent, String> stateMachineRuntimePersister;

	@Autowired
	private OperationService operationService;

	@Override
	public void configure(StateMachineConfigurationConfigurer<CreateContactState, CreateContactEvent> config)
			throws Exception {
		// @formatter:off

		config
				.withPersistence()
				.runtimePersister(stateMachineRuntimePersister);

		config
				.withConfiguration()
				.machineId("create-contact")
				.autoStartup(true)
				.listener(new CreateContactAdapter());
		// @formatter:on
	}

	@Override
	public void configure(StateMachineStateConfigurer<CreateContactState, CreateContactEvent> states) throws Exception {
		// @formatter:off
		states
				.withStates()
				.initial(CreateContactState.START)
				.state(CreateContactState.CONTACT_REGISTRY_INITIATED)
				.state(CreateContactState.CONTACT_REGISTRY_SUCCESS)

				.state(CreateContactState.CONTACT_PERSISTENCE_INITIATED)
				.state(CreateContactState.CONTACT_PERSISTENCE_SUCCESS)

				.end(CreateContactState.CONTACT_REGISTRY_ERROR)
				.end(CreateContactState.CONTACT_PERSISTENCE_ERROR)
				.end(CreateContactState.END);
		// @formatter:on
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<CreateContactState, CreateContactEvent> transitions)
			throws Exception {
		// @formatter:off
		transitions
				.withExternal()
					.source(CreateContactState.START).target(CreateContactState.CONTACT_REGISTRY_INITIATED)
					.event(CreateContactEvent.START)
					.action(this::contactRegistryInitiated)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_REGISTRY_INITIATED).target(CreateContactState.CONTACT_REGISTRY_SUCCESS)
					.event(CreateContactEvent.CONTACT_REGISTRY_SUCCESS)
					.action(this::contactRegistrySuccess)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_REGISTRY_INITIATED).target(CreateContactState.CONTACT_REGISTRY_ERROR)
					.event(CreateContactEvent.CONTACT_REGISTRY_ERROR)
					.action(this::contactRegistryError)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_REGISTRY_SUCCESS).target(CreateContactState.CONTACT_PERSISTENCE_INITIATED)
					.event(CreateContactEvent.CONTACT_PERSISTENCE_INITIATED)
					.action(this::contactPersistenceInitiated)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_PERSISTENCE_INITIATED).target(CreateContactState.CONTACT_PERSISTENCE_SUCCESS)
					.event(CreateContactEvent.CONTACT_PERSISTENCE_SUCCESS)
					.action(this::contactPersistenceSuccess)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_PERSISTENCE_INITIATED).target(CreateContactState.CONTACT_PERSISTENCE_ERROR)
					.event(CreateContactEvent.CONTACT_PERSISTENCE_ERROR)
					.action(this::contactPersistenceError)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_PERSISTENCE_SUCCESS).target(CreateContactState.END)
					.event(CreateContactEvent.STOP)
					.action(this::createContactEnd);
		// @formatter:on
	}

	@Bean
	public StateMachineService<CreateContactState, CreateContactEvent> stateMachineService(
			StateMachineFactory<CreateContactState, CreateContactEvent> stateMachineFactory,
			StateMachineRuntimePersister<CreateContactState, CreateContactEvent, String> stateMachineRuntimePersister) {
		return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
	}

	private void contactRegistryInitiated(StateContext<CreateContactState, CreateContactEvent> context) {
		final var operation = (Operation) context.getMessageHeader(OPERATION.header());
		operationService.insert(operation);
	}

	private void contactRegistrySuccess(StateContext<CreateContactState, CreateContactEvent> context) {
		final var event = (Event) context.getMessageHeader(EVENT.header());
		updateState(event, context.getTarget().getId());
	}

	private void contactRegistryError(StateContext<CreateContactState, CreateContactEvent> context) {
		final var event = (Event) context.getMessageHeader(EVENT.header());
		updateState(event, context.getTarget().getId());
		operationService.updateRunningFlag(event.getOperationId());
	}

	private void contactPersistenceInitiated(StateContext<CreateContactState, CreateContactEvent> context) {
		final var event = (Event) context.getMessageHeader(EVENT.header());
		updateState(event, context.getTarget().getId());
	}

	private void contactPersistenceSuccess(StateContext<CreateContactState, CreateContactEvent> context) {
		final var event = (Event) context.getMessageHeader(EVENT.header());
		updateState(event, context.getTarget().getId());
	}

	private void contactPersistenceError(StateContext<CreateContactState, CreateContactEvent> context) {
		final var event = (Event) context.getMessageHeader(EVENT.header());
		updateState(event, context.getTarget().getId());
		operationService.updateRunningFlag(event.getOperationId());
	}

	private void createContactEnd(StateContext<CreateContactState, CreateContactEvent> context) {
		final var event = (Event) context.getMessageHeader(EVENT.header());
		operationService.updateRunningFlag(event.getOperationId());
	}

	private Operation updateState(final Event event, CreateContactState toState) {
		requireNonNull(event);
		requireNonNull(toState);

		final var operation = operationService.deactivateOperation(event.getOperationId());
		// add a new operation
		operation.setId(null);
		operation.setState(toState);
		operation.setActive(true);
		operation.setUpdatedAt(new Date());
		operation.setErrorJobId(event.getErrorJobId());

		return operationService.insert(operation);
	}
}
