package com.ionos.domains.contact.create;

/**
 * A state is a statemachine in which a state machine can be. <br/>
 * <p>
 * Convention over states should follow rule: {object}_{service}_{state} e.g.:
 * {contact/host/domain}_{registry/persistence}_{success/error/initiated}
 */
// @formatter:off
public enum CreateContactState {
	START,

	CREATE_REGISTRY_INITIATED,
	CREATE_REGISTRY_CHOICE,
	CREATE_REGISTRY_SUCCESS,
	CREATE_REGISTRY_ERROR,

	CREATE_PERSISTENCE_INITIATED,
	CREATE_PERSISTENCE_CHOICE,
	CREATE_PERSISTENCE_SUCCESS,
	CREATE_PERSISTENCE_ERROR,

	END
}
// @formatter:on
