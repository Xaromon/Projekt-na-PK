package com.projekt_pk;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.sql.SQLException;


public class ApplicationWindow extends JFrame {

    private DatabaseConnection dbConnection;
    private JTabbedPane tabbedPane;

    public ApplicationWindow() {
        super("Tours manager");
        try {
            this.dbConnection = new DatabaseConnection();
            this.prepareGUI();
            this.pack();
            this.setVisible(true);
        }
        catch (SQLException exception) {
            System.err.println("Cannot connect to the database");
            System.exit(1);
        }
    }
    private void prepareGUI() throws SQLException {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.tabbedPane = new JTabbedPane();
        JScrollPane cityTable = this.createJTable(new City().getTableName());

        tabbedPane.addTab("Cities", null, cityTable, "Cities");
        this.add(tabbedPane, BorderLayout.CENTER);
    }

    private JScrollPane createJTable(String tableName) throws SQLException{
        TableModel model = new DatabaseJTableModel(
            this.dbConnection.getDatabaseConnetion(), tableName
        );

        JTable cityTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(cityTable);
        return scrollPane;
    }

    public static void main(String[] args) throws SQLException {
        javax.swing.SwingUtilities.invokeLater(
            new Runnable() {
                @Override
                public void run() {
                    new ApplicationWindow();
                }
            }
        );
    }
}
