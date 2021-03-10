package com.samyx.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerErrorController {
	@ExceptionHandler({ Exception.class })
	public String handleError405(Model model, Exception e) {
		return "redirect:/login/?expired";
	}
}
