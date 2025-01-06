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
public class Group1Jobs {

    private final BatchJobExecutor batchJobExecutor;

    private BatchJobResult result;
    
        public Group1Jobs(BatchJobExecutor batchJobExecutor) {
            this.batchJobExecutor = batchJobExecutor;
        }
        
        @JobWorker(type = "group1Jobs")
        public Map<String, Object> fetchJobDetails(final ActivatedJob job, final JobClient jobClient) {
    
            Map<String,Object> inputVariables = job.getVariablesAsMap();
    
            Map<String,String> job1 = (Map<String,String>) inputVariables.get("job1");
            String directory = job1.get("directory");
            String parameter = job1.get("parameter");
    
            Map<String, Object> outputVariables = new HashMap<>();
    
            try {
                
                 result = batchJobExecutor.executeBatchJob(directory, parameter);

             System.out.println(result.toString());
             outputVariables.put("output", result.getOutput());
             outputVariables.put("RC", result.getExitCode());

            if(result.getExitCode() > 4){
                throw new InterruptedException("Error While Executing Group1 Batch Job: " + result.getExitCode());
            }

        } catch (Exception e) {
            jobClient.newThrowErrorCommand(job.getKey())
            .errorCode("group1Error")
            .errorMessage(e.getMessage())
            .send()
            .exceptionally((throwable -> {
             throw new RuntimeException("Could not throw the BPMN Error Event", throwable);
         }));
        }

        
        
        

        return outputVariables;
    }
}
