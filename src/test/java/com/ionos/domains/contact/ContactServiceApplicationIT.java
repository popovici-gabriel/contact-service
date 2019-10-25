package com.ionos.domains.contact;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.statemachine.service.StateMachineService;
import com.ionos.domains.contact.service.OperationService;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@EmbeddedKafka(topics = "contact")
@DirtiesContext
public class ContactServiceApplicationIT {

	@Test
	void contextLoads(@Autowired ApplicationContext applicationContext) {
		assertThat(applicationContext.getBean(StateMachineService.class)).isNotNull();
		assertThat(applicationContext.getBean(OperationService.class)).isNotNull();
	}

}
