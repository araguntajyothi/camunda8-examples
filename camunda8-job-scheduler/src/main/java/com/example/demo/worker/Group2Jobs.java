package com.example.demo.worker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.demo.pojo.BatchJobResult;
import com.example.demo.utilty.BatchJobExecutor;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class Group2Jobs {

    private final BatchJobExecutor batchJobExecutor;

    public Group2Jobs(BatchJobExecutor batchJobExecutor) {
        this.batchJobExecutor = batchJobExecutor;
    }
    
    @JobWorker(type = "group2Jobs")
    public Map<String, Object> fetchJobDetails(final ActivatedJob job, final JobClient jobClient) {

        Map<String,Object> inputVariables = job.getVariablesAsMap();

        List<Map<String,String>> job2ToJob6List = (List<Map<String, String>>) inputVariables.get("job2ToJob6List");
        Map<String,String> jobDetails =  (Map<String, String>) inputVariables.get("jobDetails");
        String directory = jobDetails.get("directory");
        String parameter = jobDetails.get("parameter");
        BatchJobResult result =null;
        Map<String, Object> outputVariables = new HashMap<>();

        try {
            
             result = batchJobExecutor.executeBatchJob(directory, parameter);
             outputVariables.put("output", result.getOutput());
            
            if(result.getExitCode() > 4){
                throw new InterruptedException("Error While Executing Group1 Batch Job: " + result.getExitCode());
            }

        } catch (Exception e) {
            jobClient.newThrowErrorCommand(job.getKey())
            .errorCode("group2Error")
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
