package com.camunda.wrapper.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.camunda.wrapper.entity.Task;
import com.camunda.wrapper.entity.TaskSearch;
import com.camunda.wrapper.service.TaskListService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SimpleAuthentication;
import io.camunda.tasklist.dto.Form;
import io.camunda.tasklist.dto.TaskList;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.dto.Variable;
import io.camunda.tasklist.exception.TaskListException;

import io.camunda.zeebe.client.ZeebeClient;

@Service
public class TaskListServiceImpl implements TaskListService {

    private CamundaTaskListClient client;

    @Autowired
    private ZeebeClient zeebeClient;

    TaskListServiceImpl(ZeebeClient zeebeClient){
        this.zeebeClient=zeebeClient;
    }
    private CamundaTaskListClient getCamundaTaskListClient() throws TaskListException {
        if (client == null) {
            SimpleAuthentication sa = new SimpleAuthentication("demo", "demo");  //To use if Tasklist is not configured with Identity and Keycloak
            client =
                    new CamundaTaskListClient.Builder()
                            .shouldReturnVariables()
                            .taskListUrl("http://localhost:8082")
                            .authentication(sa)
                            .build();

            System.out.println("=========Task Client=========" + client + "URL==" + "http://localhost:8082");
        }
        return client;
    }
    private Task convert(io.camunda.tasklist.dto.Task task) {
        Task result = new Task();
        BeanUtils.copyProperties(task, result);
        if (task.getVariables() != null) {
            result.setVariables(new HashMap<>());
            for (Variable var_ : task.getVariables()) {
                result.getVariables().put(var_.getName(), var_.getValue());
            }
        }
        return result;
    }

    private List<Task> convert(TaskList tasks) {
        List<Task> result = new ArrayList<>();
        for (io.camunda.tasklist.dto.Task task : tasks.getItems()) {
            result.add(convert(task));
        }
        return result;
    }

    @Override
    public Task claim(String taskId, String assignee) throws TaskListException {
        return convert(getCamundaTaskListClient().claim(taskId, assignee));
    }

    @Override
    public Task unclaim(String taskId) throws TaskListException {
        return convert(getCamundaTaskListClient().unclaim(taskId));
    }
    @Override
    public void completeTask(String taskId, Map<String, Object> variables) throws TaskListException {
        getCamundaTaskListClient().completeTask(taskId, variables);
    }
    @Override
    public List<Task> searchTasks(TaskSearch taskSearch) throws TaskListException {
        if (Boolean.TRUE.equals(taskSearch.getAssigned()) && taskSearch.getAssignee() != null) {
            return convert(getCamundaTaskListClient().getAssigneeTasks(taskSearch.getAssignee(),
                    TaskState.valueOf(null), taskSearch.getPageSize()));
        }
        if (taskSearch.getGroup() != null) {
            return convert(getCamundaTaskListClient().getGroupTasks(taskSearch.getGroup(),
                    TaskState.fromJson(taskSearch.getState()), taskSearch.getPageSize()));
        }
        return convert(getCamundaTaskListClient().getTasks(taskSearch.getAssigned(),
                TaskState.fromJson(taskSearch.getState()), taskSearch.getPageSize()));
    }
    @Override
    public Task getTaskById(String taskId) throws TaskListException {
        return convert(getCamundaTaskListClient().getTask(taskId));
    }
    @Override
    public List<Task> getTasks(TaskState state, Integer pageSize) throws TaskListException {
        return convert(getCamundaTaskListClient().getTasks(null, state, pageSize));
    }
    @Override
    public List<Task> getTasksByProcessDefinitionKey(String processDefinitionKey) throws TaskListException {
        TaskState state = null;
        Integer pageSize = null;
        TaskList getTasksList = getCamundaTaskListClient().getTasks(null, state, pageSize);
        List<Task> tasks = convert(getTasksList);
        return tasks.stream()
                .filter(task -> task.getProcessDefinitionKey().equals(processDefinitionKey))
                .collect(Collectors.toList());
    }
    @Override
    public List<Task> getTasksByProcessName(String processName) throws TaskListException {
        TaskState state = null;
        Integer pageSize = null;
        TaskList getTasksList = getCamundaTaskListClient().getTasks(null, state, pageSize);
        List<Task> tasks = convert(getTasksList);
        return tasks.stream()
                .filter(task -> task.getProcessName().equals(processName))
                .collect(Collectors.toList());
    }

    public static Map<String, Object> mapVariables(List<Variable> variables) {
        Map<String, Object> result = new HashMap<>();
        for (Variable var : variables) {
            result.put(var.getName(), var.getValue());
        }
        return result;
    }
    @Override
    public Map<String, Object> getTaskVariables(String taskId) throws TaskListException {
        return mapVariables(getCamundaTaskListClient().getVariables(taskId));
    }
//	@Override
//	public List<Task> getAssigneeTasks(String assignee, TaskState state, Integer pageSize) throws TaskListException {
//		 return convert(getCamundaTaskListClient().getAssigneeTasks(assignee, state, pageSize));
//	}
//
//	@Override
//	public List<Task> getGroupTasks(String group, TaskState state, Integer pageSize) throws TaskListException {
//		 return convert(getCamundaTaskListClient().getGroupTasks(group, state, pageSize));
//	}

    @Override
    public String getForm(String formId, String processDefinitionId) throws TaskListException {

        Form form = getCamundaTaskListClient().getForm(formId, processDefinitionId);
        return form.getSchema();
    }
}

