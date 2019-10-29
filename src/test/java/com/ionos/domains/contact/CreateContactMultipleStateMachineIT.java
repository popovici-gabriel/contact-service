package com.ionos.domains.contact;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.service.StateMachineService;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Disabled
class CreateContactMultipleStateMachineIT {

    @Autowired
    private StateMachineService<CreateContactState, CreateContactEvent> stateMachineService;

    @Test
    void givenMachineId_whenAcquireStateMachineTwice_thenSameMachine() {
        // given
        final var machine1 = UUID.randomUUID().toString();
        final var stateMachine1 = stateMachineService.acquireStateMachine(machine1, true);
        final var stateMachine2 = stateMachineService.acquireStateMachine(machine1, true);

        assertThat(stateMachine1.getUuid()).isEqualTo(stateMachine2.getUuid());
        assertThat(stateMachine1.isComplete()).isFalse();
    }

    @Test
    void testMultipleStateMachines() {
        // given
        final var machine1 = UUID.randomUUID().toString();
        final var machine2 = UUID.randomUUID().toString();
        final var machine3 = UUID.randomUUID().toString();

        final var stateMachine1 = stateMachineService.acquireStateMachine(machine1, true);
        final var stateMachine2 = stateMachineService.acquireStateMachine(machine2, true);
        final var stateMachine3 = stateMachineService.acquireStateMachine(machine3, true);

        // when
        stateMachine1.sendEvent(CreateContactEvent.START);
        stateMachine1.sendEvent(CreateContactEvent.CONTACT_REGISTRY_ERROR);

        stateMachine2.sendEvent(CreateContactEvent.START);
        stateMachine2.sendEvent(CreateContactEvent.CONTACT_REGISTRY_SUCCESS);
        stateMachine2.sendEvent(CreateContactEvent.CONTACT_PERSISTENCE_INITIATED);
        stateMachine2.sendEvent(CreateContactEvent.CONTACT_PERSISTENCE_SUCCESS);
        stateMachine2.sendEvent(CreateContactEvent.STOP);

        stateMachine3.sendEvent(CreateContactEvent.START);
        stateMachine3.sendEvent(CreateContactEvent.CONTACT_REGISTRY_SUCCESS);
        stateMachine3.sendEvent(CreateContactEvent.CONTACT_PERSISTENCE_INITIATED);
        stateMachine3.sendEvent(CreateContactEvent.CONTACT_PERSISTENCE_ERROR);

        // then
        assertThat(stateMachine1.getState().getId()).isEqualTo(CreateContactState.CONTACT_REGISTRY_ERROR);
        assertThat(stateMachine1.isComplete()).isTrue();

        assertThat(stateMachine2.getState().getId()).isEqualTo(CreateContactState.END);
        assertThat(stateMachine2.isComplete()).isTrue();

        assertThat(stateMachine3.getState().getId()).isEqualTo(CreateContactState.CONTACT_PERSISTENCE_ERROR);
        assertThat(stateMachine3.isComplete()).isTrue();

        // clean up
        stateMachineService.releaseStateMachine(machine1);
        stateMachineService.releaseStateMachine(machine2);
        stateMachineService.releaseStateMachine(machine3);

    }

}
