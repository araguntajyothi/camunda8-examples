package com.example.demo.worker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.demo.pojo.BatchJobResult;
import com.example.demo.utilty.BatchJobExecutor;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class Group3Jobs {

    private final BatchJobExecutor batchJobExecutor;

    public Group3Jobs(BatchJobExecutor batchJobExecutor) {
        this.batchJobExecutor = batchJobExecutor;
    }
    
    @JobWorker(type = "group3Jobs")
    public Map<String, Object> fetchJobDetails(final ActivatedJob job, final JobClient jobClient) {

        Map<String,Object> inputVariables = job.getVariablesAsMap();

        Map<String,String> job7 = (Map<String,String>) inputVariables.get("job7");
        String directory = job7.get("directory");
        String parameter = job7.get("parameter");
        BatchJobResult result = null;
        Map<String, Object> outputVariables = new HashMap<>();

        try {
            
             result = batchJobExecutor.executeBatchJob(directory, parameter);

             outputVariables.put("output", result.getOutput());
            
            if(result.getExitCode() > 4){
                throw new InterruptedException("Error While Executing Group1 Batch Job: " + result.getExitCode());
            }

        } catch (Exception e) {
            jobClient.newThrowErrorCommand(job.getKey())
            .errorCode("group3Error")
            .errorMessage(e.getMessage())
            .send()
            .exceptionally((throwable -> {
             throw new RuntimeException("Could not throw the BPMN Error Event", throwable);
         }));
        }

       
            outputVariables.put("RC", result.getExitCode());
        return outputVariables;
    }
}
