package org.Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    private static Connection connection;

    private DataBaseConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                String url = "jdbc:sqlite:src/data/steamdatabase.db";
                connection = DriverManager.getConnection(url);
                connection.setAutoCommit(false);
                System.out.println("Connection successful!");
            } catch (SQLException e) {
                System.err.println("Error connecting to the database: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
                System.out.flush();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
                System.out.flush();
            }
        }
    }
}


