package com.projekt_pk;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAbstractClass {
    protected String createTableSqlStatement = null;
    protected String tableName = null;

    public void createTable(Connection dbConnection) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.executeUpdate(this.createTableSqlStatement);
        statement.close();
    }

    public String getTableName() {
        return this.tableName;
    }
}
