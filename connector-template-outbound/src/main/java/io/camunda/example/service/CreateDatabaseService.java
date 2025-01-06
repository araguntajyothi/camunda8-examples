package io.camunda.example.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.camunda.example.model.request.DatabaseConnection;
import io.camunda.example.model.request.PostgreSQLRequestData;
import io.camunda.example.model.response.PostgreSQLResponse;
import io.camunda.example.model.response.QueryResponse;
import io.camunda.example.utility.DatabaseClient;
import jakarta.validation.constraints.NotBlank;



public class CreateDatabaseService implements PostgreSQLRequestData { 
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDatabaseService.class); 
    @NotBlank private String databaseName; 

    @Override 
    public PostgreSQLResponse invoke(DatabaseClient databaseClient,DatabaseConnection databaseConnection,String databaseName) throws SQLException { 
     final Connection connection = databaseClient.getConnectionObject(databaseConnection, "template1"); 
      QueryResponse<String> queryResponse; 
      try (Statement st = connection.createStatement()) { 
        String query = "CREATE DATABASE " + databaseName; 
        st.executeUpdate(query); 
        queryResponse = new QueryResponse<>("Database '" + databaseName + "' created successfully"); 
        LOGGER.info("CreateDatabaseQueryStatus: {}", queryResponse.getResponse()); 
      } catch (SQLException ex) { 
        LOGGER.error(ex.getMessage()); 
        throw new RuntimeException(ex); 
      } finally { 
        try { 
          connection.close(); 
          LOGGER.debug("Connection closed"); 
        } catch (SQLException e) { 
          LOGGER.warn("Error while closing the database connection"); 
        } 
      } 
      return queryResponse; 
    } 

    @Override 
    public String getDatabaseName() { 
      return databaseName; 
    } 

    public void setDatabaseName(String databaseName) { 
      this.databaseName = databaseName; 
    } 

    @Override 
    public boolean equals(Object o) { 
      if (this == o) return true; 
      if (o == null || getClass() != o.getClass()) return false; 
      CreateDatabaseService that = (CreateDatabaseService) o; 
      return Objects.equals(databaseName, that.databaseName); 
    } 

    @Override 
    public int hashCode() { 
      return Objects.hash(databaseName); 
    } 

    @Override 
    public String toString() { 
      return "CreateDatabaseService{" + "databaseName='" + databaseName + '\'' + '}'; 
    } 
  } 
