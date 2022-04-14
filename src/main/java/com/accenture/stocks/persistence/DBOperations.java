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

    public ResultSet executeOneStringSelect(String field, String query) throws SQLException {
        PreparedStatement id = this.connection.prepareStatement(query);
        id.setString(1,field);
        ResultSet resultSet= id.executeQuery();
        return resultSet;
    }

    public ResultSet executeOneIntSelect(int field, String query) throws SQLException {
        PreparedStatement id = this.connection.prepareStatement(query);
        id.setInt(1,field);
        ResultSet resultSet= id.executeQuery();
        return resultSet;
    }

    private ResultSet executeTwoIntSelect(int field1, int field2, String query) throws SQLException {
        PreparedStatement id = this.connection.prepareStatement(query);
        id.setInt(1,field1);
        id.setInt(2,field2);
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
    //TODO fare bene il prepared statmente con ?
    public void deleteDataFromTable(String tableName) throws SQLException {
        PreparedStatement preparedStatementDeleteFromCompany = this.connection.prepareStatement("delete from " + tableName);
        preparedStatementDeleteFromCompany.executeUpdate();
    }
    //TODO fare bene il prepared statmente con ?
    public void autoincrementToZero(String tableName) throws SQLException {
        PreparedStatement preparedStatementSetAutoincrementToZero = this.connection.prepareStatement("ALTER TABLE " + tableName + " AUTO_INCREMENT=0");
        preparedStatementSetAutoincrementToZero.executeUpdate();
    }

    public int insertCompanyInCompanyTable(Stock stock) throws SQLException {
        ResultSet companyNameResult = executeOneStringSelect(stock.getCompanyName(),"SELECT id FROM company WHERE company_name = ?");
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

    public int insertIndustryInIndustryTable(Stock stock) throws SQLException {
        ResultSet industryNameResult = executeOneStringSelect(stock.getIndustryName(), "SELECT id FROM industry WHERE industry_name = ?");
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

    public int insertPriceDateInPricedateTable(Stock stock) throws SQLException{
        PreparedStatement preparedStatementPriceDate = this.connection.prepareStatement("INSERT INTO pricedate (euro_price," +
                " date)  VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);

        preparedStatementPriceDate.setBigDecimal(1, stock.getPrice());
        preparedStatementPriceDate.setDate(2, Date.valueOf(stock.getDate()));
        preparedStatementPriceDate.executeUpdate();

        int generatedKeyPriceDate = getGeneratedKey(preparedStatementPriceDate);

        return generatedKeyPriceDate;
    }
    //TODO fare bene il prepared statmente con ?
    public ResultSet executeSelectLikeStartsWith(String tableName, String columnName, String input) throws SQLException {

        PreparedStatement preparedStatementLike = this.connection.prepareStatement("SELECT * FROM " + tableName + " WHERE " + columnName + " LIKE ? ESCAPE '['");
        preparedStatementLike.setString(1, input + "%");
        ResultSet resultSet= preparedStatementLike.executeQuery();
        return resultSet;
    }

    public void importStockInDB(Stock stock) throws SQLException {
        int companyId = insertCompanyInCompanyTable(stock);
        int priceDateId = insertPriceDateInPricedateTable(stock);
        int industryId = insertIndustryInIndustryTable(stock);
        executeTwoIntInsert(companyId, priceDateId, "INSERT INTO company_pricedate (company_id,pricedate_id) VALUES (?,?)");

        ResultSet companyIndustryIdResult = executeTwoIntSelect(companyId,industryId,"SELECT id FROM company_industry WHERE stock_id = ? AND industry_id = ?");
        boolean hasNext = companyIndustryIdResult.next();
        if(!hasNext) {
            executeTwoIntInsert(companyId, industryId, "INSERT INTO company_industry (stock_id, industry_id) VALUES (?,?)");
        }
    }

    public ResultSet selectTenLastStocksByCompanyId(int input) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select c.id, c.company_name, p.euro_price, p.date, i.industry_name\n" +
                "from company c\n" +
                "left join company_pricedate d\n" +
                "on c.id = d.company_id\n" +
                "left join pricedate p\n" +
                "on d.pricedate_id = p.id\n" +
                "left join company_industry b  \n" +
                "on c.id = b.stock_id\n" +
                "left join industry i\n" +
                "on i.id = b.industry_id\n" +
                "where c.id = ?\n" +
                "order by date desc limit 10;");

        preparedStatement.setInt(1, input);
        ResultSet resultSet= preparedStatement.executeQuery();
        return resultSet;
    }

    public ResultSet getMaxPriceStock(int companyId) throws SQLException {
        ResultSet resultSet = executeTwoIntSelect(companyId, companyId, "select company_name, date, euro_price\n" +
                "from company c\n" +
                "left join company_pricedate d\n" +
                "on c.id = d.company_id\n" +
                "left join pricedate p\n" +
                "on d.pricedate_id = p.id\n" +
                "where c.id = ? and p.euro_price= (\n" +
                "select max(euro_price)\n" +
                "from company c\n" +
                "left join company_pricedate d\n" +
                "on c.id = d.company_id\n" +
                "left join pricedate p\n" +
                "on d.pricedate_id = p.id\n" +
                "where c.id = ?);");
        resultSet.next();
        return resultSet;
    }
    public ResultSet getMinPriceStock(int companyId) throws SQLException {
        ResultSet resultSet = executeTwoIntSelect(companyId, companyId, "select company_name, date, euro_price\n" +
                "from company c\n" +
                "left join company_pricedate d\n" +
                "on c.id = d.company_id\n" +
                "left join pricedate p\n" +
                "on d.pricedate_id = p.id\n" +
                "where c.id = ? and p.euro_price= (\n" +
                "select min(euro_price)\n" +
                "from company c\n" +
                "left join company_pricedate d\n" +
                "on c.id = d.company_id\n" +
                "left join pricedate p\n" +
                "on d.pricedate_id = p.id\n" +
                "where c.id = ?);");
        resultSet.next();
        return resultSet;
    }
    /*private ResultSet executeTwoIntSelect(int field1, int field2, String query) throws SQLException {
        PreparedStatement id = this.connection.prepareStatement(query);
        id.setInt(1,field1);
        id.setInt(2,field2);
        ResultSet resultSet= id.executeQuery();
        return resultSet;
    }*/
public ResultSet selectCountFromTableIndustry() throws SQLException{
   Statement stmt = this.connection.createStatement();
   ResultSet rs = stmt.executeQuery("select count(industry_name) as num from industry");
    return rs;
}

    public ResultSet selectCountCompaniesFromIndustryTable () throws SQLException{
        PreparedStatement prepStatCountCompanies = this.connection.prepareStatement("SELECT  i.industry_name, " +
                "COUNT(c.company_name)\n" +
                "FROM company c\n" +
                "left join company_industry b  \n" +
                "on c.id = b.stock_id\n" +
                "left join industry i\n" +
                "on i.id = b.industry_id\n" +
                "group by industry_name;");
        ResultSet resultSet = prepStatCountCompanies.executeQuery();
       return resultSet;

    }
}


