package com.camunda.job.scheduler.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import com.camunda.job.scheduler.pojo.BatchJobResult;
import com.camunda.job.scheduler.utilty.BatchJobExecutor;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@ExternalTaskSubscription("group3Jobs")
public class ExecuteGroup3Jobs implements ExternalTaskHandler{

    private final BatchJobExecutor batchJobExecutor;

    public ExecuteGroup3Jobs(BatchJobExecutor batchJobExecutor) {
        this.batchJobExecutor = batchJobExecutor;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
       
        Map<String, Object> variables = new HashMap<>();

        Map<String, String> job7 = externalTask.getVariable("job7");
        String directory = job7.get("directory");
        String parameter = job7.get("parameter");

        try {
            BatchJobResult result = batchJobExecutor.executeBatchJob(directory, parameter);
            variables.put("batchOutput", result.getOutput());
            variables.put("RC", result.getExitCode());
            variables.put("job7", job7);

            if(result.getExitCode() > 4){
                throw new InterruptedException("Error while executing batch job with RC: " + result.getExitCode());
            }

            externalTaskService.complete(externalTask, variables);
        } catch (IOException | InterruptedException e) {
            String message = e.getMessage();
            log.error("Exception: {}", message);
            variables.put("message", message);
            externalTaskService.handleFailure(externalTask.getId(), message, e.getClass().getSimpleName(), 3, 60000);
            externalTaskService.complete(externalTask, variables);
        }
    }
    
}