package com.projekt_pk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;


public class ApplicationWindow extends JFrame {

    private JTabbedPane tabbedPane;
    private JToolBar toolBar;
    private DatabaseJTableModel cityModel;
    private DatabaseJTableModel hotelModel;
    private DatabaseJTableModel personModel;
    private DatabaseJTableModel toursModel;

    public ApplicationWindow() {
        super("Tours manager");
        try {
            new DatabaseConnection();
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
            this.preparePersonViews();
            this.prepareHotelViews();
            this.prepareCityViews();
            this.prepareTourViews();
        } catch (SQLException exception) {
            System.err.println("Cannot run application " + exception);
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

    private void prepareHotelViews() throws SQLException {
        this.hotelModel = new DatabaseJTableModel(new Hotel());
        JTable hotelTable = new JTable(this.hotelModel);
        JScrollPane scrollPane = new JScrollPane(hotelTable);
        this.tabbedPane.addTab("Hotels", null, scrollPane, "Hotels");

        this.toolBar.add(this.createActionButton(
                "", "AddHotel", "Add new hotel...", "Add Hotel", new AddNewHotel(this)
        ));
    }

    private void preparePersonViews() throws SQLException {
        this.personModel = new DatabaseJTableModel(new Person());
        JTable personTable = new JTable(this.personModel);
        JScrollPane scrollPane = new JScrollPane(personTable);
        this.tabbedPane.addTab("Persons", null, scrollPane, "Persons");

        this.toolBar.add(this.createActionButton(
                "", "AddPerson", "Add new person...", "Add Person", new AddNewPerson(this)
        ));
    }

    private void prepareTourViews() throws SQLException {
        this.toursModel = new DatabaseJTableModel(new Tour());
        JTable toursTable = new JTable(this.toursModel);
        JScrollPane scrollPane = new JScrollPane(toursTable);
        this.tabbedPane.addTab("Tours", null, scrollPane, "Tours");

        this.toolBar.add(this.createActionButton(
                "", "AddTour", "Add new tour...", "Add Tour", new AddNewTour(this)
        ));
    }

    public DatabaseJTableModel getCityTableModel() {
        return this.cityModel;
    }

    public DatabaseJTableModel getHotelTableModel() {
        return this.hotelModel;
    }

    public DatabaseJTableModel getPersonTableModel() {
        return this.personModel;
    }

    public DatabaseJTableModel getTourTableModel() {
        return this.toursModel;
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
            String cityName = JOptionPane.showInputDialog(
                this.mainWindowReference,
                "Please enter name of the city: ",
                "Add new city",
                JOptionPane.PLAIN_MESSAGE
            );
            if (cityName != null) {
                try {
                    Connection dbConn = new DatabaseConnection().getDatabaseConnection();
                    new City().insertNewCity(dbConn, cityName);
                    dbConn.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(
                        this.mainWindowReference,
                        "Cannot create new city, please check name.",
                        "Cannot create a new City",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
                DatabaseJTableModel cityModel = this.mainWindowReference.getCityTableModel();
                cityModel.refreshTableContent();
            }
        }
    }
}


class AddNewHotel implements ActionListener {

    private ApplicationWindow mainWindowReference;

    public AddNewHotel(ApplicationWindow mainWindowReference) {
        this.mainWindowReference = mainWindowReference;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Hotel dbModel = new Hotel();

        JTextField hotelName = new JTextField();
        JTextField hotelAddress = new JTextField();
        JTextField hotelPrice = new JTextField();
        JComboBox<String> hotelCity = new JComboBox(new City().getComboBoxModel());

        final JComponent[] inputs = new JComponent[] {
            new JLabel("Name:"),
            hotelName,
            new JLabel("Address:"),
            hotelAddress,
            new JLabel("Price:"),
            hotelPrice,
            new JLabel("City:"),
            hotelCity
        };

        int status = JOptionPane.showConfirmDialog(
            this.mainWindowReference,
            inputs,
            "Add new Hotel",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        if (status == 0) {
            ComboBoxDatabaseModel comboBoxModel = (ComboBoxDatabaseModel) hotelCity.getModel();
            try {
                dbModel.insertNewHotel(
                    hotelName.getText(),
                    hotelAddress.getText(),
                    hotelPrice.getText(),
                    (String) comboBoxModel.getDatabaseId()
                );
            } catch (SQLException exception) {
                JOptionPane.showMessageDialog(
                        this.mainWindowReference,
                        "Cannot create new Hotel, please check entered values.",
                        "Cannot create a new Hotel",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            DatabaseJTableModel hotelModel = this.mainWindowReference.getHotelTableModel();
            hotelModel.refreshTableContent();
        }
    }
}


class AddNewPerson implements ActionListener {

    private ApplicationWindow mainWindowReference;

    public AddNewPerson(ApplicationWindow mainWindowReference) {
        this.mainWindowReference = mainWindowReference;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Person dbModel = new Person();

        JTextField personName = new JTextField();
        JTextField personSurname = new JTextField();
        JTextField personAddress = new JTextField();
        JComboBox<String> personCity = new JComboBox(new City().getComboBoxModel());

        final JComponent[] inputs = new JComponent[] {
                new JLabel("Name:"),
                personName,
                new JLabel("Surname:"),
                personSurname,
                new JLabel("Address:"),
                personAddress,
                new JLabel("City:"),
                personCity
        };

        int status = JOptionPane.showConfirmDialog(
                this.mainWindowReference,
                inputs,
                "Add new Person",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (status == 0) {
            ComboBoxDatabaseModel comboBoxModel = (ComboBoxDatabaseModel) personCity.getModel();
            try {
                dbModel.insertNewPerson(
                        personName.getText(),
                        personSurname.getText(),
                        personAddress.getText(),
                        (String) comboBoxModel.getDatabaseId()
                );
            } catch (SQLException exception) {
                JOptionPane.showMessageDialog(
                        this.mainWindowReference,
                        "Cannot create new person, please check entered values.",
                        "Cannot create a new Person",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            DatabaseJTableModel personModel = this.mainWindowReference.getPersonTableModel();
            personModel.refreshTableContent();
        }
    }
}

class AddNewTour implements ActionListener {


    private ApplicationWindow mainWindowReference;

    public AddNewTour(ApplicationWindow mainWindowReference) {
        this.mainWindowReference = mainWindowReference;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Tour dbModel = new Tour();

        JTextField tourName = new JTextField();
        JTextField tourDescripton = new JTextField();
        JComboBox<String> tourPerson = new JComboBox(new Person().getComboBoxModel());
        JComboBox<String> tourCity = new JComboBox(new City().getComboBoxModel());
        JComboBox<String> tourHotel = new JComboBox(new Hotel().getComboBoxModel());

        final JComponent[] inputs = new JComponent[] {
                new JLabel("Name:"),
                tourName,
                new JLabel("Description:"),
                tourDescripton,
                new JLabel("Person:"),
                tourPerson,
                new JLabel("City:"),
                tourCity,
                new JLabel("Hotel:"),
                tourHotel
        };

        int status = JOptionPane.showConfirmDialog(
                this.mainWindowReference,
                inputs,
                "Add new Tour",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (status == 0) {
            ComboBoxDatabaseModel personBoxModel = (ComboBoxDatabaseModel) tourPerson.getModel();
            ComboBoxDatabaseModel cityBoxModel = (ComboBoxDatabaseModel) tourCity.getModel();
            ComboBoxDatabaseModel hotelBoxModel = (ComboBoxDatabaseModel) tourHotel.getModel();
            try {
                dbModel.insertNewTour(
                        tourName.getText(),
                        tourDescripton.getText(),
                        (String) hotelBoxModel.getDatabaseId(),
                        (String) personBoxModel.getDatabaseId(),
                        (String) cityBoxModel.getDatabaseId()
                );
            } catch (SQLException exception) {
                JOptionPane.showMessageDialog(
                        this.mainWindowReference,
                        "Cannot create new tour, please check entered values.",
                        "Cannot create a new Tour",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            DatabaseJTableModel toursModel = this.mainWindowReference.getTourTableModel();
            toursModel.refreshTableContent();
        }
    }
}
