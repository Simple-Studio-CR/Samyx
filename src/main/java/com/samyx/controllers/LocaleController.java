package com.samyx.controllers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocaleController {
	@GetMapping({ "/locale" })
	public String locale(HttpServletRequest request) {
		String ultimaUrl = request.getHeader("referer");
		System.out.println("AG_____________ " + ultimaUrl);
		return "redirect:".concat(ultimaUrl);
	}
}
