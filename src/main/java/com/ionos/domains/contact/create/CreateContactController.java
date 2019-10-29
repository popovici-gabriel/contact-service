package com.ionos.domains.contact.create;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateContactController {

    @Autowired
    private CreateContactService createContactService;

    @RequestMapping("/create")
    public String create() {
        return createContactService.createContact(UUID.randomUUID().toString());
    }
}
