package com.projekt_pk;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBBaseClass {
    protected String createTableSqlStatement = null;
    protected String selectAllSqlStatement = null;
    protected String tableName = null;

    public DBBaseClass(String tableName, String createStatement, String selectStatement) {
        this.createTableSqlStatement = createStatement;
        this.selectAllSqlStatement = selectStatement;
        this.tableName = tableName;
    }

    public void createTable(Connection dbConnection) throws SQLException {
        Statement statement = dbConnection.createStatement();
        System.out.println(this.createTableSqlStatement);
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

    public ComboBoxDatabaseModel getComboBoxModel() {
        String[][] toComboBoxModel = null;
        ArrayList<String[]> rowList = new ArrayList<>();
        try {
            Connection dbConnection = new DatabaseConnection().getDatabaseConnection();
            Statement statement = dbConnection.createStatement();
            dbConnection.setAutoCommit(false);
            ResultSet rs = statement.executeQuery("SELECT id, name FROM " + this.tableName);
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                rowList.add(new String[]{Integer.toString(id), name});
            }
            toComboBoxModel = new String[rowList.size()][];
            for (int i = 0; i < toComboBoxModel.length; i++)
                toComboBoxModel[i] = rowList.get(i);
            rs.close();
            statement.close();
            dbConnection.commit();
        } catch (SQLException exception) {
            System.out.println(exception);
        }
        return new ComboBoxDatabaseModel(toComboBoxModel);
    }
}
