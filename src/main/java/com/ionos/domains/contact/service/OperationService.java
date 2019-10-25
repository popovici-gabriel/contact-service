package com.ionos.domains.contact.service;

import static java.util.Objects.requireNonNull;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ionos.domains.contact.model.Operation;

@Service
@Transactional
public class OperationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperationService.class);

	private final OperationRepository operationRepository;

	@Autowired
	public OperationService(OperationRepository operationRepository) {
		this.operationRepository = requireNonNull(operationRepository);
	}

	@Transactional(readOnly = true)
	public Operation getActiveOperation(final String operationId) {
		return operationRepository.findByCorrelationIdAndIsActiveTrue(operationId);
	}

	public Operation insert(Operation operation) {
		LOGGER.info("About to add operation {}", operation);
		requireNonNull(operation);
		return operationRepository.save(operation);
	}

	public void deleteAll() {
		LOGGER.info("About to delete all operation entries");
		operationRepository.deleteAll();
	}

	public Operation updateRunningFlag(String operationId) {
		final var operation = getActiveOperation(operationId);
		operation.setRunning(false);
		return operationRepository.save(operation);
	}

	public Operation deactivateOperation(final String operationId) {
		final var operation = getActiveOperation(operationId);
		operation.setUpdatedAt(new Date());
		operation.setActive(false);
		return operationRepository.save(operation);
	}

	@Transactional(readOnly = true)
	public List<Operation> getOperations(String operationId) {
		return operationRepository.findByCorrelationId(operationId);
	}
}
