package com.ionos.domains.contact.delete;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeleteContactServiceIT {

    @Autowired
    private DeleteContactService deleteContactService;

    @Test
    void test() {
        // given
        final var instanceId = UUID.randomUUID().toString();
        // when
        deleteContactService.delete(instanceId);
        // then
        Assertions.assertThat(deleteContactService.getCurrentState(instanceId)).isSameAs(DeleteContactState.END);
    }

}