package com.projekt_pk;

import java.sql.Connection;
import java.sql.SQLException;

public class Tour extends DBBaseClass {

    public Tour() {
        super(
                // TableName
                "tour",

                // Insert Statement
                "CREATE TABLE tour " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "name CHAR(50) NOT NULL, " +
                "description TEXT, " +
                "hotel INTEGER, " +
                "person INTEGER, " +
                "city INTEGER, " +
                "FOREIGN KEY(hotel) REFERENCES hotel(name), " +
                "FOREIGN KEY(person) REFERENCES person(id), " +
                "FOREIGN KEY(city) REFERENCES city(name));",

                // Select All statement
                "SELECT tour.id, tour.name, tour.description, hotel.name as hotel, person.surname as person, city.name as city " +
                "FROM tour, person, city, hotel WHERE tour.city=city.id AND tour.person=person.id AND tour.hotel=hotel.id"
        );
    }

    public void insertNewTour(String name, String description, String hotel, String person, String city) throws SQLException {
        Connection dbConnection = new DatabaseConnection().getDatabaseConnection();
        String insertSql = "INSERT INTO " + this.tableName + " (name, description, hotel, person, city) " +
                "VALUES ('" + name + "', '" + description + "', '" + hotel + "', '" + person + "', '" + city + "')";
        System.out.println(insertSql);
        this.executeSQLStatement(dbConnection, insertSql);
        dbConnection.close();
    }
}
