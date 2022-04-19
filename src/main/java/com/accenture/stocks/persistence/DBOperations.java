package com.accenture.stocks.persistence;

import com.accenture.stocks.entities.Stock;

import java.sql.*;

/**
 * This class contains all the methods to operate and interact with the SQLDatabase.
 */
public class DBOperations {
    private Connection connection;

    public DBOperations(Connection connection) {
        this.connection = connection;
    }

    /**
     * Given a prepared statement where a statement.RETURN_GENERATED_KEYS is invoked,
     * this method returns the generated key.
     *
     * @param preparedStatement query with statement.RETURN_GENERATED_KEYS
     * @return int generated key
     * @throws SQLException
     */
    private int getGeneratedKey(PreparedStatement preparedStatement) throws SQLException {
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        int generatedKey = generatedKeys.getInt(1);
        return generatedKey;
    }

    /**
     * This method executes a SELECT query where a string is set as parameter and produces a result set.
     *
     * @param field a String set as parameter
     * @param query a SELECT query that produces a result set
     * @return result set
     * @throws SQLException
     */
    public ResultSet executeOneStringSelect(String field, String query) throws SQLException {
        PreparedStatement id = this.connection.prepareStatement(query);
        id.setString(1, field);
        ResultSet resultSet = id.executeQuery();
        return resultSet;
    }

    /**
     * This method executes an INSERT/UPDATE query where a string is set as parameter and returns a generated key.
     *
     * @param field a String set as parameter
     * @param query an INSERT/UPDATE query
     * @return prepared statement
     * @throws SQLException
     */
    private PreparedStatement executeOneStringInsert(String field, String query) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, field);
        preparedStatement.executeUpdate();
        return preparedStatement;
    }

    /**
     * This method executes a SELECT query where an int is set as parameter and produces a result set.
     *
     * @param number an int set as parameter
     * @param query  a SELECT query
     * @return result set
     * @throws SQLException
     */
    public ResultSet executeOneIntSelect(int number, String query) throws SQLException {
        PreparedStatement id = this.connection.prepareStatement(query);
        id.setInt(1, number);
        ResultSet resultSet = id.executeQuery();
        return resultSet;
    }

    /**
     * This method executes a SELECT query that generates a resul set and where two ints are set as parameter .
     *
     * @param field1 String set as first parameter
     * @param field2 String set as second parameter
     * @param query  a SELECT query
     * @return result set
     * @throws SQLException
     */
    public ResultSet executeTwoIntSelect(int field1, int field2, String query) throws SQLException {
        PreparedStatement id = this.connection.prepareStatement(query);
        id.setInt(1, field1);
        id.setInt(2, field2);
        ResultSet resultSet = id.executeQuery();
        return resultSet;
    }

    /**
     * This method executes an INSERT/UPDATE query where two ints are set as parameter and that is void.
     *
     * @param int1  int set as first parameter
     * @param int2  int set as second parameter
     * @param query an INSERT/UPDATE query
     * @throws SQLException
     */
    public void executeTwoIntInsert(int int1, int int2, String query) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setInt(1, int1);
        preparedStatement.setInt(2, int2);
        preparedStatement.executeUpdate();
    }


    /**
     * Given the name of a table, this method deletes all the data inside. The name of a table must be set as a
     * private constant in the class.
     *
     * @param tableName String
     * @throws SQLException
     */
    public void deleteDataFromTable(String tableName) throws SQLException {
        PreparedStatement preparedStatementDeleteFromCompany = this.connection.prepareStatement("delete from " +
                tableName);
        preparedStatementDeleteFromCompany.executeUpdate();
    }


    /**
     * Given the name of a table, this method set the autoincrement to zero. The name of a table must be set as a
     * private constant in the class.
     *
     * @param tableName String
     * @throws SQLException
     */
    public void autoincrementToZero(String tableName) throws SQLException {
        PreparedStatement preparedStatementSetAutoincrementToZero = this.connection.prepareStatement("ALTER TABLE "
                + tableName + " AUTO_INCREMENT=0");
        preparedStatementSetAutoincrementToZero.executeUpdate();
    }

    /**
     * Given a stock, this method checks if a company is already inserted in the company table. If it is present it
     * returns the already existing key, otherwise it inserts the new company in the table and
     * returns the generated key.
     *
     * @param stock instance of Stock
     * @return key of company inserted in the table
     * @throws SQLException
     */
    public int insertCompanyInCompanyTable(Stock stock) throws SQLException {
        ResultSet companyNameResult = executeOneStringSelect(stock.getCompanyName(), "SELECT id FROM company " +
                "WHERE company_name = ?");
        boolean hasNext = companyNameResult.next();
        if (hasNext) {
            int alreadyExistingKeyCompany = companyNameResult.getInt(1);
            return alreadyExistingKeyCompany;
        } else {
            PreparedStatement preparedStatementCompany = executeOneStringInsert(stock.getCompanyName(),
                    "INSERT INTO company (company_name) VALUES(?)");
            int generatedKeyCompany = getGeneratedKey(preparedStatementCompany);
            return generatedKeyCompany;
        }
    }

    /**
     * Given a stock, this method checks if an industry is already inserted in the industry table. If it is present,
     * it returns the already existing key, otherwise it inserts the new industry in the table and
     * returns the generated key.
     *
     * @param stock instance of Stock
     * @return key of industry inserted in the table
     * @throws SQLException
     */
    public int insertIndustryInIndustryTableFromStock(Stock stock) throws SQLException {
        ResultSet industryNameResult = executeOneStringSelect(stock.getIndustryName(),
                "SELECT id FROM industry WHERE industry_name = ?");
        boolean hasNext = industryNameResult.next();
        if (hasNext) {
            int alreadyExistingKeyIndustry = industryNameResult.getInt(1);
            return alreadyExistingKeyIndustry;
        } else {
            PreparedStatement preparedStatementIndustry = executeOneStringInsert(stock.getIndustryName(),
                    "INSERT INTO industry(industry_name) VALUES (?)");
            int generatedKeyIndustry = getGeneratedKey(preparedStatementIndustry);
            return generatedKeyIndustry;
        }
    }

    /**
     * Given a String, this method checks if an industry is already inserted in the industry table. If it is present,
     * it returns the already existing key, otherwise it inserts the new industry in the table and
     * returns the generated key.
     *
     * @param industryName String
     * @return key of industry inserted in the table
     * @throws SQLException
     */
    public int insertIndustryInIndustryTable(String industryName) throws SQLException {
        ResultSet industryNameResult = executeOneStringSelect(industryName,
                "SELECT id FROM industry WHERE industry_name = ?");
        boolean hasNext = industryNameResult.next();
        if (hasNext) {
            int alreadyExistingKeyIndustry = industryNameResult.getInt(1);
            return alreadyExistingKeyIndustry;
        } else {
            PreparedStatement preparedStatementIndustry = executeOneStringInsert(industryName,
                    "INSERT INTO industry(industry_name) VALUES (?)");
            int generatedKeyIndustry = getGeneratedKey(preparedStatementIndustry);
            return generatedKeyIndustry;
        }
    }

    /**
     * Given a stock, this method inserts the price and date in the table pricedate and returns the generated key.
     *
     * @param stock instance of Stock
     * @return key of pricedate inserted in the table
     * @throws SQLException
     */
    public int insertPriceDateInPriceDateTable(Stock stock) throws SQLException {
        PreparedStatement preparedStatementPriceDate = this.connection.prepareStatement("INSERT INTO " +
                "pricedate (euro_price, date)  VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatementPriceDate.setBigDecimal(1, stock.getPrice());
        preparedStatementPriceDate.setDate(2, Date.valueOf(stock.getDate()));
        preparedStatementPriceDate.executeUpdate();
        int generatedKeyPriceDate = getGeneratedKey(preparedStatementPriceDate);
        return generatedKeyPriceDate;
    }

    /**
     * Given a stock, this method insert all the variable in the corresponding tables.
     *
     * @param stock instance of Stock
     * @throws SQLException
     */
    public void importStockInDB(Stock stock) throws SQLException {
        int companyId = insertCompanyInCompanyTable(stock);
        int priceDateId = insertPriceDateInPriceDateTable(stock);
        int industryId = insertIndustryInIndustryTableFromStock(stock);
        executeTwoIntInsert(companyId, priceDateId,
                "INSERT INTO company_pricedate (company_id,pricedate_id) VALUES (?,?)");
        ResultSet companyIndustryIdResult = executeTwoIntSelect(companyId, industryId,
                "SELECT id FROM company_industry WHERE stock_id = ? AND industry_id = ?");
        boolean hasNext = companyIndustryIdResult.next();
        if (!hasNext) {
            executeTwoIntInsert(companyId, industryId,
                    "INSERT INTO company_industry (stock_id, industry_id) VALUES (?,?)");
        }
    }

    /**
     * Given the starting letters of a company name, this method searches for a company name. The ESCAPE is used to
     * ignore symbols that implement wildcards(i.e. % and _), but also may be part of a company name.
     * A wildcard character is used to substitute one or more characters in a string.
     *
     * @param input String inserted by user
     * @return result set
     * @throws SQLException
     */
    public ResultSet executeSelectLikeStartsWith(String input) throws SQLException {
        PreparedStatement preparedStatementLike = this.connection.prepareStatement("SELECT * FROM company" +
                " WHERE company_name LIKE ? ESCAPE '['");
        preparedStatementLike.setString(1, input + "%");
        ResultSet resultSet = preparedStatementLike.executeQuery();
        return resultSet;
    }

    /**
     * Given a company id, this method gets the highest price of a stock.
     *
     * @param companyId int
     * @return result set
     * @throws SQLException
     */
    public ResultSet getMaxPriceStock(int companyId) throws SQLException {
        ResultSet resultSet = executeTwoIntSelect(companyId, companyId,
                "select company_name, date, euro_price\n" +
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

    /**
     * Given a company id, this method gets the lowest price of a stock.
     *
     * @param companyId int
     * @return result set
     * @throws SQLException
     */
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

    /**
     * This method returns the number of industries in industry table.
     *
     * @return result set
     * @throws SQLException
     */
    public ResultSet selectCountFromTableIndustry() throws SQLException {
        Statement stmt = this.connection.createStatement();
        ResultSet rs = stmt.executeQuery("select count(industry_name) as num from industry");
        return rs;
    }

    /**
     * This method selects how many company are assigned to each industry.
     *
     * @return result set
     * @throws SQLException
     */
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


    /**
     * Given a company id, this method selects the corresponding industry.
     *
     * @param input int
     * @return
     * @throws SQLException
     */
    public int fromCompanyIdToIndustry(int input) throws SQLException {
        ResultSet companyNameIndustry = executeOneIntSelect(input, "SELECT c.id, c.company_name, \n" +
                " i.id, i.industry_name\n" +
                "FROM company c\n" +
                "LEFT JOIN\n" +
                "company_industry b ON c.id = b.stock_id\n" +
                "LEFT JOIN\n" +
                "industry i ON i.id = b.industry_id\n" +
                "where c.id = ? \n" +
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

    /**
     * Given a company id, this method selects the last ten prices of its stock.
     *
     * @param input int inserted by user
     * @return result set
     * @throws SQLException
     */
    public ResultSet selectTenLastStocksByCompanyId(int input) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select c.id, c.company_name," +
                " p.euro_price, p.date, i.industry_name\n" +
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

    /**
     * This method select all the stocks inserted in the database and order them following the pricedate id.
     *
     * @return result set
     * @throws SQLException
     */
    public ResultSet selectAllStocks() throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement("select c.company_name," +
                " p.euro_price, p.date, i.industry_name\n" +
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
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}



