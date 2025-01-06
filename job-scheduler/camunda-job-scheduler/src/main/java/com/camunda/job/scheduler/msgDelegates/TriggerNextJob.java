package com.camunda.job.scheduler.msgDelegates;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class TriggerNextJob implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // TODO Auto-generated method stub
       
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        runtimeService.createMessageCorrelation("TriggerNextJob").correlate();
    }

}
