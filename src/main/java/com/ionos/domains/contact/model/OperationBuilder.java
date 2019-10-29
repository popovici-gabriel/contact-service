package com.ionos.domains.contact.model;

import static java.util.Optional.ofNullable;
import com.ionos.domains.contact.create.CreateContactState;
import java.util.Date;
import java.util.UUID;

public final class OperationBuilder {
	private Type type;
	private String parameters;
	private String tenant;
	private CreateContactState state;
	private String correlationId;
	private String externalCorrelationId;
	private Long errorJobId;

	private OperationBuilder() {
	}

	public static OperationBuilder builder() {
		return new OperationBuilder();
	}

	public OperationBuilder type(Type type) {
		this.type = type;
		return this;
	}

	public OperationBuilder parameters(String parameters) {
		this.parameters = parameters;
		return this;
	}

	public OperationBuilder tenant(String tenant) {
		this.tenant = tenant;
		return this;
	}

	public OperationBuilder state(CreateContactState state) {
		this.state = state;
		return this;
	}

	public OperationBuilder correlationId(String correlationId) {
		this.correlationId = correlationId;
		return this;
	}

	public OperationBuilder externalCorrelationId(String externalCorrelationId) {
		this.externalCorrelationId = externalCorrelationId;
		return this;
	}

	public OperationBuilder errorJobId(Long errorJobId) {
		this.errorJobId = errorJobId;
		return this;
	}

	public Operation build() {
		Operation operation = new Operation();
		operation.setCreatedAt(new Date());
		operation.setType(ofNullable(type).orElseThrow(() -> new IllegalArgumentException("Type must not be empty")));
		operation.setParameters(
				ofNullable(parameters).orElseThrow(() -> new IllegalArgumentException("Parameters must not be empty")));
		operation.setTenant(
				ofNullable(tenant).orElseThrow(() -> new IllegalArgumentException("Tenant must not be empty")));
		operation.setState(ofNullable(state).orElseThrow(() -> new IllegalArgumentException("State must not ne null")));
		operation.setCorrelationId(ofNullable(correlationId).orElse(computeCorrelationId()));
		operation.setExternalCorrelationId(ofNullable(externalCorrelationId)
				.orElseThrow(() -> new IllegalArgumentException("External correlation id must not be empty")));
		operation.setErrorJobId(errorJobId);
		operation.setActive(true);
		operation.setRunning(true);
		return operation;
	}

	private String computeCorrelationId() {
		return UUID.randomUUID().toString();
	}
}
