package com.ionos.domains.contact.update;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateContactController {

    @Autowired
    private UpdateContactService updateContactService;

    @RequestMapping("/update")
    public String update() {
        return updateContactService.updateContact(UUID.randomUUID().toString());
    }
}
