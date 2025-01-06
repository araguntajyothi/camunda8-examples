package com.example.demo.worker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class FetchJobDetails {

    @Value("${csv.file.path}")
    private String csvFilePath;

    private final ResourceLoader resourceLoader;

    public FetchJobDetails(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @JobWorker(type = "fetchJobDetails")
    public Map<String, Object> fetchJobDetails(final ActivatedJob job, final JobClient jobClient) {

     Resource resource = resourceLoader.getResource(csvFilePath);

        Map<String, String> job1 = null;
        List<Map<String, String>> job2ToJob6List = new ArrayList<>();
        Map<String, String> job7 = null;
        Map<String, Object> outputVariables = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))){

            String header = br.readLine();
            System.out.println("*************************************************************************");
            System.out.println("Header: " +header);
            System.out.println("*************************************************************************");

            String line;
            while((line = br.readLine()) !=null){

                String[] data = line.split(",\\s*");

                if(data.length >=4){
                    Map<String, String> jobDetails = new HashMap<>();
                    jobDetails.put("jobName", data[0].trim());
                    jobDetails.put("description", data[1].trim());
                    jobDetails.put("directory", data[2].trim());
                    jobDetails.put("parameter", String.join(" ", Arrays.copyOfRange(data, 3, data.length)).trim());

                    System.out.println("job details: " +jobDetails);

                    String jobName = jobDetails.get("jobName");
                    if("F09LQ407".equals(jobName)){
                        job1 = jobDetails;
                    }else if(jobName.compareTo("F09LQ470") >= 0 && jobName.compareTo("F09LQ474") <= 0){
                        job2ToJob6List.add(jobDetails);
                    }else if("F09LQ475".equals(jobName)){
                        job7 = jobDetails;
                    }  
                } else {
                    System.out.println("Invalid data: " +line);
                }
            }
        } catch (Exception e) {
            jobClient.newThrowErrorCommand(job.getKey())
                   .errorCode("fileReadError")
                   .errorMessage("Error happened while reading the File")
                   .send()
                   .exceptionally((throwable -> {
                    throw new RuntimeException("Could not throw the BPMN Error Event", throwable);
                }));
        }

        outputVariables.put("job1", job1);
        outputVariables.put("job2ToJob6List", job2ToJob6List);
        outputVariables.put("job7", job7);

            return outputVariables;
        
        
    }
    
}
