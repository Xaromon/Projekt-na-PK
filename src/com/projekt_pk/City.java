package com.projekt_pk;

public class City extends DBAbstractClass {
    public City () {
        this.tableName = "city";
        this.createTableSqlStatement =
            "CREATE TABLE " + this.tableName + " " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " name CHAR(50))";
    }
}
