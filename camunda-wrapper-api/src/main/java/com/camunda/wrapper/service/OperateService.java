package com.camunda.wrapper.service;

import io.camunda.operate.dto.FlownodeInstance;
import io.camunda.operate.dto.ProcessDefinition;
import io.camunda.operate.dto.ProcessInstance;
import io.camunda.operate.dto.Variable;
import io.camunda.operate.exception.OperateException;
import io.camunda.operate.search.FlownodeInstanceFilter;
import io.camunda.operate.search.ProcessDefinitionFilter;
import io.camunda.operate.search.ProcessInstanceFilter;
import io.camunda.operate.search.VariableFilter;
import java.util.List;

public interface OperateService {

    FlownodeInstance getFlowNodeInstance(Long key) throws OperateException;

    List<ProcessDefinition> getProcessDefinitions() throws OperateException;

    String getProcessDefinitionXmlByKey(Long key) throws OperateException;

    ProcessInstance getProcessInstance(Long key) throws OperateException;

    List<ProcessInstance> getProcessInstances() throws OperateException;

    Variable getVariable(Long key) throws OperateException;


    List<FlownodeInstance> searchFlowNodeInstances(FlownodeInstanceFilter flownodeInstanceFilter)
            throws OperateException;

    List<ProcessDefinition> searchProcessDefinition(ProcessDefinitionFilter processDefinitionFilter)
            throws OperateException;

    List<ProcessInstance> searchProcessInstances(ProcessInstanceFilter processInstanceFilter)
            throws OperateException;

    List<Variable> searchVariables(VariableFilter variableFilter) throws OperateException;


//    ChangeStatus deleteProcessInstance(Long key) throws OperateException;

    ProcessDefinition getProcessDefinitionByKey(long key) throws OperateException;
}
