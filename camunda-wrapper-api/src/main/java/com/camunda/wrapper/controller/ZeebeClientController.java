package com.camunda.wrapper.controller;

import java.io.IOException;
import java.util.Map;

import com.camunda.wrapper.annotations.ValidBPMNFile;
import com.camunda.wrapper.service.ZeebeClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;

@RestController
public class ZeebeClientController {

    private final ZeebeClientService zeebeClientService;



    public ZeebeClientController(ZeebeClientService zeebeClientService) {
        super();
        this.zeebeClientService = zeebeClientService;
    }

    @PostMapping(path = "/deploy", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<DeploymentEvent>  deploy(@RequestParam("file") @ValidBPMNFile MultipartFile bpmnFile) throws IOException{
        return new ResponseEntity<>(zeebeClientService.deploy(bpmnFile), HttpStatus.OK);

    }

    @PostMapping("/message/{messageName}/{correlationKey}")

    public void publishMessage(@PathVariable String messageName, @PathVariable String correlationKey,
                               @RequestBody Map<String, Object> variables) {
        zeebeClientService.publishMessage(messageName, correlationKey, variables);
    }

    @PostMapping("/{bpmnProcessId}/start")

    public ResponseEntity<ProcessInstanceEvent>  startProcessInstance(@PathVariable String bpmnProcessId,
                                                                      @RequestBody Map<String, Object> variables){

        return new ResponseEntity<>(zeebeClientService.startProcessInstance(bpmnProcessId, variables), HttpStatus.OK);
    }


    @PatchMapping("/{resourceKey}/cancelResource")

    public void cancelProcessInstance(Long processInstanceKey) {
        zeebeClientService.cancelProcessInstance(processInstanceKey);
    }

}
