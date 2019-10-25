package com.ionos.domains.contact.event;

import static java.util.Objects.requireNonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

	private final KafkaTemplate<Object, Event> kafkaTemplate;

	@Autowired
	public EventProducer(KafkaTemplate<Object, Event> kafkaTemplate) {
		this.kafkaTemplate = requireNonNull(kafkaTemplate);
	}

	public void send(Event message) {
		this.kafkaTemplate.send("contact", message);
	}
}
