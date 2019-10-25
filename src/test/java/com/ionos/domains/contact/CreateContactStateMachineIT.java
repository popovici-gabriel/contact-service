package com.ionos.domains.contact;

import static org.assertj.core.api.Assertions.assertThat;
import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;

@SpringBootTest
@Disabled
class CreateContactStateMachineIT {

	@Autowired
	private StateMachineService<CreateContactState, CreateContactEvent> stateMachineService;

	private StateMachine<CreateContactState, CreateContactEvent> stateMachine;

	private String machineId;

	@BeforeEach
	void setUp() {
		machineId = UUID.randomUUID().toString();
		stateMachine = stateMachineService.acquireStateMachine(machineId, true);
	}

	@Test
	@DisplayName("Should validate Initial State is START")
	void verifyInitialStateIsStart() {
		assertThat(stateMachine.getInitialState().getId()).isEqualTo(CreateContactState.START);
	}

	@Test
	@DisplayName("Should validate CONTACT_REGISTRY_INITIATED state when START event is send to machine")
	void whenStartEvent_thenContactRegistryInitiated() {
		// when
		stateMachine.sendEvent(CreateContactEvent.START);
		// then
		assertThat(stateMachine.getState().getId()).isEqualTo(CreateContactState.CONTACT_REGISTRY_INITIATED);
		assertThat(stateMachine.isComplete()).isFalse();
	}

	@Test
	@DisplayName("Should validate CONTACT_REGISTRY_ERROR state is end state")
	void whenSendingStartAndContactRegistryError_thenContactRegistryErrorEndState() {
		// given
		stateMachine.sendEvent(CreateContactEvent.START);
		stateMachine.sendEvent(CreateContactEvent.CONTACT_REGISTRY_ERROR);
		// then
		assertThat(stateMachine.getState().getId()).isEqualTo(CreateContactState.CONTACT_REGISTRY_ERROR);
		assertThat(stateMachine.isComplete()).isTrue();
	}

	@AfterEach
	void clean() {
		stateMachineService.releaseStateMachine(machineId);
	}

}
