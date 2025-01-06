package com.camunda.job.scheduler.utilty;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.camunda.job.scheduler.pojo.BatchJobResult;

@Component
public class BatchJobExecutor {

    public BatchJobResult executeBatchJob(String directory, String parameter) throws IOException, InterruptedException {

        String[] jobParameterParts = parameter.split(" ");
        String exeName = jobParameterParts[0];

        String[] remainingParameters = new String[jobParameterParts.length - 1];
        System.arraycopy(jobParameterParts, 1, remainingParameters, 0, remainingParameters.length);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(directory + "\\" + exeName);
        processBuilder.command().addAll(java.util.Arrays.asList(remainingParameters));

        StringBuilder fullCommand = new StringBuilder();
        fullCommand.append(directory).append("\\").append(exeName);

        for(String param: remainingParameters) {
            fullCommand.append(" ").append(param);
        }

        System.out.println("Executing Batch Job With Command" +fullCommand.toString());

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();

        try(BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {

            String line;

            while((line = reader.readLine()) != null) {
                System.out.println(line);
                output.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        return new BatchJobResult(output.toString(), exitCode);

        
        
    }
    
}
