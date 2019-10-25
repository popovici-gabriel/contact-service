package com.ionos.domains.contact.resource;

import static com.ionos.domains.contact.resource.ContactResource.API_CONTACT_PATH;
import static com.ionos.domains.contact.resource.ContactResource.CORRELATION_ID;
import static com.ionos.domains.contact.resource.ContactResource.TENANT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import com.ionos.domains.contact.service.OperationService;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@AutoConfigureMockMvc
@EmbeddedKafka(topics = "contact")
@DirtiesContext
class ContactResourceIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StateMachineService<CreateContactState, CreateContactEvent> stateMachineService;

	@Autowired
	private OperationService operationService;

	@Test
	void testCreateContactResource() throws Exception {
		// @formatter:off
		// when
		final MvcResult mvcResult = mockMvc.perform(post(API_CONTACT_PATH).param(TENANT, "oneandone")
				.param(CORRELATION_ID, UUID.randomUUID().toString()).contentType(APPLICATION_JSON_VALUE).content("{}"))
				.andReturn();
		// @formatter:on

		// then
		assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
		assertThat(mvcResult.getResponse().getContentAsString()).isNotEmpty();
		// cleanup
		stateMachineService.releaseStateMachine(mvcResult.getResponse().getContentAsString());
	}

	@AfterEach
	void cleanUp() {
		operationService.deleteAll();
	}

}
