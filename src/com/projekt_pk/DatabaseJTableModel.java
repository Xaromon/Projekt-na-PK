package com.projekt_pk;


import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseJTableModel extends AbstractTableModel {
    private Object[][] contents;
    private String[] columnNames;
    private Class[] columnClasses;
    private DBBaseClass dbModelInstance;

    public DatabaseJTableModel(DBBaseClass dbModelInstance) throws SQLException {
        super();
        this.dbModelInstance = dbModelInstance;
        this.getTableColumns();
        this.getTableContents();
    }

    private Connection createDBConnection() throws SQLException {
        return new DatabaseConnection().getDatabaseConnection();
    }

    protected void getTableColumns() throws SQLException{
        Connection dbConnection = this.createDBConnection();
        DatabaseMetaData meta = dbConnection.getMetaData();

        ResultSet results = meta.getColumns(
                null, null, this.dbModelInstance.getTableName(), null
        );

        ArrayList<String> colNamesList = new ArrayList<>();
        ArrayList<Class> colClassesList = new ArrayList<>();

        while (results.next()) {
            String columnName = results.getString("COLUMN_NAME");
            columnName = columnName.substring(0,1).toUpperCase() + columnName.substring(1);

            if (columnName.equals("Id"))
                continue;

            colNamesList.add(columnName);

            int dbType = results.getInt("DATA_TYPE");

            switch (dbType) {
                case Types.INTEGER:
                    if (columnName.equals("City"))
                        colClassesList.add(String.class);
                    else if (columnName.equals("Person"))
                        colClassesList.add(String.class);
                    else if (columnName.equals("Hotel"))
                        colClassesList.add(String.class);
                    else
                        colClassesList.add(Integer.class); break;
                case Types.FLOAT:
                    colClassesList.add(Float.class); break;
                case Types.DOUBLE:
                case Types.REAL:
                    colClassesList.add(Double.class); break;
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    colClassesList.add(java.sql.Date.class); break;
                default:
                    colClassesList.add(String.class); break;
            }
        }
        this.columnNames = new String[colNamesList.size()];
        colNamesList.toArray(this.columnNames);

        this.columnClasses = new Class[colClassesList.size()];
        colClassesList.toArray(this.columnClasses);
        dbConnection.close();
    }

    public void refreshTableContent() {
        try {
            this.getTableContents();
            this.fireTableDataChanged();
        } catch (SQLException exception) {
            System.err.println(exception);
        }
    }
    protected void getTableContents() throws SQLException {
        ResultSet results;
        Connection dbConnection = this.createDBConnection();

        results = this.dbModelInstance.selectAll(dbConnection);

        ArrayList rowList = new ArrayList();
        while (results.next()) {
            ArrayList cellList = new ArrayList();
            for (int i = 0; i< this.columnClasses.length; i++) {
                Object cellValue = null;

                if (this.columnClasses[i] == String.class)
                    cellValue = results.getString(columnNames[i]);
                else if (this.columnClasses[i] == Integer.class)
                    cellValue = new Integer(results.getInt(this.columnNames[i]));
                else if (this.columnClasses[i] == Float.class)
                    cellValue = new Float(results.getInt(this.columnNames[i]));
                else if (this.columnClasses[i] == Double.class)
                    cellValue = new Double(results.getDouble(this.columnNames[i]));
                else if (this.columnClasses[i] == java.sql.Date.class)
                    cellValue = results.getDate (this.columnNames[i]);
                else
                    System.out.println ("Can't assign " + this.columnNames[i]);
                cellList.add(cellValue);
            }
            Object[] cells = cellList.toArray();
            rowList.add(cells);
        }
        if (rowList.size() == 0) {
            this.contents = new Object[1][1];
            this.contents[0] = new String[this.columnNames.length];
        }
        else {
            this.contents = new Object[rowList.size()][];
            for (int i = 0; i < this.contents.length; i++)
                this.contents[i] = (Object[]) rowList.get(i);
        }

        results.close();
        dbConnection.close();
    }

    public int getRowCount() {
        return contents.length;
    }

    public int getColumnCount() {
        if (contents.length == 0)
            return 0;
        else
            return contents[0].length;
    }

    public Object getValueAt(int row, int column) {
        return contents[row][column];
    }

    public Class getColumnClass(int col) {
        return columnClasses[col];
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }
}