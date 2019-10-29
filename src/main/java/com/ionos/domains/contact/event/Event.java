package com.ionos.domains.contact.event;

import java.util.Date;
import java.util.StringJoiner;
import com.ionos.domains.contact.create.CreateContactEvent;

public class Event {

	private String operationId;

	private CreateContactEvent createContactEvent;

	private final Date date;

	private Long errorJobId;

	public Event() {
		date = new Date();
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public CreateContactEvent getCreateContactEvent() {
		return createContactEvent;
	}

	public void setCreateContactEvent(CreateContactEvent createContactEvent) {
		this.createContactEvent = createContactEvent;
	}

	public Date getDate() {
		return date;
	}

	public Long getErrorJobId() {
		return errorJobId;
	}

	public void setErrorJobId(Long errorJobId) {
		this.errorJobId = errorJobId;
	}

	@Override
	public String toString() {
		// @formatter:off
		return new StringJoiner(", ", Event.class.getSimpleName() + "[", "]").add("operationId='" + operationId + "'")
				.add("createContactEvent=" + createContactEvent).add("date=" + date).add("errorJobId=" + errorJobId)
				.toString();
		// @formatter:on
	}
}
