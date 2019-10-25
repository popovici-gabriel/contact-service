package com.ionos.domains.contact.configuration;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

@Configuration
public class JpaCreateContactStateMachineConfiguration {

	@Bean
	public StateMachineRuntimePersister<CreateContactState, CreateContactEvent, String> stateMachineRuntimePersister(
			JpaStateMachineRepository jpaStateMachineRepository) {
		return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
	}
}
