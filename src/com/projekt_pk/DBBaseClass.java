package com.projekt_pk;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBBaseClass {
    protected String createTableSqlStatement = null;
    protected String selectAllSqlStatement = null;
    protected String tableName = null;

    public DBBaseClass(String createStatement, String selectStatement, String tableName) {
        this.createTableSqlStatement = createStatement;
        this.selectAllSqlStatement = selectStatement;
        this.tableName = tableName;
    }

    public void createTable(Connection dbConnection) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.executeUpdate(this.createTableSqlStatement);
        statement.close();
    }

    public ResultSet selectAll(Connection dbConnection) throws SQLException {
        Statement statement = dbConnection.createStatement();
        ResultSet results = statement.executeQuery(this.selectAllSqlStatement);
        return results;
    }

    protected void executeSQLStatement(Connection dbConnection, String sqlStatement)
            throws SQLException {
        Statement statement = dbConnection.createStatement();
        dbConnection.setAutoCommit(false);
        statement.executeUpdate(sqlStatement);
        statement.close();
        dbConnection.commit();
    }

    public String getTableName() {
        return this.tableName;
    }
}
