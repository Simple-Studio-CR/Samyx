package com.samyx;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.samyx.MyEApplication;

public class ServletInitializer extends SpringBootServletInitializer {
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(new Class[] { MyEApplication.class });
	}
}
