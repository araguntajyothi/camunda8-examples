package io.camunda.example.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.camunda.example.model.request.DatabaseConnection;




public class DatabaseClient { 
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseClient.class); 
 
      public DatabaseClient() {} 
 
      public Connection getConnectionObject( 
          DatabaseConnection databaseConnection, String databaseName) { 
         System.out.println("DATABSE DETAILS : " + databaseConnection + "  " + databaseName); 
        String dbURL = 
            "jdbc:postgresql://" 
                + databaseConnection.getHost().strip() 
                + ":" 
                + databaseConnection.getPort().strip() 
                + "/"; 
        //String db1 = "postgres"; 
        if (databaseName != null && !databaseName.isBlank()) dbURL = dbURL+databaseName.toLowerCase(); 
        else dbURL += "template1"; 
        return connectToDatabase(databaseConnection, dbURL); 
      } 
 
      private Connection connectToDatabase(DatabaseConnection databaseConnection, String dbURL) { 
        Connection conn; 
        try { 
          Class.forName("org.postgresql.Driver"); 
          Properties props = new Properties(); 
          props.setProperty("user", databaseConnection.getUsername()); 
          props.setProperty("password", databaseConnection.getPassword()); 
          props.setProperty("stringtype", "unspecified"); 
          conn = DriverManager.getConnection(dbURL, props); 
        } catch (ClassNotFoundException clsEx) { 
          LOGGER.error("Unable to load Postgres Driver: {}", clsEx.getMessage()); 
          throw new RuntimeException("Unable to load PostgreSQL Driver: " + clsEx.getMessage(), clsEx); 
        } catch (SQLException sqlException) { 
          LOGGER.error("SQLException: {}", sqlException.getMessage()); 
          if (sqlException.getMessage().contains("FATAL: password authentication")) 
            throw new RuntimeException( 
                "AuthenticationError: Invalid username or password", sqlException); 
          else if (sqlException.getMessage().contains("FATAL: database")) 
            throw new RuntimeException("InvalidDatabase: " + sqlException.getMessage(), sqlException); 
          else if (sqlException.getMessage().contains("FATAL: role")) 
            throw new RuntimeException("InvalidUsername: " + sqlException.getMessage(), sqlException); 
          throw new RuntimeException( 
              "ConnectionError: Unable to connect to DB " + sqlException.getMessage(), sqlException); 
        } 
        if (conn == null) { 
          LOGGER.error("ConnectionError: Unable to connect to Database"); 
          throw new RuntimeException("ConnectionError: Unable to connect to Database"); 
        } else LOGGER.debug("Connected successfully to the database"); 
        return conn; 
      } 
 
} 
 
