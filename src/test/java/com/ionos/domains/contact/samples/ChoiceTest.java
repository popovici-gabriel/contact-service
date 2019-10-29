package com.ionos.domains.contact.samples;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = ChoiceStateMachine.class)
@SpringBootTest
public class ChoiceTest {

    @Autowired
    private StateMachine<CreateContactState, CreateContactEvent> stateMachine;

    @BeforeEach
    void setUp() {
        stateMachine.start();
    }

    @AfterEach
    void tearDown() {
        stateMachine.stop();
    }

    @Test
    void shouldBeSame() {
        boolean success = stateMachine.sendEvent(CreateContactEvent.START);
        Assertions.assertTrue(success);
        stateMachine.sendEvent(CreateContactEvent.START);
        Assertions.assertTrue(success);
        Assertions.assertSame(stateMachine.getState().getId(), CreateContactState.CONTACT_REGISTRY_ERROR);
    }
}
