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

	CONTACT_REGISTRY_INITIATED,
	CONTACT_REGISTRY_CHOICE,
	CONTACT_REGISTRY_SUCCESS,
	CONTACT_REGISTRY_ERROR,

	CONTACT_PERSISTENCE_INITIATED,
	CONTACT_PERSISTENCE_CHOICE,
	CONTACT_PERSISTENCE_SUCCESS,
	CONTACT_PERSISTENCE_ERROR,

	END
}
// @formatter:on
