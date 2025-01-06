package com.camunda.wrapper.service;

import io.camunda.operate.exception.OperateException;

public interface BpmnService {

    public String getEmbeddedFormSchema(String bpmnProcessId, String processDefinitionId, String formId)
            throws NumberFormatException, OperateException;

    public String getProcessDefinitionXml(String processDefinitionId) throws NumberFormatException, OperateException;

    public   String getProcessName(String bpmnProcessId, String processDefinitionId)
            throws NumberFormatException, OperateException;

    public  String getTaskName(String bpmnProcessId, String processDefinitionId, String activityId)
            throws NumberFormatException, OperateException;

}
