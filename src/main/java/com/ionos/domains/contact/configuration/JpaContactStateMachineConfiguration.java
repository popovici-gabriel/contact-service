package com.ionos.domains.contact.configuration;

import com.ionos.domains.contact.create.CreateContactEvent;
import com.ionos.domains.contact.create.CreateContactState;
import com.ionos.domains.contact.delete.DeleteContactEvent;
import com.ionos.domains.contact.delete.DeleteContactState;
import com.ionos.domains.contact.update.UpdateContactEvent;
import com.ionos.domains.contact.update.UpdateContactState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

@Configuration
public class JpaContactStateMachineConfiguration {

    @Bean
    public StateMachineRuntimePersister<CreateContactState, CreateContactEvent, String> createStateMachineRuntimePersister(
            JpaStateMachineRepository jpaStateMachineRepository) {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

    @Bean
    public StateMachineRuntimePersister<UpdateContactState, UpdateContactEvent, String> updateStateMachineRuntimePersister(
            JpaStateMachineRepository jpaStateMachineRepository) {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

    @Bean
    public StateMachineRuntimePersister<DeleteContactState, DeleteContactEvent, String> deleteStateMachineRuntimePersister(
            JpaStateMachineRepository jpaStateMachineRepository) {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }
}
