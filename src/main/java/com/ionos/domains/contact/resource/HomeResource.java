package com.ionos.domains.contact.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeResource {

	@RequestMapping("/")
	public String swaggerApi() {
		return "redirect:swagger-ui.html";
	}

}
