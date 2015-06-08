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
            try {
                this.prepareDatabaseStructure(dbConnection);
            } catch (SQLException exception) {
                this.createExceptionMessageAndExit(exception);
            }
            dbConnection.close();
        }
    }

    private Connection connectToDatabase() {
        Connection dbConnection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.databasePath);
        } catch (Exception exception) {
            this.createExceptionMessageAndExit(exception);
        }
        return dbConnection;
    }

    public Connection getDatabaseConnetion() {
        return this.connectToDatabase();
    }

    private void createExceptionMessageAndExit(Exception exc) {
        System.err.println(exc.getClass().getName() + ": " + exc.getMessage());
        System.exit(0);
    }

    private void prepareDatabaseStructure(Connection dbConnection) throws SQLException{
        City city = new City();
        city.createTable(dbConnection);
    }
}
