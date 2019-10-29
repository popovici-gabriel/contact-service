package com.ionos.domains.contact.resource;

import com.ionos.domains.contact.create.CreateContactService;
import com.ionos.domains.contact.delete.DeleteContactService;
import com.ionos.domains.contact.update.UpdateContactService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    @Autowired
    private CreateContactService createContactService;

    @Autowired
    private UpdateContactService updateContactService;

    @Autowired
    private DeleteContactService deleteContactService;

    @PostMapping
    public String create() {
        return createContactService.create(UUID.randomUUID().toString());
    }

    @PutMapping
    public String update() {
        return updateContactService.update(UUID.randomUUID().toString());
    }

    @DeleteMapping
    public String delete() {
        return deleteContactService.delete(UUID.randomUUID().toString());
    }


}
