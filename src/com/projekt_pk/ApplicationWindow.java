package com.projekt_pk;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;


public class ApplicationWindow extends JFrame {

    public ApplicationWindow() {
        super("Tours manager");
        this.prepareGUI();
        this.pack();
        this.setVisible(true);
    }
    private void prepareGUI() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        JButton button = new JButton("Add");
        this.add(button, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) throws SQLException {
//        javax.swing.SwingUtilities.invokeLater(
//            new Runnable() {
//                @Override
//                public void run() {
//                    new ApplicationWindow();
//                }
//            }
//        );
        DatabaseConnection dbConnector = new DatabaseConnection();
    }
}
