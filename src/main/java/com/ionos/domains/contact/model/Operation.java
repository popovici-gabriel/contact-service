package com.ionos.domains.contact.model;

import java.util.Date;
import java.util.StringJoiner;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "operation", indexes = {@Index(name = "operation_type_idx", columnList = "type"),
		@Index(name = "operation_tenant_idx", columnList = "tenant"),
		@Index(name = "operation_created_at", columnList = "created_at"),
		@Index(name = "operation_updated_at", columnList = "updated_at"),
		@Index(name = "correlation_id_idx", columnList = "correlation_id"),
		@Index(name = "external_correlation_id_idx", columnList = "external_correlation_id"),
		@Index(name = "is_active_idx", columnList = "is_active")})
public class Operation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "type", nullable = false, length = 64)
	private Type type;

	@Column(name = "parameters", length = 10485760)
	private String parameters;

	@Column(name = "tenant", nullable = false, length = 64)
	private String tenant;

	@Enumerated(EnumType.STRING)
	@Column(name = "state", nullable = false, length = 64)
	private CreateContactState state;

	@Column(name = "correlation_id", nullable = false)
	private String correlationId;

	@Column(name = "external_correlation_id", nullable = false)
	private String externalCorrelationId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

	@Version
	@Column(name = "version", nullable = false)
	private int version;

	@Column(name = "is_active", nullable = false)
	private boolean isActive;

	@Column(name = "is_running", nullable = false)
	private boolean isRunning;

	@Column(name = "error_job_id")
	private Long errorJobId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public CreateContactState getState() {
		return state;
	}

	public void setState(CreateContactState state) {
		this.state = state;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getExternalCorrelationId() {
		return externalCorrelationId;
	}

	public void setExternalCorrelationId(String externalCorrelationId) {
		this.externalCorrelationId = externalCorrelationId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean running) {
		isRunning = running;
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
		return new StringJoiner(", ", Operation.class.getSimpleName() + "[", "]").add("id=" + id).add("type=" + type)
				.add("parameters='" + parameters + "'").add("tenant=" + tenant).add("state=" + state)
				.add("correlationId='" + correlationId + "'")
				.add("externalCorrelationId='" + externalCorrelationId + "'").add("createdAt=" + createdAt)
				.add("updatedAt=" + updatedAt).add("version=" + version).add("isActive=" + isActive)
				.add("isRunning=" + isRunning).add("errorJobId=" + errorJobId).toString();
		// @formatter:on
	}
}
