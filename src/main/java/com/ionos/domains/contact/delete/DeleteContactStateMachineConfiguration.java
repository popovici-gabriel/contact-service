package com.ionos.domains.contact.delete;

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
import static com.ionos.domains.contact.delete.DeleteChoiceGuard.deletePersistenceChoice;
import static com.ionos.domains.contact.delete.DeleteChoiceGuard.deleteRegistryChoice;
import static com.ionos.domains.contact.delete.DeleteContactState.DELETE_PERSISTENCE_CHOICE;
import static com.ionos.domains.contact.delete.DeleteContactState.DELETE_PERSISTENCE_ERROR;
import static com.ionos.domains.contact.delete.DeleteContactState.DELETE_PERSISTENCE_INITIATED;
import static com.ionos.domains.contact.delete.DeleteContactState.DELETE_PERSISTENCE_SUCCESS;
import static com.ionos.domains.contact.delete.DeleteContactState.DELETE_REGISTRY_CHOICE;
import static com.ionos.domains.contact.delete.DeleteContactState.DELETE_REGISTRY_ERROR;
import static com.ionos.domains.contact.delete.DeleteContactState.DELETE_REGISTRY_INITIATED;
import static com.ionos.domains.contact.delete.DeleteContactState.DELETE_REGISTRY_SUCCESS;
import static com.ionos.domains.contact.delete.DeleteContactState.END;
import static com.ionos.domains.contact.delete.DeleteContactState.START;

@Configuration
@EnableStateMachineFactory(name = "deleteStateMachineFactory")
public class DeleteContactStateMachineConfiguration
		extends
			EnumStateMachineConfigurerAdapter<DeleteContactState, DeleteContactEvent> {

	@Autowired
	private StateMachineRuntimePersister<DeleteContactState, DeleteContactEvent, String> deleteStateMachineRuntimePersister;

	@Autowired
	private DeleteContactAction deleteContactAction;

	@Override
	public void configure(StateMachineConfigurationConfigurer<DeleteContactState, DeleteContactEvent> config)
			throws Exception {
		// @formatter:off

		config
				.withPersistence()
				.runtimePersister(deleteStateMachineRuntimePersister);

		config
				.withConfiguration()
				.machineId("delete-contact")
				.autoStartup(false)
				.listener(new LifecycleStateAdapter());
		// @formatter:on
	}

	@Override
	public void configure(StateMachineStateConfigurer<DeleteContactState, DeleteContactEvent> states) throws Exception {
		// @formatter:off
		states
				.withStates()
				.initial(START)
				.state(DELETE_REGISTRY_INITIATED)
				.choice(DELETE_REGISTRY_CHOICE)
				.state(DELETE_PERSISTENCE_INITIATED)
				.choice(DELETE_PERSISTENCE_CHOICE)
				.end(END)
				.states(EnumSet.allOf(DeleteContactState.class));
		// @formatter:on
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<DeleteContactState, DeleteContactEvent> transitions)
			throws Exception {
		// @formatter:off
		transitions
				.withExternal()
					.source(START).target(DELETE_REGISTRY_INITIATED)
					.event(DeleteContactEvent.START)
					.and()

				.withExternal()
					.source(DELETE_REGISTRY_INITIATED).target(DELETE_REGISTRY_CHOICE)
					.event(DeleteContactEvent.CONTACT_REGISTRY_INITIATED)
					.action(deleteContactAction::delete)
					.and()

				.withChoice()
					.source(DELETE_REGISTRY_CHOICE)
					.first(DELETE_REGISTRY_SUCCESS, deleteRegistryChoice())
					.last(DELETE_REGISTRY_ERROR)
					.and()

				.withExternal()
					.source(DELETE_REGISTRY_ERROR).target(END)
					.event(DeleteContactEvent.STOP)
					.and()

				.withExternal()
					.source(DELETE_REGISTRY_SUCCESS).target(DELETE_PERSISTENCE_INITIATED)
					.event(DeleteContactEvent.CONTACT_PERSISTENCE_INITIATED)
					.action(deleteContactAction::delete)
					.and()

				.withJoin()
					.source(DELETE_PERSISTENCE_INITIATED).target(DELETE_PERSISTENCE_CHOICE)
					.and()

				.withChoice()
					.source(DELETE_PERSISTENCE_CHOICE)
					.first(DELETE_PERSISTENCE_SUCCESS, deletePersistenceChoice())
					.last(DELETE_PERSISTENCE_ERROR)
					.and()

				.withExternal()
					.source(DELETE_PERSISTENCE_ERROR).target(END)
					.event(DeleteContactEvent.STOP)
					.and()

				.withExternal()
					.source(DELETE_PERSISTENCE_SUCCESS).target(END)
					.event(DeleteContactEvent.STOP);
		// @formatter:on
	}

	@Bean
	public StateMachineService<DeleteContactState, DeleteContactEvent> deleteService(
			StateMachineFactory<DeleteContactState, DeleteContactEvent> deleteStateMachineFactory,
			StateMachineRuntimePersister<DeleteContactState, DeleteContactEvent, String> deleteStateMachineRuntimePersister) {
		return new DefaultStateMachineService<>(deleteStateMachineFactory, deleteStateMachineRuntimePersister);
	}

}
