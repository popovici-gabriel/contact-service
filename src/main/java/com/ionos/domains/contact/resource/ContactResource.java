package com.ionos.domains.contact.resource;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.ionos.domains.contact.model.Operation;
import com.ionos.domains.contact.service.ContactService;
import com.ionos.domains.contact.service.OperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = ContactResource.API_CONTACT_PATH, produces = APPLICATION_JSON_VALUE)
@Api(value = ContactResource.API_CONTACT_PATH)
public class ContactResource {

	private final static Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

	static final String API_CONTACT_PATH = "/api/contact";

	static final String OPERATION_ID_PARAM = "operationId";

	static final String OPERATION_ID_PATH = "/{" + OPERATION_ID_PARAM + "}";

	static final String JOB_TYPE_PARAM_NAME = "jobType";

	static final String TENANT = "tenant";

	static final String CORRELATION_ID = "correlationId";

	static final String PAGE = "page";

	static final String SIZE = "size";

	private final ContactService contactService;

	private final OperationService operationService;

	@Autowired
	public ContactResource(ContactService contactService, OperationService operationService) {
		this.contactService = requireNonNull(contactService);
		this.operationService = requireNonNull(operationService);
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	@ApiOperation(value = "CreateContactProcess", response = String.class)
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid payload"),
			@ApiResponse(code = 201, message = "Contact process created", response = String.class)})
	public ResponseEntity<String> createContact(
			@ApiParam(value = "Tenant", required = true) @RequestParam(value = TENANT) String tenant,
			@ApiParam(value = "Correlation ID", required = true) @RequestParam(value = CORRELATION_ID) String externalCorrelationId,
			@ApiParam(value = "Parameters", required = true) @Valid @RequestBody String parameters,
			HttpServletRequest request) {
		// @formatter:off
		if (parameters == null) {
			throw new IllegalArgumentException("Parameters must not be empty");
		}

		if (tenant == null) {
			throw new IllegalArgumentException("Tenant must not be empty");
		}

		if (externalCorrelationId == null) {
			throw new IllegalArgumentException("External correlation id  must not be empty");
		}

		final Operation operation = contactService.createContactOperation(parameters, tenant, externalCorrelationId);

		return ResponseEntity
				.created(buildURI(request, operation.getCorrelationId()))
				.body(operation.getCorrelationId());
		// @formatter:on
	}

	@GetMapping(value = OPERATION_ID_PATH)
	@ApiOperation(value = "GetOperationsById", response = List.class)
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"),
			@ApiResponse(code = 404, message = "Operation not found")})
	public ResponseEntity<List<Operation>> getOperations(
			@ApiParam(value = "ID of the operation that needs to be fetched", required = true) @PathVariable(name = OPERATION_ID_PARAM) String operationId)
			throws ResourceNotFoundException {
		LOGGER.info("About to fetch operationId [{}]", operationId);
		if (operationId == null) {
			throw new IllegalArgumentException("Operation id must not be null");
		}

		// @formatter:off
		return ok(Optional.ofNullable(operationService.getOperations(operationId))
				.orElseThrow(() -> new ResourceNotFoundException(format("Operation Id %d not found", operationId))));
		// @formatter:on
	}

	private URI buildURI(HttpServletRequest httpServletRequest, String operationId) {
		// @formatter:off
		return ServletUriComponentsBuilder.fromContextPath(httpServletRequest).path(OPERATION_ID_PATH)
				.buildAndExpand(operationId).toUri();
		// @formatter:on
	}

}
