package com.ionos.domains.contact.update;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UpdateContactServiceTest {

    @Autowired
    private UpdateContactService updateService;

    @Test
    void test() {
        // given
        final var instanceId = UUID.randomUUID().toString();
        // when
        updateService.update(instanceId);
        // then
        Assertions.assertThat(updateService.getCurrentState(instanceId)).isSameAs(UpdateContactState.END);
    }

}