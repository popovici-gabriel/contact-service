package com.ionos.domains.contact.resource;

import com.ionos.domains.contact.create.CreateContactService;
import com.ionos.domains.contact.delete.DeleteContactService;
import com.ionos.domains.contact.update.UpdateContactService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/contacts", produces = APPLICATION_JSON_VALUE)
public class ContactController {

    @Autowired
    private CreateContactService createContactService;

    @Autowired
    private UpdateContactService updateContactService;

    @Autowired
    private DeleteContactService deleteContactService;

    @PostMapping
    public InstanceId create() {
        return InstanceId.builder().id(createContactService.create(UUID.randomUUID().toString())).build();
    }

    @PutMapping
    public InstanceId update() {
        return InstanceId.builder().id(updateContactService.update(UUID.randomUUID().toString())).build();
    }

    @DeleteMapping
    public InstanceId delete() {
        return InstanceId.builder().id(deleteContactService.delete(UUID.randomUUID().toString())).build();
    }


}
