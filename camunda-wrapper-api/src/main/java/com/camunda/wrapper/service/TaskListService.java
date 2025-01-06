package com.camunda.wrapper.service;

import java.util.List;
import java.util.Map;


import com.camunda.wrapper.entity.Task;
import com.camunda.wrapper.entity.TaskSearch;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;

public interface TaskListService   {

    public Task claim(String taskId, String assignee) throws TaskListException;
    public Task unclaim(String taskId) throws TaskListException;
    public void completeTask(String taskId, Map<String, Object> variables) throws TaskListException;
    public List<Task> searchTasks(TaskSearch taskSearch) throws TaskListException;
    public Task getTaskById(String taskId) throws TaskListException;
    public List<Task> getTasks(TaskState state, Integer pageSize) throws TaskListException;
    public List<Task> getTasksByProcessDefinitionKey(String processDefinitionKey) throws TaskListException;
    public List<Task> getTasksByProcessName(String processName) throws TaskListException;
    public  Map<String, Object> getTaskVariables(String taskId) throws TaskListException;
    //    public List<Task> getAssigneeTasks(String assignee, TaskState state, Integer pageSize) throws TaskListException;
//    public List<Task> getGroupTasks(String group, TaskState state, Integer pageSize) throws TaskListException;
    public String getForm(String formId, String processDefinitionId) throws TaskListException;
}
