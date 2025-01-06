package io.camunda.example.model.request;
import java.sql.SQLException;

import io.camunda.example.model.response.PostgreSQLResponse;
import io.camunda.example.utility.DatabaseClient;




public interface PostgreSQLRequestData {
     
    PostgreSQLResponse invoke(final DatabaseClient databaseClient,DatabaseConnection databaseConnection,String databaseName) throws SQLException; 
      String getDatabaseName();
 
}


