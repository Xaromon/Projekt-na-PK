package com.projekt_pk;

import java.io.File;
import java.sql.*;

public class DatabaseConnection
{
    private static final String dataBaseName = "toursDataBase.db";
    private String databasePath;

    public DatabaseConnection() throws SQLException{
        this.databasePath = System.getProperty("user.home") + '/' + dataBaseName;
        boolean databaseExists = new File(databasePath).isFile();

        if (!databaseExists) {
            System.out.println("Database doesn't exists, creating new one.");
            Connection dbConnection = this.connectToDatabase();
            dbConnection.close();
        } else
            System.out.println("Database exists.");
    }

    private Connection connectToDatabase() {
        Connection dbConnection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.databasePath);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Database opened successfully");
        return dbConnection;
    }

    private void prepareDatabaseStructure(Connection dbConnection) {
    }
}
