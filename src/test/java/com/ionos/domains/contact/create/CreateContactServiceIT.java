package com.ionos.domains.contact.create;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.statemachine.service.StateMachineService;

import java.util.UUID;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@EmbeddedKafka
class CreateContactServiceIT {

    @Autowired
    private CreateContactService contactService;

    @Autowired
    private StateMachineService<CreateContactState, CreateContactEvent> stateMachineService;

    @Test
    void test() throws InterruptedException {
        final var instanceId = UUID.randomUUID().toString();
        contactService.startCreateContact(instanceId);
        Thread.sleep(4_000);
        System.out.println("stateMachineService = " + stateMachineService.acquireStateMachine(instanceId).getState().getId());
    }
}