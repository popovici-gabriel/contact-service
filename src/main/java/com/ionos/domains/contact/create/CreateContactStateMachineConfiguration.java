package com.ionos.domains.contact.create;

import com.ionos.domains.contact.configuration.LifecycleStateAdapter;
import java.util.EnumSet;
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
import static com.ionos.domains.contact.create.CreateChoiceGuard.createRegistryChoice;
import static com.ionos.domains.contact.create.CreateContactEvent.STOP;
import static com.ionos.domains.contact.create.CreateContactState.CREATE_PERSISTENCE_CHOICE;
import static com.ionos.domains.contact.create.CreateContactState.CREATE_PERSISTENCE_ERROR;
import static com.ionos.domains.contact.create.CreateContactState.CREATE_PERSISTENCE_SUCCESS;
import static com.ionos.domains.contact.create.CreateContactState.CREATE_REGISTRY_CHOICE;
import static com.ionos.domains.contact.create.CreateContactState.CREATE_REGISTRY_ERROR;
import static com.ionos.domains.contact.create.CreateContactState.CREATE_REGISTRY_INITIATED;
import static com.ionos.domains.contact.create.CreateContactState.CREATE_REGISTRY_SUCCESS;
import static com.ionos.domains.contact.create.CreateContactState.END;

@Configuration
@EnableStateMachineFactory(name = "createStateMachineFactory")
public class CreateContactStateMachineConfiguration
		extends
			EnumStateMachineConfigurerAdapter<CreateContactState, CreateContactEvent> {

	@Autowired
	private StateMachineRuntimePersister<CreateContactState, CreateContactEvent, String> createStateMachineRuntimePersister;

	@Autowired
	private CreateContactAction createContactAction;

	@Override
	public void configure(StateMachineConfigurationConfigurer<CreateContactState, CreateContactEvent> config)
			throws Exception {
		// @formatter:off

		config
				.withPersistence()
				.runtimePersister(createStateMachineRuntimePersister);

		config
				.withConfiguration()
				.machineId("create-contact")
				.autoStartup(false)
				.listener(new LifecycleStateAdapter());
		// @formatter:on
	}

	@Override
	public void configure(StateMachineStateConfigurer<CreateContactState, CreateContactEvent> states) throws Exception {
		// @formatter:off
		states
				.withStates()
				.initial(CreateContactState.START)
				.state(CREATE_REGISTRY_INITIATED)
				.choice(CREATE_REGISTRY_CHOICE)
				.state(CreateContactState.CREATE_PERSISTENCE_INITIATED)
				.choice(CREATE_PERSISTENCE_CHOICE)
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
					.source(CreateContactState.START).target(CREATE_REGISTRY_INITIATED)
					.event(CreateContactEvent.START)
					.and()

				.withExternal()
					.source(CREATE_REGISTRY_INITIATED).target(CREATE_REGISTRY_CHOICE)
					.event(CreateContactEvent.CONTACT_REGISTRY_INITIATED)
					.action(createContactAction::create)
					.and()

				.withChoice()
					.source(CREATE_REGISTRY_CHOICE)
					.first(CREATE_REGISTRY_SUCCESS, createRegistryChoice())
					.last(CREATE_REGISTRY_ERROR)
					.and()

				.withExternal()
					.source(CREATE_REGISTRY_ERROR).target(END)
					.event(STOP)
					.and()

				.withExternal()
					.source(CREATE_REGISTRY_SUCCESS).target(CreateContactState.CREATE_PERSISTENCE_INITIATED)
					.event(CreateContactEvent.CONTACT_PERSISTENCE_INITIATED)
					.action(createContactAction::create)
					.and()

				.withJoin()
					.source(CreateContactState.CREATE_PERSISTENCE_INITIATED).target(CREATE_PERSISTENCE_CHOICE)
					.and()

				.withChoice()
					.source(CREATE_PERSISTENCE_CHOICE)
					.first(CREATE_PERSISTENCE_SUCCESS, createPersistenceChoice())
					.last(CREATE_PERSISTENCE_ERROR)
					.and()

				.withExternal()
					.source(CREATE_PERSISTENCE_ERROR).target(END)
					.event(STOP)
					.and()

				.withExternal()
					.source(CREATE_PERSISTENCE_SUCCESS).target(END)
					.event(STOP);
		// @formatter:on
	}

	@Bean
	public StateMachineService<CreateContactState, CreateContactEvent> createService(
			StateMachineFactory<CreateContactState, CreateContactEvent> createStateMachineFactory,
			StateMachineRuntimePersister<CreateContactState, CreateContactEvent, String> createStateMachineRuntimePersister) {
		return new DefaultStateMachineService<>(createStateMachineFactory, createStateMachineRuntimePersister);
	}

}
