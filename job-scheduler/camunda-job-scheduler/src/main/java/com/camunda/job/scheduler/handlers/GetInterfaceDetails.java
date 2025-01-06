package com.camunda.job.scheduler.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class GetInterfaceDetails  implements JavaDelegate{

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        
        Object messageContentObj = execution.getVariable("messageContent");

        List<Map<String, Object>> messageContent = new ArrayList<>();

        if(messageContentObj instanceof Map){
        
            messageContent.add((Map<String, Object>) messageContentObj);

        } else if(messageContentObj instanceof List){
            
            List<?> list = (List<?>) messageContentObj;
            for (Object obj : list) {
                if(obj instanceof Map) {
                messageContent.add((Map<String, Object>) obj);
                } else {
                    throw new IllegalArgumentException("List contains non map objects: " + obj);
                }
            }
        } else {
            throw new IllegalArgumentException("messageContent is not a m: " + messageContentObj.getClass().getName());
        }

        List<String> interfaceIds = new ArrayList<>();
        List<Integer> statusCodes = new ArrayList<>();

        for(Map<String, Object> message : messageContent){

            String interfaceId = (String) message.get("interfaceId");
            Integer statusCode = (Integer) message.get("statusCode");

            if(interfaceId != null && statusCode != null){
                interfaceIds.add(interfaceId);
                statusCodes.add(statusCode);
            }
        }

        execution.setVariable("interfaceIds", interfaceIds);
        execution.setVariable("statusCodes", statusCodes);

        boolean anyStatusCodeGreaterThanZero = false;
        boolean allStatusCodesZero = true;

        for(Integer statusCode : statusCodes){
            if(statusCode > 0){
                anyStatusCodeGreaterThanZero = true;
            } 
            if(statusCode != 0){
                allStatusCodesZero = false;
            }
        }

        execution.setVariable("anyStatusCodeGreaterThanZero", anyStatusCodeGreaterThanZero);
        execution.setVariable("allStatusCodesZero", allStatusCodesZero);

        System.out.println("anyStatusCodeGreaterThanZero: " + anyStatusCodeGreaterThanZero);
        System.out.println("allStatusCodesZero: " + allStatusCodesZero);
    }

}
