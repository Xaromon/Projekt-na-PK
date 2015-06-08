package com.projekt_pk;


import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseJTableModel extends AbstractTableModel {
    Object[][] contents;
    String[] columnNames;
    Class[] columnClasses;

    public DatabaseJTableModel(Connection conn, String tableName)  throws SQLException {
        super();
        getTableContents(conn, tableName);
    }

    protected void getTableContents(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();

        ResultSet results = meta.getColumns(null, null, tableName, null) ;
        ArrayList<String> colNamesList = new ArrayList<>();
        ArrayList colClassesList = new ArrayList();

        while (results.next()) {
            String columnName = results.getString("COLUMN_NAME");
            columnName = columnName.substring(0,1).toUpperCase() + columnName.substring(1);
            if (columnName.equals("Id"))
                continue;
            colNamesList.add(columnName);
            int dbType = results.getInt("DATA_TYPE");
            switch (dbType) {
                case Types.INTEGER:
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
            System.out.println ("type: " + results.getInt("DATA_TYPE"));
        }
        columnNames = new String[colNamesList.size()];
        colNamesList.toArray(columnNames);
        columnClasses = new Class [colClassesList.size()];
        colClassesList.toArray (columnClasses);

        Statement statement = conn.createStatement ();
        results = statement.executeQuery("SELECT * FROM " + tableName);
        ArrayList rowList = new ArrayList();
        while (results.next()) {
            ArrayList cellList = new ArrayList();
            for (int i = 0; i<columnClasses.length; i++) {
                Object cellValue = null;


                if (columnClasses[i] == String.class)
                    cellValue = results.getString (columnNames[i]);
                else if (columnClasses[i] == Integer.class)
                    cellValue = new Integer (
                            results.getInt (columnNames[i]));
                else if (columnClasses[i] == Float.class)
                    cellValue = new Float (
                            results.getInt (columnNames[i]));
                else if (columnClasses[i] == Double.class)
                    cellValue = new Double (
                            results.getDouble (columnNames[i]));
                else if (columnClasses[i] == java.sql.Date.class)
                    cellValue = results.getDate (columnNames[i]);
                else
                    System.out.println ("Can't assign " +
                            columnNames[i]);
                cellList.add (cellValue);
            }// for
            Object[] cells = cellList.toArray();
            rowList.add (cells);

        } // while
        // finally create contents two-dim array
        contents = new Object[rowList.size()] [];
        for (int i=0; i<contents.length; i++)

            contents[i] = (Object []) rowList.get (i);
        System.out.println ("Created model with " + contents.length + " rows");

        // close stuff
        results.close();
        statement.close();
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
        return contents [row][column];
    }

    public Class getColumnClass(int col) {
        return columnClasses [col];
    }

    public String getColumnName(int col) {
        return columnNames [col];
    }
}