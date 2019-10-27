package com.ionos.domains.contact.create;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.ionos.domains.contact.configuration.CreateContactAdapter;
import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
@EnableStateMachineFactory
public class CreateContactUsingChoiceStateMachineConfiguration
		extends
			EnumStateMachineConfigurerAdapter<CreateContactState, CreateContactEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateContactUsingChoiceStateMachineConfiguration.class);

	@Autowired
	private StateMachineRuntimePersister<CreateContactState, CreateContactEvent, String> stateMachineRuntimePersister;

	@Autowired
	private CreateAction createAction;

	@Autowired
	private CreateRegistryAction createRegistryAction;

	@Autowired
	private CreatePersistenceAction createPersistenceAction;

	@Override
	public void configure(StateMachineConfigurationConfigurer<CreateContactState, CreateContactEvent> config)
			throws Exception {
		// @formatter:off

		config
				.withPersistence()
				.runtimePersister(stateMachineRuntimePersister);

		config
				.withConfiguration()
				.machineId("create-contact-with-choices")
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
				.state(CreateContactState.CONTACT_REGISTRY_CHOICE)

				.state(CreateContactState.CONTACT_PERSISTENCE_INITIATED)
				.state(CreateContactState.CONTACT_PERSISTENCE_CHOICE)

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
					.action(createRegistryAction::contactRegistryInitiated)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_REGISTRY_INITIATED).target(CreateContactState.CONTACT_REGISTRY_SUCCESS)
					.event(CreateContactEvent.CONTACT_REGISTRY_SUCCESS)
					.action(createRegistryAction::contactRegistrySuccess)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_REGISTRY_INITIATED).target(CreateContactState.CONTACT_REGISTRY_ERROR)
					.event(CreateContactEvent.CONTACT_REGISTRY_ERROR)
					.action(createRegistryAction::contactRegistryError)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_REGISTRY_SUCCESS).target(CreateContactState.CONTACT_PERSISTENCE_INITIATED)
					.event(CreateContactEvent.CONTACT_PERSISTENCE_INITIATED)
					.action(createPersistenceAction::contactPersistenceInitiated)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_PERSISTENCE_INITIATED).target(CreateContactState.CONTACT_PERSISTENCE_SUCCESS)
					.event(CreateContactEvent.CONTACT_PERSISTENCE_SUCCESS)
					.action(createPersistenceAction::contactPersistenceSuccess)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_PERSISTENCE_INITIATED).target(CreateContactState.CONTACT_PERSISTENCE_ERROR)
					.event(CreateContactEvent.CONTACT_PERSISTENCE_ERROR)
					.action(createPersistenceAction::contactPersistenceError)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_PERSISTENCE_SUCCESS).target(CreateContactState.END)
					.event(CreateContactEvent.STOP)
					.action(createAction::createContactEnd);
		// @formatter:on
	}

	@Bean
	public StateMachineService<CreateContactState, CreateContactEvent> stateMachineService(
			StateMachineFactory<CreateContactState, CreateContactEvent> stateMachineFactory,
			StateMachineRuntimePersister<CreateContactState, CreateContactEvent, String> stateMachineRuntimePersister) {
		return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
	}
}
