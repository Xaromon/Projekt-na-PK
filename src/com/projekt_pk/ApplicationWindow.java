package com.projekt_pk;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;


public class ApplicationWindow extends JFrame {

    private DatabaseConnection dbConnection;
    private JTabbedPane tabbedPane;
    private JToolBar toolBar;
    private TableModel cityModel;

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

        this.toolBar = new JToolBar("Action toolbar");
        this.toolBar.setFloatable(false);
        this.tabbedPane = new JTabbedPane();
        try {
            this.prepareCityViews();
        } catch (SQLException exception) {
            System.err.println("Cannot run application");
            System.exit(1);
        }


        this.add(this.toolBar, BorderLayout.NORTH);
        this.add(this.tabbedPane, BorderLayout.CENTER);
    }

    private void prepareCityViews() throws SQLException{
        this.cityModel = new DatabaseJTableModel(new City());
        JTable cityTable = new JTable(this.cityModel);
        JScrollPane scrollPane = new JScrollPane(cityTable);
        this.tabbedPane.addTab("Cities", null, scrollPane, "Cities");

        this.toolBar.add(this.createActionButton(
                "", "AddCity", "Add new city...", "Add City", new AddNewCity(this)
        ));
    }

    public TableModel getCityTableModel() {
        return this.cityModel;
    }

    private JButton createActionButton(
            String imageName, String actionCommand, String toolTipText, String altText, ActionListener actionListener
    ) {
        String imgLocation = "images/" + imageName;
        URL imageURL = ApplicationWindow.class.getResource(imgLocation);

        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(actionListener);

        if (imageURL != null) {
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
        }

        return button;
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


class AddNewCity implements ActionListener {

    private ApplicationWindow mainWindowReference;

    public AddNewCity(ApplicationWindow mainWindowReference) {
        this.mainWindowReference = mainWindowReference;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("AddCity")) {
            String cityName = (String) JOptionPane.showInputDialog(
                this.mainWindowReference,
                "Please enter name of the city: ",
                "Add new city",
                JOptionPane.PLAIN_MESSAGE
            );
            try {
                Connection dbConn = new DatabaseConnection().getDatabaseConnetion();
                new City().insertNewCity(dbConn, cityName);
                dbConn.close();
            }
            catch (SQLException exception) {
                System.err.println("Cannot create a new city." + exception.getMessage());
            }
            TableModel cityModel = this.mainWindowReference.getCityTableModel();
        }
    }
}
