package com.ionos.domains.contact.create;

import com.ionos.domains.contact.configuration.CreateContactAdapter;
import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

import static com.ionos.domains.contact.create.CreateChoiceGuard.createPersistenceChoice;
import static com.ionos.domains.contact.create.CreateChoiceGuard.createRegistryChoice;
import static com.ionos.domains.contact.model.CreateContactState.*;

//@Configuration
//@EnableStateMachineFactory
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
				.initial(START)
				.state(CONTACT_REGISTRY_INITIATED)
				.state(CONTACT_REGISTRY_CHOICE)

				.state(CONTACT_PERSISTENCE_INITIATED)
				.state(CONTACT_PERSISTENCE_CHOICE)

				.end(END);
		// @formatter:on
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<CreateContactState, CreateContactEvent> transitions)
			throws Exception {
		// @formatter:off
		transitions
				.withExternal()
					.source(START).target(CONTACT_REGISTRY_INITIATED)
					.event(CreateContactEvent.START)
					.action(createRegistryAction::sendContactRegistryInitiated)
					.and()

				.withExternal()
					.source(CONTACT_REGISTRY_INITIATED).target(CONTACT_REGISTRY_CHOICE)
					.event(CreateContactEvent.CONTACT_REGISTRY_INITIATED)
					.action(createRegistryAction::contactRegistrySuccess)
					.and()

				.withChoice()
					.source(CONTACT_REGISTRY_CHOICE)
					.first(CONTACT_REGISTRY_SUCCESS, createRegistryChoice())
					.last(CONTACT_REGISTRY_ERROR, createRegistryAction::end)
					.and()

				.withExternal()
					.source(CONTACT_REGISTRY_SUCCESS).target(CONTACT_PERSISTENCE_INITIATED)
					.event(CreateContactEvent.CONTACT_PERSISTENCE_INITIATED)
					.action(createPersistenceAction::contactPersistenceInitiated)
					.and()

				.withExternal()
					.source(CONTACT_PERSISTENCE_INITIATED).target(CONTACT_PERSISTENCE_CHOICE)
					.event(CreateContactEvent.CONTACT_PERSISTENCE_INITIATED)
					.action(createPersistenceAction::contactPersistenceSuccess)
					.and()

				.withChoice()
					.source(CONTACT_PERSISTENCE_CHOICE)
					.first(CONTACT_PERSISTENCE_SUCCESS, createPersistenceChoice())
					.last(CONTACT_PERSISTENCE_ERROR)
					.and()

				.withExternal()
					.source(CONTACT_PERSISTENCE_SUCCESS).target(END)
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
