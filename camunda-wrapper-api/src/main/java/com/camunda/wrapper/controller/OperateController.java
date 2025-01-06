package com.camunda.wrapper.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.camunda.wrapper.service.BpmnService;
import com.camunda.wrapper.service.OperateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.camunda.operate.dto.FlownodeInstance;
import io.camunda.operate.dto.ProcessDefinition;
import io.camunda.operate.dto.ProcessInstance;
import io.camunda.operate.dto.Variable;
import io.camunda.operate.exception.OperateException;
import io.camunda.operate.search.FlownodeInstanceFilter;
import io.camunda.operate.search.ProcessDefinitionFilter;
import io.camunda.operate.search.ProcessInstanceFilter;
import io.camunda.operate.search.VariableFilter;

@RestController
@RequestMapping("/v1")
public class OperateController {

    private final BpmnService bpmnService;

    private final OperateService operateService;


    public OperateController(BpmnService bpmnService, OperateService operateService) {
        super();
        this.bpmnService = bpmnService;
        this.operateService = operateService;
    }

    @GetMapping("/process-definitions/{processDefinitionId}/getProcessDefinitionXml")
    public ResponseEntity<String>   getProcessDefinitionXml(@PathVariable String processDefinitionId)
            throws NumberFormatException, OperateException{
        return new ResponseEntity<String>(bpmnService.getProcessDefinitionXml(processDefinitionId), HttpStatus.OK);

    }

    @GetMapping("/process-definitions/getAll")
    public ResponseEntity<List<ProcessDefinition>> getAllProcessDefinitions() throws OperateException{
        Set<String> present = new HashSet<>();
        List<ProcessDefinition> result = new ArrayList<>();
        List<ProcessDefinition> processDefs = operateService.getProcessDefinitions();
        if (processDefs != null) {
            for (ProcessDefinition def : processDefs) {
                if (!present.contains(def.getBpmnProcessId())) {
                    result.add(def);
                    present.add(def.getBpmnProcessId());
                }
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/process-definitions/{key}")

    public ResponseEntity<ProcessDefinition>  getProcessDefinitionByKey(@PathVariable long key) throws OperateException{
        return  new ResponseEntity<>(operateService.getProcessDefinitionByKey(key), HttpStatus.OK);

    }

    @PostMapping("/process-definitions/search")
    public ResponseEntity<List<ProcessDefinition>>  searchProcessDefinitions(
            @RequestBody ProcessDefinitionFilter processDefinitionFilter) throws OperateException{
        return new ResponseEntity<>(operateService.searchProcessDefinition(processDefinitionFilter), HttpStatus.OK);

    }

    @GetMapping("/process-instances/{key}/process-instance")
    public ResponseEntity<ProcessInstance> getProcessInstanceByKey(@PathVariable long key) throws OperateException{
        return new ResponseEntity<>( operateService.getProcessInstance(key), HttpStatus.OK);

    }

    @GetMapping("/process-instances/getAll")
    public ResponseEntity<List<ProcessInstance>> getProcessInstances() throws OperateException{
        return	new ResponseEntity<>( operateService.getProcessInstances(), HttpStatus.OK);

    }


    @PostMapping("/process-instances/search")
    public ResponseEntity<List<ProcessInstance>> searchProcessInstances(@RequestBody ProcessInstanceFilter processInstanceFilter)
            throws OperateException{
        return new ResponseEntity<>(operateService.searchProcessInstances(processInstanceFilter), HttpStatus.OK);

    }

//	    @DeleteMapping("/process-instances/{key}")
//
//	    ChangeStatus deleteProcessInstance(Long key) throws OperateException;

    @GetMapping("/process-instances/variables/{key}")
    public ResponseEntity<Variable> getVariableByKey(@PathVariable long key) throws OperateException{
        return new ResponseEntity<>(operateService.getVariable(key), HttpStatus.OK);

    }

    @PostMapping("/process-instances/variables/search")
    public ResponseEntity<List<Variable>> searchVariables(@RequestBody VariableFilter variableFilter) throws OperateException{
        return new ResponseEntity<>(operateService.searchVariables(variableFilter), HttpStatus.OK);

    }

    @GetMapping("/flowNode-instances/{key}")
    public ResponseEntity<FlownodeInstance> getFlowNodeInstanceByKey(@PathVariable long key) throws OperateException{
        return new ResponseEntity<>(operateService.getFlowNodeInstance(key), HttpStatus.OK);

    }

    @PostMapping("/flowNode-instances/search")
    public ResponseEntity<List<FlownodeInstance>> searchFlowNodeInstances(@RequestBody FlownodeInstanceFilter flownodeInstanceFilter)
            throws OperateException{
        return new ResponseEntity<>(operateService.searchFlowNodeInstances(flownodeInstanceFilter), HttpStatus.OK);

    }
}
