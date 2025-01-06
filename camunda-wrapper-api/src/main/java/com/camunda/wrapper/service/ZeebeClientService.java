package com.camunda.wrapper.service;

import io.camunda.zeebe.client.api.response.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ZeebeClientService {
    DeploymentEvent deploy(MultipartFile bpmnFile) throws IOException;

    PublishMessageResponse publishMessage(String messageName, String correlationKey, @RequestBody Map<String, Object> variables);

    ProcessInstanceEvent startProcessInstance(String bpmnProcessId, Map<String, Object> variables);

    DeleteResourceResponse deleteResource(Long resourceKey);

    CancelProcessInstanceResponse cancelProcessInstance(Long processInstanceKey);
}
