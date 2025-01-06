package com.camunda.job.scheduler.governance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateCSVServiceTask  implements  JavaDelegate{

     @Value("${csv.file.path}")
    private String csvFilePath;

    private final ResourceLoader resourceLoader;

    public UpdateCSVServiceTask(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
       
        Object updatedData = execution.getVariable("updatedJobData");

        if(updatedData != null && updatedData instanceof String) {

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> jobDataList = objectMapper.readValue((String) updatedData, List.class);

            log.info("Updated job data: {}", jobDataList);

            List<Map<String, String>> originalCsvData = readCsvFile(csvFilePath);
            mergeCsvData(originalCsvData, jobDataList);

            writeCsvToTempFile(originalCsvData);
        } else {
            throw new RuntimeException("No updated job data found in process variables");
            }
    }

    private List<Map<String, String>> readCsvFile(String csvFilePath)  throws  IOException {

        List<Map<String, String>> csvData = new ArrayList<>();
        Resource resource = resourceLoader.getResource(csvFilePath);

        try(BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser.getRecords()) {
                Map<String, String> jobData = new HashMap<>();
                jobData.put("jobName", record.get("JobName"));
                jobData.put("description", record.get("Description"));
                jobData.put("parameter", record.get("Parameters"));
                jobData.put("directory", record.get("Directory"));

                // Add placeholders for new columns
                jobData.put("jobCancellation", "");
                jobData.put("jobOverride", "");
                jobData.put("batchRestart", "");
                csvData.add(jobData);
            }
        }

        return csvData;
    }

    private void mergeCsvData(List<Map<String, String>> originalData, List<Map<String, String>> updatedData){
        Map<String,Map<String, String>> updatedJobsMap = new HashMap<>();
        for (Map<String, String> updatedJob : updatedData) {
            updatedJobsMap.put(updatedJob.get("jobName"), updatedJob);
            }

            for(Map<String, String> originalJob : originalData){
                Map<String, String> updatedJob = updatedJobsMap.get(originalJob.get("jobName"));

                if(updatedJob != null){

                    originalJob.put("description", updatedJob.get("description"));
                    originalJob.put("parameter", updatedJob.get("parameter"));
                    originalJob.put("directory", updatedJob.get("directory"));

                    originalJob.put("jobCancellation", String.valueOf(updatedJob.get("jobCancellation")));
                    originalJob.put("jobOverride", String.valueOf(updatedJob.get("jobOverride")));
                    originalJob.put("batchRestart", String.valueOf(updatedJob.get("batchRestart")));
                }
            }

            for(Map<String, String> updatedJob : updatedData){
                if(!updatedJobsMap.containsKey(updatedJob.get("jobName"))){
                    originalData.add(updatedJob);
                }
            }
    }

    private void  writeCsvToTempFile(List<Map<String, String>> mergedData) throws IOException {
        
        String baseDir = System.getProperty("user.dir");

        String tempFolderPath = baseDir + "/camunda-job-scheduler/src/main/resources/temp/";

        File tempFolder = new File(tempFolderPath);

        if(!tempFolder.exists()){
            if(tempFolder.mkdir()){
                log.info("Temp folder created at: {}", tempFolderPath);
            } else{
                log.error("Failed to create temp folder at: {}", tempFolderPath);
                throw new IOException("Failed to create temp.");
                }
        }

        File tempCsvFile = new File(tempFolder + "Updated_LoanIQActJobs.csv");

        try(@SuppressWarnings("deprecation")
        CSVPrinter printer = new CSVPrinter(new FileWriter(tempCsvFile, false),
          CSVFormat.DEFAULT.withHeader("JobName", "Description", "Parameters", "Directory","JobCancellation", "JobOverride", "BatchRestart"))){
            for (Map<String, String> jobData : mergedData) {

                printer.printRecord(jobData.get("jobName"), jobData.get("description"), jobData.get("parameter"), jobData.get("directory"),
                jobData.get("jobCancellation"), jobData.get("jobOverride"), jobData.get("batchRestart"));
                
            }
            log.info("CSV file successfully written to: {}", tempCsvFile.getAbsolutePath());
          } catch (IOException e) {
            log.error("Error writing CSV file to: {}", e.getMessage());
            throw new IOException("Error writing CSV file to: " + tempCsvFile.getAbsolutePath());
            }
    }

}
