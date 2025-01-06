package com.camunda.wrapper.service.impl;

import com.camunda.wrapper.service.BpmnService;
import com.camunda.wrapper.service.OperateService;
import com.camunda.wrapper.utils.BpmnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import io.camunda.operate.exception.OperateException;


@EnableCaching
public class BpmnServiceImpl implements BpmnService {

    public static final String BPMN_EXTENSION = ".bpmn";

    @Autowired
    private OperateService operateService;

    @Override
    @Cacheable("processEmbeddedForms")
    public String getEmbeddedFormSchema(String bpmnProcessId, String processDefinitionId, String formId)
            throws NumberFormatException, OperateException {
        if (bpmnProcessId != null) {
            String schema = BpmnUtils.getFormSchemaFromFile(bpmnProcessId + BPMN_EXTENSION, formId);

            if (schema != null) {
                return schema;
            }
        }
        String xml = operateService.getProcessDefinitionXmlByKey(Long.valueOf(processDefinitionId));
        return BpmnUtils.getFormSchemaFromBpmn(xml, formId);
    }

    @Override
    @Cacheable("processDefinitionXml")
    public String getProcessDefinitionXml(String processDefinitionId) throws NumberFormatException, OperateException {
        return operateService.getProcessDefinitionXmlByKey(Long.valueOf(processDefinitionId));
    }

    @Override
    @Cacheable("processNames")
    public String getProcessName(String bpmnProcessId, String processDefinitionId)
            throws NumberFormatException, OperateException {

        if (bpmnProcessId != null) {
            return BpmnUtils.getProcessNameFromFile(bpmnProcessId + BPMN_EXTENSION, bpmnProcessId);
        }
        String xml = operateService.getProcessDefinitionXmlByKey(Long.valueOf(processDefinitionId));
        return BpmnUtils.getTaskNameFromBpmn(xml, bpmnProcessId);
    }

    @Override
    @Cacheable("processTaskNames")
    public String getTaskName(String bpmnProcessId, String processDefinitionId, String activityId)
            throws NumberFormatException, OperateException {

        if (bpmnProcessId != null) {
            return BpmnUtils.getTaskNameFromFile(bpmnProcessId + BPMN_EXTENSION, activityId);
        }
        String xml = operateService.getProcessDefinitionXmlByKey(Long.valueOf(processDefinitionId));
        return BpmnUtils.getTaskNameFromBpmn(xml, activityId);
    }
}
