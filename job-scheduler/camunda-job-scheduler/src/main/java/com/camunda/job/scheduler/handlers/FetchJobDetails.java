package com.camunda.job.scheduler.handlers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FetchJobDetails implements JavaDelegate {

    @Value("${csv.file.path}")
    private String csvFilePath;

    private final ResourceLoader resourceLoader;

    public FetchJobDetails(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Resource resource = resourceLoader.getResource(csvFilePath);

        Map<String, String> job1 = null;
        List<Map<String, String>> job2ToJob6List = new ArrayList<>();
        Map<String, String> job7 = null;

        try(BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))){

            String header = br.readLine();
            log.info("*************************************************************************");
            log.info("Header: {}", header);
            log.info("*************************************************************************");

            String line;
            while((line = br.readLine()) !=null){

                String[] data = line.split(",\\s*");

                if(data.length >=4){
                    Map<String, String> jobDetails = new HashMap<>();
                    jobDetails.put("jobName", data[0].trim());
                    jobDetails.put("description", data[1].trim());
                    jobDetails.put("directory", data[2].trim());
                    jobDetails.put("parameter", String.join(" ", Arrays.copyOfRange(data, 3, data.length)).trim());

                    log.info("job details: {}", jobDetails);

                    String jobName = jobDetails.get("jobName");
                    if("F09LQ407".equals(jobName)){
                        job1 = jobDetails;
                    }else if(jobName.compareTo("F09LQ470") >= 0 && jobName.compareTo("F09LQ474") <= 0){
                        job2ToJob6List.add(jobDetails);
                    }else if("F09LQ475".equals("job7")){
                        job7 = jobDetails;
                    }
                } else {
                    log.info("Invalid data: {}", line);
                }
            }
        } catch (Exception e) {
            log.error("Error reading csv file", e);
            throw new BpmnError("CSV_READ_ERROR", "Failed to read csv file");
        }

        delegateExecution.setVariable("job1", job1);
        delegateExecution.setVariable("job2ToJob6List", job2ToJob6List);
        delegateExecution.setVariable("job7", job7);
    }
}
