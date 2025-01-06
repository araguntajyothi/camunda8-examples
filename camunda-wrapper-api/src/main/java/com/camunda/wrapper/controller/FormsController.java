package com.camunda.wrapper.controller;

import java.io.IOException;
import java.util.Map;

import com.camunda.wrapper.service.BpmnService;
import com.camunda.wrapper.service.TaskListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import io.camunda.operate.exception.OperateException;
import io.camunda.operate.util.JsonUtils;
import io.camunda.tasklist.exception.TaskListException;
import io.swagger.v3.core.util.Json;

@RequestMapping("/v1/forms")
@RestController
public class FormsController {
    private final BpmnService bpmnService;
    private final TaskListService taskListService;



    public FormsController(BpmnService bpmnService, TaskListService taskListService) {
        super();
        this.bpmnService = bpmnService;
        this.taskListService = taskListService;
    }

    @GetMapping("/{processName}/{processDefinitionId}/{formKey}")
    public JsonNode getFormSchema(@PathVariable String processName, @PathVariable String processDefinitionId,
                                  @PathVariable String formKey) throws IOException, NumberFormatException, OperateException{
        JsonNode formSchema = null;
        if (formKey.startsWith("camunda-forms:bpmn:")) {
            String formId = formKey.substring(formKey.lastIndexOf(":") + 1);
            String schema = bpmnService.getEmbeddedFormSchema(processName, processDefinitionId, formId);
            formSchema = JsonUtils.toJsonNode(schema);
            return formSchema;
        }
        return formSchema;

    }

    @GetMapping("/{formId}")
    public String getForm(@PathVariable String formId, @RequestParam String processDefinitionId) throws TaskListException{
        return taskListService.getForm(formId, processDefinitionId);

    }

}

