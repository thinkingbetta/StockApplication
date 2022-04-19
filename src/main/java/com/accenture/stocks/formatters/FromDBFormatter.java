package com.accenture.stocks.formatters;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * This class contains a method to format the data contained in the database.
 */
public class FromDBFormatter {

    /**
     * This method transforms a SQL date into a Java local date.
     *
     * @param resultSet   result set showing a date
     * @param columnIndex index of the column of the result set where the date is shown
     * @return local date
     * @throws SQLException
     */
    public LocalDate fromDateSQLtoLocalDate(ResultSet resultSet, int columnIndex) throws SQLException {
        Date sqlDate = resultSet.getDate(columnIndex);
        LocalDate date = sqlDate.toLocalDate();
        return date;
    }
}
