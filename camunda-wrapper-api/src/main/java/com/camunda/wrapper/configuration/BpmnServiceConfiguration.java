package com.camunda.wrapper.configuration;

import com.camunda.wrapper.controller.OperateController;
import com.camunda.wrapper.service.BpmnService;
import com.camunda.wrapper.service.OperateService;
import com.camunda.wrapper.service.impl.BpmnServiceImpl;
import com.camunda.wrapper.service.impl.OperateServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;



import io.camunda.operate.CamundaOperateClient;
import io.camunda.zeebe.client.ZeebeClient;

@Configuration
@Import(value = {
        OperateController.class
})
public class BpmnServiceConfiguration {


    @Bean
    public OperateService operateService(ZeebeClient zeebeClient, CamundaOperateClient camundaOperateClient) {
        return new OperateServiceImpl(zeebeClient, camundaOperateClient);
    }

//	@Bean
//	public CamundaOperateClient camundaOperateClient() {
//		return new CamundaOperateClient();
//	}

    @Bean
    public BpmnService bpmnService() {
        return new BpmnServiceImpl();
    }
}

