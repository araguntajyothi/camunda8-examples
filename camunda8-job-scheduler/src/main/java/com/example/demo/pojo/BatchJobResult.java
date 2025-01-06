package com.example.demo.pojo;

public class BatchJobResult {

    private final String output;

    private final int  exitCode;

    /**
     * @param output
     * @param exitCode
     */
    public BatchJobResult(String output, int exitCode) {
        this.output = output;
        this.exitCode = exitCode;
    }

    public String getOutput() {
        return output;
    }

    public int getExitCode() {
        return exitCode;
    }
    
}
