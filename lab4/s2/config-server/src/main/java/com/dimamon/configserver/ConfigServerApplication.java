package com.dimamon.configserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

//@RestController
//class SSS {
//
//	@Autowired
//	private Environment env;
//
//	@GetMapping("/**")
//	public Map<String, Object> getAllProperties() {
//		Map<String, Object> properties = new HashMap<>();
//
//		if (env instanceof ConfigurableEnvironment) {
//			ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) env;
//
//			for (PropertySource<?> propertySource : configurableEnvironment.getPropertySources()) {
//				if (propertySource instanceof EnumerablePropertySource) {
//					EnumerablePropertySource<?> enumerablePropertySource = (EnumerablePropertySource<?>) propertySource;
//					for (String propertyName : enumerablePropertySource.getPropertyNames()) {
//						properties.put(propertyName, env.getProperty(propertyName));
//					}
//				}
//			}
//		}
//
//		return properties;
//	}
//}

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}
}
