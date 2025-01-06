package com.camunda.wrapper.service.impl;

import com.camunda.wrapper.service.ZeebeClientService;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.util.Map;

public class ZeebeClientServiceImpl implements ZeebeClientService {
    private static final Logger LOG = LoggerFactory.getLogger(ZeebeClientServiceImpl.class);
    private final ZeebeClient zeebeClient;

    public ZeebeClientServiceImpl(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @Override
    public DeploymentEvent deploy(MultipartFile bpmnFile) throws IOException {
        return zeebeClient.newDeployResourceCommand()
                .addResourceBytes(bpmnFile.getBytes(), bpmnFile.getResource().getFilename()).send().join();
    }

    @Override
    public PublishMessageResponse publishMessage(String messageName, String correlationKey, @RequestBody Map<String, Object> variables) {
        LOG.info("Publishing message `{}` with correlation key `{}` and variables: {}", messageName, correlationKey,
                variables);
        return zeebeClient.newPublishMessageCommand().messageName(messageName).correlationKey(correlationKey)
                .variables(variables).send().join();
    }

    @Override
    public ProcessInstanceEvent startProcessInstance(String bpmnProcessId, Map<String, Object> variables) {
        LOG.info("Starting process {} with variables {}", bpmnProcessId, variables);
        return zeebeClient.newCreateInstanceCommand().bpmnProcessId(bpmnProcessId).latestVersion().variables(variables)
                .send().join();
    }

    public DeleteResourceResponse deleteResource(Long resourceKey) {
        LOG.info("Deleting resource with key {}", resourceKey);
        return zeebeClient.newDeleteResourceCommand(resourceKey).send().join();
    }

    public CancelProcessInstanceResponse cancelProcessInstance(Long processInstanceKey) {
        LOG.info("Cancelling resource with key {}", processInstanceKey);
        return zeebeClient.newCancelInstanceCommand(processInstanceKey.longValue()).send().join();
    }
}
