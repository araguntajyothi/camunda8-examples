package com.camunda.wrapper.entity;

import java.util.List;

import io.camunda.tasklist.dto.DateFilter;
import io.camunda.tasklist.dto.Pagination;
import lombok.*;

@Getter
@Setter
public class TaskSearch {

    private Boolean Assigned;

    private String assignee;

    private String group;

    private Integer pageSize;

    private String state;

    private String processDefinitionKey;

    private String processInstanceKey;

    private String candidateUser;

    private String taskDefinitionId;

    private List<String> tenantIds;

    private boolean withVariables;

    private DateFilter followUpDate;

    private DateFilter dueDate;

    private Pagination pagination;

}
