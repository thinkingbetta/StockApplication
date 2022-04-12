package com.accenture.stocks.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    public Connection getConnection() throws SQLException {
        Connection conn;
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "secret");

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/stockapp", properties);

        System.out.println("You are successfully connected to the database.");
        return conn;
    }
}
