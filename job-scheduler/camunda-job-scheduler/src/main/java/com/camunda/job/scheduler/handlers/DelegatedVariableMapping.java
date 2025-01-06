package com.camunda.job.scheduler.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateVariableMapping;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;

public class DelegatedVariableMapping implements DelegateVariableMapping {

    @Override
    public void mapInputVariables(DelegateExecution execution, VariableMap variables) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'mapInputVariables'");

        variables.putValue("job1","job1");
        variables.put("messageContent","messageContent");

    }

    @Override
    public void mapOutputVariables(DelegateExecution arg0, VariableScope arg1) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'mapOutputVariables'");
    }
    
}
