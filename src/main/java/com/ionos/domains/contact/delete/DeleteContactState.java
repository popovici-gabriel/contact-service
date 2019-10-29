package com.ionos.domains.contact.delete;

/**
 * A state is a statemachine in which a state machine can be. <br/>
 * <p>
 * Convention over states should follow rule: {object}_{service}_{state} e.g.:
 * {contact/host/domain}_{registry/persistence}_{success/error/initiated}
 */
// @formatter:off
public enum DeleteContactState {
	START,

	DELETE_REGISTRY_INITIATED,
	DELETE_REGISTRY_CHOICE,
	DELETE_REGISTRY_SUCCESS,
	DELETE_REGISTRY_ERROR,

	DELETE_PERSISTENCE_INITIATED,
	DELETE_PERSISTENCE_CHOICE,
	DELETE_PERSISTENCE_SUCCESS,
	DELETE_PERSISTENCE_ERROR,

	END
}
// @formatter:on
