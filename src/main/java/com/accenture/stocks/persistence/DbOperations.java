package com.accenture.stocks.persistence;

import com.accenture.stocks.entities.Stock;

import java.sql.*;

public class DbOperations {
    private Connection connection;

    public DbOperations(Connection connection) {

        this.connection = connection;
    }

    public void insertCompanyInTable(Stock stock) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO company (company_name)" +
                "  VALUES(?)", Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1,stock.getCompanyName());
        preparedStatement.executeUpdate();



        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        int generatedKey = generatedKeys.getInt(1);

        System.out.println("my generated key is " + generatedKey + "\n" + stock);





    }

}
