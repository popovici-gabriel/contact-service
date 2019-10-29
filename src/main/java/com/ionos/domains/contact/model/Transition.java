package com.ionos.domains.contact.model;

import com.ionos.domains.contact.create.CreateContactEvent;
import com.ionos.domains.contact.create.CreateContactState;
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
