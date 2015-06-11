package com.projekt_pk;

import java.sql.Connection;
import java.sql.SQLException;

public class City extends DBBaseClass {
    public City() {
        super(
            // TableName
            "city",

            // Insert statement
            "CREATE TABLE city (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name CHAR(50) UNIQUE NOT NULL);",

            // Select all statement
            "SELECT * FROM city"
        );
    }

    public void insertNewCity(Connection dbConnection, String cityName) throws SQLException{
        String insertSql =
            "INSERT INTO " + this.tableName + " (name) " +
            "VALUES ('" + cityName + "');";
        this.executeSQLStatement(dbConnection, insertSql);
    }
}
