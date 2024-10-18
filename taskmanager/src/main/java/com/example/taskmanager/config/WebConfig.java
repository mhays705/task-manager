package com.example.taskmanager.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 * Configuration class for web application settings.
 *
 * This class contains bean definitions and other configuration settings
 * that help in setting up the web application context.
 */
@Configuration
public class WebConfig {



	/**
	 * Creates a new instance of HiddenHttpMethodFilter.
	 *
	 * The HiddenHttpMethodFilter allows HTTP methods like PUT and DELETE to be
	 * sent via browser forms by using a hidden input field.
	 *
	 * @return a HiddenHttpMethodFilter bean used to support HTTP methods not
	 * natively supported by HTML forms.
	 */
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
}
