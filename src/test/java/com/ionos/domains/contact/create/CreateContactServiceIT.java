package com.ionos.domains.contact.create;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CreateContactServiceIT {

    @Autowired
    private CreateContactService contactService;

    @Test
    void shouldCreateContact() {
        // given
        final var instanceId = UUID.randomUUID().toString();
        // when
        contactService.create(instanceId);
        // then
        Assertions.assertThat(contactService.getCurrentState(instanceId)).isSameAs(CreateContactState.END);
    }
}