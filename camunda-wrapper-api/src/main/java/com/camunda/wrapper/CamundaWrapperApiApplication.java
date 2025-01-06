package com.camunda.wrapper;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
//import io.camunda.zeebe.spring.client.config.ZeebeClientStarterAutoConfiguration;
//@Import(ZeebeClientStarterAutoConfiguration.class)

@Deployment(resources =  {"classpath*:*.bpmn", "classpath*:*.dmn"})
@SpringBootApplication
public class CamundaWrapperApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaWrapperApiApplication.class, args);
	}

}

