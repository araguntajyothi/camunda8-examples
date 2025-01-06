package com.camunda.job.scheduler.pojo;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((output == null) ? 0 : output.hashCode());
        result = prime * result + exitCode;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BatchJobResult other = (BatchJobResult) obj;
        if (output == null) {
            if (other.output != null)
                return false;
        } else if (!output.equals(other.output))
            return false;
        if (exitCode != other.exitCode)
            return false;
        return true;
    }

    
    
}
