package com.camunda.wrapper.configuration;

import com.camunda.wrapper.controller.ZeebeClientController;
import com.camunda.wrapper.service.ZeebeClientService;
import com.camunda.wrapper.service.impl.ZeebeClientServiceImpl;
import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import(ZeebeClientController.class)
public class ZeebeClientConfiguration {

    @Bean
    public ZeebeClientService zeebeClientService(ZeebeClient zeebeClient) {
        return new ZeebeClientServiceImpl(zeebeClient);
    }
}

