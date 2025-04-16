package plantmonitor;

import javax.swing.*;
import java.awt.*;
import plantmonitor.AccessCSV;

/////////////////////////  USER INTERFACE TO CREATE NEW PLANT ///////////////////
public class PlantCreationPanel extends JPanel {

    public PlantCreationPanel(Runnable onBack) {
        ////////// MAIN LAYOUT ///////////
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        //////////////////// TITLE /////////////////
        JLabel title = new JLabel("Add New Plant", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        ///////////////// USER INPUT /////////////////////
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JTextField nameField = new JTextField();
        JTextField minTempField = new JTextField();
        JTextField maxTempField = new JTextField();
        JTextField minHumidField = new JTextField();
        JTextField maxHumidField = new JTextField();
        JTextField minSoilField = new JTextField();
        JTextField maxSoilField = new JTextField();

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Min Temp (°C):"));
        formPanel.add(minTempField);

        formPanel.add(new JLabel("Max Temp (°C):"));
        formPanel.add(maxTempField);

        formPanel.add(new JLabel("Min Humidity (%):"));
        formPanel.add(minHumidField);

        formPanel.add(new JLabel("Max Humidity (%):"));
        formPanel.add(maxHumidField);

        formPanel.add(new JLabel("Min Soil Moisture:"));
        formPanel.add(minSoilField);

        formPanel.add(new JLabel("Max Soil Moisture:"));
        formPanel.add(maxSoilField);

        add(formPanel, BorderLayout.CENTER);

        ///////////// ADD THE CREATE AND BACK BUTTON ///////////////
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createButton = new JButton("Create");
        JButton backButton = new JButton("Back");

        buttonPanel.add(createButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        ////////////////// WHEN CREATE IS CLICKED ///////////////
        createButton.addActionListener(e -> {
            ////////////// CONVERTS INPUTS INTO DATA TYPES //////////////
            String name = nameField.getText().trim();
            float minTemp = Float.parseFloat(minTempField.getText().trim());
            float maxTemp = Float.parseFloat(maxTempField.getText().trim());
            float minHumid = Float.parseFloat(minHumidField.getText().trim());
            float maxHumid = Float.parseFloat(maxHumidField.getText().trim());
            int minSoil = Integer.parseInt(minSoilField.getText().trim());
            int maxSoil = Integer.parseInt(maxSoilField.getText().trim());

            ////////// ADDS THE PLANT TO THE CSV FILE ////////////////////
            AccessCSV.createNewPlant(name,minTemp,maxTemp,minHumid,maxHumid,minSoil,maxSoil);
            onBack.run(); // RETURNS TO PREVIOUS PAGE
        });

        backButton.addActionListener(e -> onBack.run()); // RETURNS TO PREVIOUS PAGE
    }
}