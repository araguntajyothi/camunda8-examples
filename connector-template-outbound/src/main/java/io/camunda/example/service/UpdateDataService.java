package io.camunda.example.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.camunda.example.model.request.DatabaseConnection;
import io.camunda.example.model.request.PostgreSQLRequestData;
import io.camunda.example.model.response.PostgreSQLResponse;
import io.camunda.example.model.response.QueryResponse;
import io.camunda.example.utility.ConstructWhereClause;
import io.camunda.example.utility.DatabaseClient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;


public class UpdateDataService implements PostgreSQLRequestData { 
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateDataService.class); 
    @NotBlank private String databaseName; 
    @NotBlank private String tableName; 
    private Map<String, Object> filters; 

    @NotEmpty(message = "updateMap can't be null or empty") 
    private Map<String, Object> updateMap; 

    @Override 
    public PostgreSQLResponse invoke(DatabaseClient databaseClient,DatabaseConnection databaseConnection, String databaseName) throws SQLException { 
       final Connection connection = databaseClient.getConnectionObject(databaseConnection, databaseName); 
       QueryResponse<String> queryResponse; 
      try { 
        String updateQuery = updateRowsQuery(tableName, updateMap, filters); 
        LOGGER.info("Update query: {}", updateQuery); 
        int rowsUpdated; 
        try (Statement st = connection.createStatement()) { 
          connection.setAutoCommit(false); 
          rowsUpdated = st.executeUpdate(updateQuery); 
          connection.commit(); 
          queryResponse = new QueryResponse<>(rowsUpdated + " row(s) updated successfully"); 
          LOGGER.info("UpdateDataQueryStatus: {}", queryResponse.getResponse()); 
        } 
      } catch (SQLException sqlException) { 
        LOGGER.error(sqlException.getMessage()); 
        throw new RuntimeException(sqlException); 
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

    private String updateRowsQuery( 
        String tableName, Map<String, Object> updateMap, Map<String, Object> filters) { 
      String whereClause = ConstructWhereClause.getWhereClause(filters); 
      if (whereClause == null || whereClause.isBlank() || filters == null || filters.isEmpty()) 
        LOGGER.debug("WHERE clause is empty, this will update all rows in the table"); 
      StringBuilder setClause = new StringBuilder(); 
      List<String> columns = new ArrayList<>(updateMap.keySet()); 
      for (String column : columns) { 
        Object colValue = updateMap.get(column); 
        if (colValue instanceof String) { 
          setClause.append(column).append("=").append("'").append(colValue).append("', "); 
        } else { 
          setClause.append(column).append("=").append(colValue).append(", "); 
        } 
      } 
      setClause = new StringBuilder(setClause.toString().strip()); 
      if (setClause.length() > 1) 
        setClause = new StringBuilder(setClause.substring(0, setClause.length() - 1)); 
      return String.format("UPDATE %s SET %s %s", tableName, setClause, whereClause); 
    } 

    public String getDatabaseName() { 
      return databaseName; 
    } 

    public void setDatabaseName(String databaseName) { 
      this.databaseName = databaseName; 
    } 

    public String getTableName() { 
      return tableName; 
    } 

    public void setTableName(String tableName) { 
      this.tableName = tableName; 
    } 

    public Map<String, Object> getFilters() { 
      return filters; 
    } 

    public void setFilters(Map<String, Object> filters) { 
      this.filters = filters; 
    } 

    public Map<String, Object> getUpdateMap() { 
      return updateMap; 
    } 

    public void setUpdateMap(Map<String, Object> updateMap) { 
      this.updateMap = updateMap; 
    } 

    @Override 
    public boolean equals(Object o) { 
      if (this == o) return true; 
      if (o == null || getClass() != o.getClass()) return false; 
      UpdateDataService that = (UpdateDataService) o; 
      return Objects.equals(databaseName, that.databaseName) 
          && Objects.equals(tableName, that.tableName) 
          && Objects.equals(filters, that.filters) 
          && Objects.equals(updateMap, that.updateMap); 
    } 

    @Override 
    public int hashCode() { 
      return Objects.hash(databaseName, tableName, filters, updateMap); 
    } 

    @Override 
    public String toString() { 
      return "UpdateDataService{" 
          + "databaseName='" 
          + databaseName 
          + '\'' 
          + ", tableName='" 
          + tableName 
          + '\'' 
          + ", filters=" 
          + filters 
          + ", updateMap=" 
          + updateMap 
          + '}'; 
    } 
  } 

