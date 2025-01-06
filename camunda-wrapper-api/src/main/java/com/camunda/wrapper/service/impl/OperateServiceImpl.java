package com.camunda.wrapper.service.impl;

import java.util.List;

import com.camunda.wrapper.service.OperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;

import io.camunda.operate.CamundaOperateClient;
import io.camunda.operate.auth.AuthInterface;
import io.camunda.operate.dto.FlownodeInstance;
import io.camunda.operate.dto.ProcessDefinition;
import io.camunda.operate.dto.ProcessInstance;
import io.camunda.operate.dto.Variable;
import io.camunda.operate.exception.OperateException;
import io.camunda.operate.search.FlownodeInstanceFilter;
import io.camunda.operate.search.ProcessDefinitionFilter;
import io.camunda.operate.search.ProcessInstanceFilter;
import io.camunda.operate.search.SearchQuery;
import io.camunda.operate.search.Sort;
import io.camunda.operate.search.SortOrder;
import io.camunda.operate.search.VariableFilter;
import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SimpleAuthentication;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.zeebe.client.ZeebeClient;

@EnableCaching
public class OperateServiceImpl implements OperateService {
    private static final Logger LOG = LoggerFactory.getLogger(OperateServiceImpl.class);
    private final ZeebeClient zeebeClient;
    private final CamundaOperateClient camundaOperateClient;




    public OperateServiceImpl(ZeebeClient zeebeClient, CamundaOperateClient camundaOperateClient) {
        super();
        this.zeebeClient = zeebeClient;
        this.camundaOperateClient = camundaOperateClient;
    }

    private CamundaOperateClient getCamundaOperateClient() {
        return this.camundaOperateClient;
    }


    @Override
    public FlownodeInstance getFlowNodeInstance(Long key) throws OperateException {
        return getCamundaOperateClient().getFlownodeInstance(key);
    }

    @Override
    public List<ProcessDefinition> getProcessDefinitions() throws OperateException {
        ProcessDefinitionFilter processDefinitionFilter = new ProcessDefinitionFilter();
        SearchQuery procDefQuery = new SearchQuery.Builder()
                .filter(processDefinitionFilter)
                .size(1000)
                .sort(new Sort("version", SortOrder.DESC)).build();

        return getCamundaOperateClient().searchProcessDefinitions(procDefQuery);
    }

    @Override
    public String getProcessDefinitionXmlByKey(Long key) throws OperateException {
        LOG.info("Entering getProcessDefinitionXmlByKey for key: {} ", key);
        return getCamundaOperateClient().getProcessDefinitionXml(key);
    }

    @Override
    public ProcessInstance getProcessInstance(Long key) throws OperateException {
        return getCamundaOperateClient().getProcessInstance(key);
    }

    @Override
    public List<ProcessInstance> getProcessInstances() throws OperateException {
        ProcessInstanceFilter processInstanceFilter = new ProcessInstanceFilter();
        SearchQuery procDefQuery = new SearchQuery.Builder()
                .filter(processInstanceFilter)
                .size(1000)
                .sort(new Sort("startDate", SortOrder.DESC)).build();
        return getCamundaOperateClient().searchProcessInstances(procDefQuery);
    }

    @Override
    public Variable getVariable(Long key) throws OperateException {
        return getCamundaOperateClient().getVariable(key);
    }

    @Override
    public List<FlownodeInstance> searchFlowNodeInstances(FlownodeInstanceFilter flownodeInstanceFilter)
            throws OperateException {
        SearchQuery query = new SearchQuery.Builder().filter(flownodeInstanceFilter).build();
        return getCamundaOperateClient().searchFlownodeInstances(query);
    }

    @Override
    public List<ProcessDefinition> searchProcessDefinition(ProcessDefinitionFilter processDefinitionFilter)
            throws OperateException {
        SearchQuery query = new SearchQuery.Builder().filter(processDefinitionFilter).build();
        return getCamundaOperateClient().searchProcessDefinitions(query);
    }

    @Override
    public List<ProcessInstance> searchProcessInstances(ProcessInstanceFilter processInstanceFilter)
            throws OperateException {
        SearchQuery query = new SearchQuery.Builder().filter(processInstanceFilter).build();
        return getCamundaOperateClient().searchProcessInstances(query);
    }

    @Override
    public List<Variable> searchVariables(VariableFilter variableFilter) throws OperateException {
        SearchQuery query = new SearchQuery.Builder().filter(variableFilter).build();
        return getCamundaOperateClient().searchVariables(query);
    }

//	@Override
//	public ChangeStatus deleteProcessInstance(Long key) throws OperateException {
//		 return getCamundaOperateClient().deleteProcessInstance(key);
//	}

    @Override
    public ProcessDefinition getProcessDefinitionByKey(long key) throws OperateException {
        return getCamundaOperateClient().getProcessDefinition(key);
    }
}

