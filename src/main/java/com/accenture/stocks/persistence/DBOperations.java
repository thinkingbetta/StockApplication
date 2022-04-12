package com.accenture.stocks.persistence;

import com.accenture.stocks.entities.Stock;

import java.sql.*;

public class DBOperations {
    private Connection connection;

    public DBOperations(Connection connection) {

        this.connection = connection;
    }

    private int getGeneratedKey (PreparedStatement preparedStatement) throws SQLException {
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        int generatedKey = generatedKeys.getInt(1);
        return generatedKey;
    }

    private ResultSet executeSelect(String field, String query) throws SQLException {
        PreparedStatement id = this.connection.prepareStatement(query);
        id.setString(1,field);
        ResultSet resultSet= id.executeQuery();
        return resultSet;
    }

    private PreparedStatement executeStringInsert( String field, String query ) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, field);
        preparedStatement.executeUpdate();
        return preparedStatement;
    }

    private void executeTwoIntInsert(int int1, int int2, String query) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setInt(1, int1);
        preparedStatement.setInt(2, int2);
        preparedStatement.executeUpdate();
    }

    public void importStockInDB(Stock stock) throws SQLException {
        int companyId = insertCompanyInTable(stock);
        int priceDateId = insertPriceDate(stock);
        int industryId = insertIndustry(stock);
        executeTwoIntInsert(companyId, priceDateId, "INSERT INTO company_pricedate (company_id,pricedate_id) VALUES (?,?)");
        executeTwoIntInsert(companyId, industryId,"INSERT INTO company_industry (stock_id,industry_id) VALUES (?,?)");
    }

    public int insertCompanyInTable(Stock stock) throws SQLException {
        ResultSet companyNameResult = executeSelect(stock.getCompanyName(),"SELECT id FROM company WHERE company_name = ?");
        boolean hasNext = companyNameResult.next();
        if (hasNext)  {
            int alreadyExistingKeyCompany = companyNameResult.getInt(1);
            return alreadyExistingKeyCompany;
        } else {
            PreparedStatement preparedStatementCompany = executeStringInsert(stock.getCompanyName(),"INSERT INTO company (company_name) VALUES(?)");

            int generatedKeyCompany = getGeneratedKey(preparedStatementCompany);

           return generatedKeyCompany;
        }
    }

    public int insertIndustry(Stock stock) throws SQLException {
        ResultSet industryNameResult = executeSelect(stock.getIndustryName(), "SELECT id FROM industry WHERE industry_name = ?");
        boolean hasNext = industryNameResult.next();
        if (hasNext) {
            int alreadyExistingKeyIndustry = industryNameResult.getInt(1);
            return alreadyExistingKeyIndustry;
        } else {
            PreparedStatement preparedStatementIndustry = executeStringInsert(stock.getIndustryName(), "INSERT INTO industry(industry_name) VALUES (?)");
            int generatedKeyIndustry = getGeneratedKey(preparedStatementIndustry);
            return generatedKeyIndustry;
        }
    }

    //TODO ha senso anche su questa tabella verificare che non esista gia' una copia dei dati che si stanno andando a raggiungere?
    public int insertPriceDate(Stock stock) throws SQLException{
        PreparedStatement preparedStatementPriceDate = this.connection.prepareStatement("INSERT INTO pricedate (euro_price," +
                " date)  VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);

        preparedStatementPriceDate.setBigDecimal(1, stock.getPrice());
        preparedStatementPriceDate.setDate(2, Date.valueOf(stock.getDate()));
        preparedStatementPriceDate.executeUpdate();

        int generatedKeyPriceDate = getGeneratedKey(preparedStatementPriceDate);

        return generatedKeyPriceDate;
    }

    public void deleteDataFromTable(String tableName) throws SQLException {
        PreparedStatement preparedStatementDeleteFromCompany = this.connection.prepareStatement("delete from " + tableName);
        preparedStatementDeleteFromCompany.executeUpdate();
    }

    public void autoincrementToZero(String tableName) throws SQLException {
        PreparedStatement preparedStatementSetAutoincrementToZero = this.connection.prepareStatement("ALTER TABLE " + tableName + " AUTO_INCREMENT=0");
        preparedStatementSetAutoincrementToZero.executeUpdate();
    }

    public ResultSet executeSelectLikeStartsWith(String tableName, String columnName, String input) throws SQLException {

        PreparedStatement preparedStatementLike = this.connection.prepareStatement("SELECT * FROM " + tableName + " WHERE " + columnName + " LIKE ? ESCAPE '['");
        preparedStatementLike.setString(1, input + "%");
        ResultSet resultSet= preparedStatementLike.executeQuery();
        return resultSet;
    }

}


