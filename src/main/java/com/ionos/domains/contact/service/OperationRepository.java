package com.ionos.domains.contact.service;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.ionos.domains.contact.model.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long>, JpaSpecificationExecutor<Operation> {

	Operation findByCorrelationIdAndIsActiveTrue(String correlationId);

	List<Operation> findByCorrelationId(String correlationId);
}
