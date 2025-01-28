package com.example.project.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles database operations such as connecting to the database, executing queries, and closing connections.
 */
public class DBHandler {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_PATH = "jdbc:mysql://localhost:3307/libraryjava";
    private static final String DB_USER = "root";
    private static final String DB_PW = "root";

    private Connection connection = null;
    private Statement statement = null;

    /**
     * Initializes a DBHandler object and establishes a connection to the database.
     */
    public DBHandler() {
        connect();
    }

    /**
     * Establishes a connection to the database.
     */
    private void connect() {
        try {
            // Load MySQL JDBC driver
            Class.forName(JDBC_DRIVER);
            // Establish connection
            connection = DriverManager.getConnection(DB_PATH, DB_USER, DB_PW);
            statement = connection.createStatement();
            System.out.println("Connection to database established successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: Failed to establish connection to the database.");
            e.printStackTrace();
        }
    }

    /**
     * Executes a query on the database.
     *
     * @param sql The SQL query to be executed.
     * @return The result set of the query, or null if an error occurs.
     */
    public ResultSet executeQuery(String sql) {
        try {
            if (statement != null) {
                return statement.executeQuery(sql);
            } else {
                System.out.println("Statement is null. Cannot execute query.");
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the connection to the database.
     *
     * @return The database connection.
     */
    public Connection getConnection() {
        if (connection == null) {
            System.out.println("Connection is null. Attempting to reconnect.");
            connect();
        }
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
