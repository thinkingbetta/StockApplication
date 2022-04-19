package com.accenture.stocks.persistence;

import com.accenture.stocks.entities.Stock;

import java.sql.*;
import java.util.Scanner;

public class DBOperations {
    private Connection connection;

    public DBOperations(Connection connection) {

        this.connection = connection;
    }

    private int getGeneratedKey(PreparedStatement preparedStatement) throws SQLException {
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        int generatedKey = generatedKeys.getInt(1);
        return generatedKey;
    }

    public ResultSet executeOneStringSelect(String field, String query) throws SQLException {
        PreparedStatement id = this.connection.prepareStatement(query);
        id.setString(1, field);
        ResultSet resultSet = id.executeQuery();
        return resultSet;
    }

   /* public void deleteRowfromTable( String tableName, String columnName, int id) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(" Delete from ? where ? = ?");
        preparedStatement.setString(1, tableName);
        preparedStatement.setString(2, columnName);
        preparedStatement.setInt(3, id);
        preparedStatement.executeUpdate();
    }*/

    public ResultSet executeOneIntSelect(int field, String query) throws SQLException {
        PreparedStatement id = this.connection.prepareStatement(query);
        id.setInt(1, field);
        ResultSet resultSet = id.executeQuery();
        return resultSet;
    }

    public ResultSet executeTwoIntSelect(int field1, int field2, String query) throws SQLException {
        PreparedStatement id = this.connection.prepareStatement(query);
        id.setInt(1, field1);
        id.setInt(2, field2);
        ResultSet resultSet = id.executeQuery();
        return resultSet;
    }

    private PreparedStatement executeStringInsert(String field, String query) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, field);
        preparedStatement.executeUpdate();
        return preparedStatement;
    }

    public void executeTwoIntQuery(int int1, int int2, String query) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setInt(1, int1);
        preparedStatement.setInt(2, int2);
        preparedStatement.executeUpdate();
    }


    public void deleteDataFromTable(String tableName) throws SQLException {
        PreparedStatement preparedStatementDeleteFromCompany = this.connection.prepareStatement("delete from " + tableName);
        preparedStatementDeleteFromCompany.executeUpdate();
    }
    //TODO come fare bene il prepared statmente con  i nomi delle table?

    public void autoincrementToZero(String tableName) throws SQLException {
        PreparedStatement preparedStatementSetAutoincrementToZero = this.connection.prepareStatement("ALTER TABLE " + tableName + " AUTO_INCREMENT=0");
        preparedStatementSetAutoincrementToZero.executeUpdate();
    }

    public int insertCompanyInCompanyTable(Stock stock) throws SQLException {
        ResultSet companyNameResult = executeOneStringSelect(stock.getCompanyName(), "SELECT id FROM company WHERE company_name = ?");
        boolean hasNext = companyNameResult.next();
        if (hasNext) {
            int alreadyExistingKeyCompany = companyNameResult.getInt(1);
            return alreadyExistingKeyCompany;
        } else {
            PreparedStatement preparedStatementCompany = executeStringInsert(stock.getCompanyName(), "INSERT INTO company (company_name) VALUES(?)");

            int generatedKeyCompany = getGeneratedKey(preparedStatementCompany);

            return generatedKeyCompany;
        }
    }

    public int insertIndustryInIndustryTableFromStock(Stock stock) throws SQLException {
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

    public int insertIndustryInIndustryTable(String industryName) throws SQLException {
        ResultSet industryNameResult = executeOneStringSelect(industryName, "SELECT id FROM industry WHERE industry_name = ?");
        boolean hasNext = industryNameResult.next();
        if (hasNext) {
            int alreadyExistingKeyIndustry = industryNameResult.getInt(1);
            return alreadyExistingKeyIndustry;
        } else {
            PreparedStatement preparedStatementIndustry = executeStringInsert(industryName, "INSERT INTO industry(industry_name) VALUES (?)");
            int generatedKeyIndustry = getGeneratedKey(preparedStatementIndustry);
            return generatedKeyIndustry;
        }
    }

    public int insertPriceDateInPricedateTable(Stock stock) throws SQLException {
        PreparedStatement preparedStatementPriceDate = this.connection.prepareStatement("INSERT INTO pricedate (euro_price," +
                " date)  VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);

        preparedStatementPriceDate.setBigDecimal(1, stock.getPrice());
        preparedStatementPriceDate.setDate(2, Date.valueOf(stock.getDate()));
        preparedStatementPriceDate.executeUpdate();

        int generatedKeyPriceDate = getGeneratedKey(preparedStatementPriceDate);

        return generatedKeyPriceDate;
    }

    public ResultSet executeSelectLikeStartsWith(String input) throws SQLException {

        PreparedStatement preparedStatementLike = this.connection.prepareStatement("SELECT * FROM company WHERE company_name LIKE ? ESCAPE '['");
        preparedStatementLike.setString(1, input + "%");
        ResultSet resultSet = preparedStatementLike.executeQuery();
        return resultSet;
    }

    public void importStockInDB(Stock stock) throws SQLException {
        int companyId = insertCompanyInCompanyTable(stock);
        int priceDateId = insertPriceDateInPricedateTable(stock);
        int industryId = insertIndustryInIndustryTableFromStock(stock);
        executeTwoIntQuery(companyId, priceDateId, "INSERT INTO company_pricedate (company_id,pricedate_id) VALUES (?,?)");

        ResultSet companyIndustryIdResult = executeTwoIntSelect(companyId, industryId, "SELECT id FROM company_industry WHERE stock_id = ? AND industry_id = ?");
        boolean hasNext = companyIndustryIdResult.next();
        if (!hasNext) {
            executeTwoIntQuery(companyId, industryId, "INSERT INTO company_industry (stock_id, industry_id) VALUES (?,?)");
        }
    }

    public ResultSet selectAllStocks() throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement("select c.company_name, p.euro_price, p.date, i.industry_name\n" +
                "from company c\n" +
                "left join company_pricedate d\n" +
                "on c.id = d.company_id\n" +
                "left join pricedate p\n" +
                "on d.pricedate_id = p.id\n" +
                "left join company_industry b  \n" +
                "on c.id = b.stock_id\n" +
                "left join industry i\n" +
                "on i.id = b.industry_id\n" +
                "order by p.id");
        ResultSet resultSet =preparedStatement.executeQuery();
        return resultSet;
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
        ResultSet resultSet = preparedStatement.executeQuery();
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

    public ResultSet selectCountFromTableIndustry() throws SQLException {
        Statement stmt = this.connection.createStatement();
        ResultSet rs = stmt.executeQuery("select count(industry_name) as num from industry");
        return rs;
    }

    public ResultSet selectCountCompaniesFromIndustryTable() throws SQLException {
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

    public int fromCompanyIdToIndustry(int input, Scanner scanner) throws SQLException {

            ResultSet companyNameIndustry = executeOneIntSelect(input, "SELECT c.id, c.company_name, \n" +
                    " i.id, i.industry_name\n" +
                    "FROM\n" +
                    "    company c\n" +
                    "        LEFT JOIN\n" +
                    "    company_industry b ON c.id = b.stock_id\n" +
                    "        LEFT JOIN\n" +
                    "    industry i ON i.id = b.industry_id\n" +
                    "    where c.id = ? \n" +
                    "GROUP BY c.id, company_name, i.id, industry_name");
            companyNameIndustry.next();
            int companyId = companyNameIndustry.getInt(1);
            String companyName = companyNameIndustry.getString(2);
            int industryId = companyNameIndustry.getInt(3);
            String industryName = companyNameIndustry.getString(4);
            System.out.println("==============ID:" + companyId + "==============\n" +
                    "Stock name: " + companyName + "\nIndustry ID: " + industryId + "\nIndustry: " + industryName
                    + "\n==================================");

            return companyId;
        }
}



