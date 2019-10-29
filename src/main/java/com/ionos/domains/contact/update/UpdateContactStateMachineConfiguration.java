package com.ionos.domains.contact.update;

import com.ionos.domains.contact.configuration.LifecycleStateAdapter;
import com.ionos.domains.contact.model.UpdateContactEvent;
import com.ionos.domains.contact.model.UpdateContactState;
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
import static com.ionos.domains.contact.model.UpdateContactEvent.STOP;
import static com.ionos.domains.contact.model.UpdateContactState.CONTACT_PERSISTENCE_CHOICE;
import static com.ionos.domains.contact.model.UpdateContactState.CONTACT_PERSISTENCE_ERROR;
import static com.ionos.domains.contact.model.UpdateContactState.CONTACT_PERSISTENCE_SUCCESS;
import static com.ionos.domains.contact.model.UpdateContactState.CONTACT_REGISTRY_CHOICE;
import static com.ionos.domains.contact.model.UpdateContactState.CONTACT_REGISTRY_ERROR;
import static com.ionos.domains.contact.model.UpdateContactState.CONTACT_REGISTRY_INITIATED;
import static com.ionos.domains.contact.model.UpdateContactState.CONTACT_REGISTRY_SUCCESS;
import static com.ionos.domains.contact.model.UpdateContactState.END;
import static com.ionos.domains.contact.update.UpdateChoiceGuard.updatePersistenceChoice;
import static com.ionos.domains.contact.update.UpdateChoiceGuard.updateRegistryChoice;

@Configuration
@EnableStateMachineFactory(name = "updateStateMachineFactory")
public class UpdateContactStateMachineConfiguration
		extends
			EnumStateMachineConfigurerAdapter<UpdateContactState, UpdateContactEvent> {

	@Autowired
	private StateMachineRuntimePersister<UpdateContactState, UpdateContactEvent, String> updateStateMachineRuntimePersister;

	@Autowired
	private UpdateContactAction updateContactAction;

	@Override
	public void configure(StateMachineConfigurationConfigurer<UpdateContactState, UpdateContactEvent> config)
			throws Exception {
		// @formatter:off

		config
				.withPersistence()
				.runtimePersister(updateStateMachineRuntimePersister);

		config
				.withConfiguration()
				.machineId("update-contact")
				.autoStartup(false)
				.listener(new LifecycleStateAdapter());
		// @formatter:on
	}

	@Override
	public void configure(StateMachineStateConfigurer<UpdateContactState, UpdateContactEvent> states) throws Exception {
		// @formatter:off
		states
				.withStates()
				.initial(UpdateContactState.START)
				.state(CONTACT_REGISTRY_INITIATED)
				.choice(CONTACT_REGISTRY_CHOICE)
				.state(UpdateContactState.CONTACT_PERSISTENCE_INITIATED)
				.choice(CONTACT_PERSISTENCE_CHOICE)
				.end(END)
				.states(EnumSet.allOf(UpdateContactState.class));
		// @formatter:on
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<UpdateContactState, UpdateContactEvent> transitions)
			throws Exception {
		// @formatter:off
		transitions
				.withExternal()
					.source(UpdateContactState.START).target(CONTACT_REGISTRY_INITIATED)
					.event(UpdateContactEvent.START)
					.and()

				.withExternal()
					.source(CONTACT_REGISTRY_INITIATED).target(CONTACT_REGISTRY_CHOICE)
					.event(UpdateContactEvent.CONTACT_REGISTRY_INITIATED)
					.action(updateContactAction::update)
					.and()

				.withChoice()
					.source(CONTACT_REGISTRY_CHOICE)
					.first(CONTACT_REGISTRY_SUCCESS, updateRegistryChoice())
					.last(CONTACT_REGISTRY_ERROR)
					.and()

				.withExternal()
					.source(CONTACT_REGISTRY_ERROR).target(END)
					.event(STOP)
					.and()

				.withExternal()
					.source(CONTACT_REGISTRY_SUCCESS).target(UpdateContactState.CONTACT_PERSISTENCE_INITIATED)
					.event(UpdateContactEvent.CONTACT_PERSISTENCE_INITIATED)
					.action(updateContactAction::update)
					.and()

				.withJoin()
					.source(UpdateContactState.CONTACT_PERSISTENCE_INITIATED).target(CONTACT_PERSISTENCE_CHOICE)
					.and()

				.withChoice()
					.source(CONTACT_PERSISTENCE_CHOICE)
					.first(CONTACT_PERSISTENCE_SUCCESS, updatePersistenceChoice())
					.last(CONTACT_PERSISTENCE_ERROR)
					.and()

				.withExternal()
					.source(CONTACT_PERSISTENCE_ERROR).target(END)
					.event(STOP)
					.and()

				.withExternal()
					.source(CONTACT_PERSISTENCE_SUCCESS).target(END)
					.event(STOP);
		// @formatter:on
	}

	@Bean
	public StateMachineService<UpdateContactState, UpdateContactEvent> updateService(
			StateMachineFactory<UpdateContactState, UpdateContactEvent> updateStateMachineFactory,
			StateMachineRuntimePersister<UpdateContactState, UpdateContactEvent, String> updateStateMachineRuntimePersister) {
		return new DefaultStateMachineService<>(updateStateMachineFactory, updateStateMachineRuntimePersister);
	}

}
