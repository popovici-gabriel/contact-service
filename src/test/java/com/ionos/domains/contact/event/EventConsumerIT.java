package com.ionos.domains.contact.event;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import com.ionos.domains.contact.model.CreateContactEvent;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@EmbeddedKafka(topics = "contact")
@DirtiesContext
class EventConsumerIT {

	@Autowired
	private EventConsumer consumer;

	@Autowired
	private EventProducer producer;

	@Test
	void testEventConsumer() throws Exception {
		// given
		Event event = new Event();
		event.setOperationId(UUID.randomUUID().toString());
		event.setCreateContactEvent(CreateContactEvent.CONTACT_REGISTRY_ERROR);
		event.setErrorJobId(1_234L);
		// when
		producer.send(event);

		// than
		consumer.getCountDownLatch().await(10, TimeUnit.SECONDS);
		assertThat(consumer.getCountDownLatch().getCount()).isEqualTo(0);
	}

}
