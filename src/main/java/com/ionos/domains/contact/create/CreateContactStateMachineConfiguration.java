package com.ionos.domains.contact.create;

import com.ionos.domains.contact.configuration.CreateContactAdapter;
import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import java.util.EnumSet;
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
import static com.ionos.domains.contact.create.CreateChoiceGuard.createPersistenceChoice;
import static com.ionos.domains.contact.model.CreateContactEvent.STOP;
import static com.ionos.domains.contact.model.CreateContactState.CONTACT_PERSISTENCE_CHOICE;
import static com.ionos.domains.contact.model.CreateContactState.CONTACT_PERSISTENCE_ERROR;
import static com.ionos.domains.contact.model.CreateContactState.CONTACT_PERSISTENCE_SUCCESS;
import static com.ionos.domains.contact.model.CreateContactState.CONTACT_REGISTRY_CHOICE;
import static com.ionos.domains.contact.model.CreateContactState.CONTACT_REGISTRY_ERROR;
import static com.ionos.domains.contact.model.CreateContactState.CONTACT_REGISTRY_INITIATED;
import static com.ionos.domains.contact.model.CreateContactState.CONTACT_REGISTRY_SUCCESS;
import static com.ionos.domains.contact.model.CreateContactState.END;

@Configuration
@EnableStateMachineFactory
public class CreateContactStateMachineConfiguration
		extends
			EnumStateMachineConfigurerAdapter<CreateContactState, CreateContactEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateContactStateMachineConfiguration.class);

	@Autowired
	private StateMachineRuntimePersister<CreateContactState, CreateContactEvent, String> stateMachineRuntimePersister;

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
				.state(CONTACT_REGISTRY_INITIATED)
				.choice(CONTACT_REGISTRY_CHOICE)

				.state(CreateContactState.CONTACT_PERSISTENCE_INITIATED)
				.choice(CONTACT_PERSISTENCE_CHOICE)

				.end(END)
				.states(EnumSet.allOf(CreateContactState.class));
		// @formatter:on
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<CreateContactState, CreateContactEvent> transitions)
			throws Exception {
		// @formatter:off
		transitions
				.withExternal()
					.source(CreateContactState.START).target(CONTACT_REGISTRY_INITIATED)
					.event(CreateContactEvent.START)
					.and()

				.withExternal()
					.source(CONTACT_REGISTRY_INITIATED).target(CONTACT_REGISTRY_CHOICE)
					.event(CreateContactEvent.CONTACT_REGISTRY_INITIATED)
					.and()

				.withChoice()
					.source(CONTACT_REGISTRY_CHOICE)
					.first(CONTACT_REGISTRY_SUCCESS,context -> true)
					.last(CONTACT_REGISTRY_ERROR)
					.and()

				.withExternal()
					.source(CONTACT_REGISTRY_ERROR).target(END)
					.event(STOP)
					.and()

				.withExternal()
					.source(CONTACT_REGISTRY_SUCCESS).target(CreateContactState.CONTACT_PERSISTENCE_INITIATED)
					.event(CreateContactEvent.CONTACT_PERSISTENCE_INITIATED)
					.and()

				.withExternal()
					.source(CreateContactState.CONTACT_PERSISTENCE_INITIATED).target(CONTACT_PERSISTENCE_CHOICE)
					.event(CreateContactEvent.CONTACT_PERSISTENCE_INITIATED)
					.and()

				.withChoice()
					.source(CONTACT_PERSISTENCE_CHOICE)
					.first(CONTACT_PERSISTENCE_SUCCESS, createPersistenceChoice())
					.last(CONTACT_PERSISTENCE_ERROR)
					.and()

				.withExternal()
					.source(CONTACT_PERSISTENCE_SUCCESS).target(END)
					.event(STOP);
		// @formatter:on
	}

	@Bean
	public StateMachineService<CreateContactState, CreateContactEvent> stateMachineService(
			StateMachineFactory<CreateContactState, CreateContactEvent> stateMachineFactory,
			StateMachineRuntimePersister<CreateContactState, CreateContactEvent, String> stateMachineRuntimePersister) {
		return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
	}

}
