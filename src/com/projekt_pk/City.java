package com.projekt_pk;

import java.sql.Connection;
import java.sql.SQLException;

public class City extends DBBaseClass {
    public City() {
        super(
            "CREATE TABLE " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " name CHAR(50))",
            "SELECT * FROM city",
            "city"
        );
    }

    public void insertNewCity(Connection dbConnection, String cityName) throws SQLException{
        String insertSql =
            "INSERT INTO " + this.tableName + " (name) " +
            "VALUES ('" + cityName + "');";
        this.executeSQLStatement(dbConnection, insertSql);

    }
}
