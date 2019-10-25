package com.ionos.domains.contact.event;

import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.ionos.domains.contact.service.ContactService;

@Component
public class EventConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);

	private final ContactService contactService;

	private final CountDownLatch countDownLatch;

	@Autowired
	public EventConsumer(ContactService contactService) {
		this.contactService = contactService;
		countDownLatch = new CountDownLatch(1);
	}

	@KafkaListener(topics = "contact", groupId = "contact")
	public void consumeEvent(Event event) {
		countDownLatch.countDown();
		LOGGER.info("About to process event {}", event);
		if (event != null && event.getOperationId() != null && event.getCreateContactEvent() != null) {
			contactService.processEvent(event);
		}
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}
}
