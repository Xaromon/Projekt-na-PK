package com.projekt_pk;

import java.sql.Connection;
import java.sql.SQLException;

public class Person extends DBBaseClass {

    public Person() {
        super(
                // TableName
                "person",

                // Insert Statement
                "CREATE TABLE person " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "name CHAR(50) NOT NULL, " +
                "surname CHAR(50) NOT NULL, " +
                "address CHAR(50) NOT NULL, " +
                "city INTEGER, " +
                "FOREIGN KEY(city) REFERENCES city(name));",

                // Select All statement
                "SELECT person.id, person.name, person.surname, person.address, city.name as city FROM person, city WHERE person.city=city.id"
        );
    }

    public void insertNewPerson(String name, String surname, String address, String city) throws SQLException {
        Connection dbConnection = new DatabaseConnection().getDatabaseConnection();
        String insertSql = "INSERT INTO " + this.tableName + " (name, surname, address, city) " +
                "VALUES ('" + name + "', '" + surname + "', '" + address + "', '" + city + "')";
        System.out.println(insertSql);
        this.executeSQLStatement(dbConnection, insertSql);
        dbConnection.close();
    }
}
