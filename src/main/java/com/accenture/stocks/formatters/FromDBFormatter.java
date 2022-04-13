package com.accenture.stocks.formatters;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class FromDBFormatter {

    public LocalDate fromDateSQLtoLocalDate(ResultSet resultSet, int columnIndex) throws SQLException {
        Date sqlDate = resultSet.getDate(columnIndex);
        LocalDate date = sqlDate.toLocalDate();
        return date;
    }
}
