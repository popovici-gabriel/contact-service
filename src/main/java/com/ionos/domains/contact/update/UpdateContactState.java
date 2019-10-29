package com.ionos.domains.contact.update;

/**
 * A state is a statemachine in which a state machine can be. <br/>
 * <p>
 * Convention over states should follow rule: {object}_{service}_{state} e.g.:
 * {contact/host/domain}_{registry/persistence}_{success/error/initiated}
 */
// @formatter:off
public enum UpdateContactState {
	START,

	UPDATE_REGISTRY_INITIATED,
	UPDATE_REGISTRY_CHOICE,
	UPDATE_REGISTRY_SUCCESS,
	UPDATE_REGISTRY_ERROR,

	UPDATE_PERSISTENCE_INITIATED,
	UPDATE_PERSISTENCE_CHOICE,
	UPDATE_PERSISTENCE_SUCCESS,
	UPDATE_PERSISTENCE_ERROR,

	END
}
// @formatter:on
