package com.camunda.wrapper.controller;

import java.util.List;
import java.util.Map;

import com.camunda.wrapper.entity.Task;
import com.camunda.wrapper.entity.TaskSearch;
import com.camunda.wrapper.service.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.zeebe.client.ZeebeClient;
import jakarta.validation.constraints.NotNull;

@RestController
public class TaskListController {
    @Autowired
    ZeebeClient client;
    private TaskListService taskListService;

    TaskListController(TaskListService taskListService, ZeebeClient client) {
        this.taskListService = taskListService;
        this.client = client;
    }

    @GetMapping("/{taskId}/claim")
    public ResponseEntity<Task> claimTask(@PathVariable String taskId) throws TaskListException {
        Task claim = taskListService.claim(taskId, "demo");
        return ResponseEntity.ok(claim);
    }

    @GetMapping("/{taskId}/unclaim")
    public ResponseEntity<Task> unclaimTask(@PathVariable String taskId) throws TaskListException {
        Task unclaim = taskListService.unclaim(taskId);
        return ResponseEntity.ok(unclaim);
    }

    @PostMapping("/{taskId}/complete")
    public void completeTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables)
            throws TaskListException {
        System.out.println("Completing task " + taskId + "` with variables: " + variables);
        taskListService.completeTask(taskId, variables);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskByTaskId(@PathVariable String taskId) throws TaskListException {
        return new ResponseEntity<>(taskListService.getTaskById(taskId), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Task>> getTasks() throws TaskListException {
        return new ResponseEntity<> (taskListService.getTasks(TaskState.CREATED, null), HttpStatus.OK);
    }

    @PostMapping("/tasks/search")
    public ResponseEntity<List<Task>> searchTasks(@NotNull @RequestBody TaskSearch taskSearch) throws TaskListException {
        return new ResponseEntity<>(taskListService.searchTasks(taskSearch), HttpStatus.OK);
    }

    @GetMapping("/tasks/{processDefinitionKey}")
    public ResponseEntity<List<Task>> getTasksByPDKey(@PathVariable String processDefinitionKey) throws TaskListException{
        return new ResponseEntity<>(taskListService.getTasksByProcessDefinitionKey(processDefinitionKey),HttpStatus.OK);

    }
    @GetMapping("/tasks/processName/{processName}")
    public ResponseEntity<List<Task>> getTasksByProcessName(@PathVariable String processName) throws TaskListException{
        return new ResponseEntity<>(taskListService.getTasksByProcessName(processName),HttpStatus.OK);

    }

    @GetMapping("/variables/{taskId}")
    public ResponseEntity<Map<String, Object>> getTaskVariables(@PathVariable String taskId) throws TaskListException{
        return new ResponseEntity<> (taskListService.getTaskVariables(taskId), HttpStatus.OK);

    }
}
