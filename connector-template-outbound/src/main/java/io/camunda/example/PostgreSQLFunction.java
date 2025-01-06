package io.camunda.example;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import io.camunda.example.model.request.PostgreSQLRequest;
import io.camunda.example.utility.DatabaseClient;

@OutboundConnector( 
        name = "PostgreSQL", 
        inputVariables = {"databaseConnection", "operation", "data"}, 
        type = "com.cts.camundaconnectors.db:postgresql:1") 
// @ElementTemplate( 
//        id = "com.cts.camundaconnectors.db.postgresql.v1", 
//        name = "PostgreSQL Database Connector", 
//        version = 1, 
//        description = "PostgreSQL connector", 
//        propertyGroups = { 
//                 @ElementTemplate.PropertyGroup(id = "databaseConnection", label = "Database Connection"), 
//                 @ElementTemplate.PropertyGroup(id = "operation", label = "Operation"), 
//                 @ElementTemplate.PropertyGroup(id = "method", label =  "Method Type"), 
//                 @ElementTemplate.PropertyGroup(id = "inputs", label = "Input Mapping"), 
//                 @ElementTemplate.PropertyGroup(id = "output", label = "Output Mapping"), 
//                 @ElementTemplate.PropertyGroup(id = "errors", label = "Error Handling") 
//               }, 
//               inputDataClass = PostgreSQLRequest.class 
//        ) 
 
public class PostgreSQLFunction implements OutboundConnectorFunction { 
    private static final Logger LOGGER = LoggerFactory.getLogger(PostgreSQLFunction.class); 
    private final DatabaseClient databaseClient; 
 
    public PostgreSQLFunction() { 
       this(new DatabaseClient()); 
    } 
 
    public PostgreSQLFunction(DatabaseClient databaseClient) { 
       this.databaseClient = databaseClient; 
    } 
 
    @Override 
    public Object execute(OutboundConnectorContext outboundConnectorContext) throws Exception { 
       final var context = outboundConnectorContext.bindVariables(PostgreSQLRequest.class); 
       System.out.println("********###########*********##############************" +  context ); 
       LOGGER.debug("Request verified successfully and all required secrets replaced"); 
       var obj = context.invoke(databaseClient); 
       return obj; 
    } 
} 
 


