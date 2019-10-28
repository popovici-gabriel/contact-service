package com.ionos.domains.contact.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transition {

    private CreateContactState to;

    private CreateContactState from;

    private String instanceId;

    private CreateContactEvent event;
}
