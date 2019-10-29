package com.ionos.domains.contact.resource;

import com.ionos.domains.contact.create.CreateContactService;
import com.ionos.domains.contact.update.UpdateContactService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    @Autowired
    private CreateContactService createContactService;

    @Autowired
    private UpdateContactService updateContactService;

    @RequestMapping("/create")
    public String create() {
        return createContactService.createContact(UUID.randomUUID().toString());
    }

    @RequestMapping("/update")
    public String update() {
        return updateContactService.updateContact(UUID.randomUUID().toString());
    }

}
