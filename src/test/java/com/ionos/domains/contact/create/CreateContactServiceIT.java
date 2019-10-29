package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.service.StateMachineService;

@SpringBootTest
class CreateContactServiceIT {

    @Autowired
    private CreateContactService contactService;

    @Autowired
    private StateMachineService<CreateContactState, CreateContactEvent> stateMachineService;

    @Test
    void test() throws InterruptedException {
        // given
        final var instanceId = UUID.randomUUID().toString();
        // when
        contactService.startCreateContact(instanceId);
        // then
        Assertions.assertThat(contactService.getCurrentState(instanceId)).isSameAs(CreateContactState.END);
    }
}