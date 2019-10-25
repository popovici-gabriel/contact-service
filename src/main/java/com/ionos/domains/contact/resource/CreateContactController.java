package com.ionos.domains.contact.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CreateContactController {

	@RequestMapping("/create")
	public String create() {
		return null;
	}
}
