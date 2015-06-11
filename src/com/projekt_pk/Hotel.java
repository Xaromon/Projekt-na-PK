package com.projekt_pk;

import java.sql.Connection;
import java.sql.SQLException;

public class Hotel extends DBBaseClass {

    public Hotel() {
        super(
                // TableName
                "hotel",

                // Insert Statement
                "CREATE TABLE hotel " + "" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name CHAR(50) UNIQUE NOT NULL, address CHAR(50), " +
                "price INTEGER, city INTEGER, FOREIGN KEY(city) REFERENCES city(name));",

                // Select All statement
                "SELECT hotel.name, hotel.address, hotel.price, city.name as city FROM hotel, city WHERE hotel.city=city.id"
        );
    }
    public void insertNewHotel(String name, String addres, String price, String city) throws SQLException {
        Connection dbConnection = new DatabaseConnection().getDatabaseConnection();
        String insertSql = "INSERT INTO " + this.tableName + " (name, address, price, city) " +
                "VALUES ('" + name + "', '" + addres + "', + '" + price + "', '" + city + "')";
        this.executeSQLStatement(dbConnection, insertSql);
        dbConnection.close();
    }
}
