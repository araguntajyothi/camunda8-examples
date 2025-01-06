package com.camunda.wrapper.entity;

import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;

import io.camunda.tasklist.dto.TaskState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Task   {

    private String id;
    private String jobKey;
    private String name;
    private String processName;
    private String assignee;
    @CreationTimestamp
    private String creationDate;
    private TaskState taskState;
    private List<String> candidateGroups;
    private List<String> sortValues;
    private Boolean isFirst;
    private Map<String, Object> variables;
    private String formKey;
    private String processDefinitionKey;
    private String processInstanceKey;
}
