
package plantmonitor;


////// VERY IMPORTANT GUI LIBRARIES /////
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.Timer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

////////////// CLASS IMPORTS //////////
import plantmonitor.AccessCSV;
import plantmonitor.sensors.BME280Sensor;
import plantmonitor.sensors.SoilMoistureSensor;


////////////////////////// MAIN GUI ///////////////////////
public class DashboardPanel extends JPanel {

    ///////// UI COMPONENTS //////////
    private JPanel plantGrid;
    private JLabel tempLabel;
    private JLabel humidLabel;
    private JLabel timeLabel;
    

    ///// LIST OF PLANTS//////
    private List<Plant> plantList;
    private List<JLabel> moistureLabels = new ArrayList<>();
    
    
    ///////// SENSORS ////////////
    private SoilMoistureSensor soilSensor;
    private BME280Sensor sensor;
    
    public DashboardPanel() {
        /////////// MAIN LAYOUT //////////////
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        /////// INIT SENSORS ///////////
        sensor = new BME280Sensor();
        soilSensor= new SoilMoistureSensor();
        
       //////////////// HEADER DISPLAY  /////////////////
        tempLabel = new JLabel("Temp: --°F");
        humidLabel = new JLabel("Humidity: --%");
        timeLabel = new JLabel("Time: --:--");
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        headerPanel.setBackground(new Color(230, 230, 250));
        
        headerPanel.add(Box.createHorizontalStrut(100));
        headerPanel.add(tempLabel);
        headerPanel.add(Box.createHorizontalStrut(100));
        headerPanel.add(humidLabel);
        headerPanel.add(Box.createHorizontalStrut(100));
        headerPanel.add(timeLabel);

        add(headerPanel, BorderLayout.NORTH);
        
        
        /////////////////// CREATES PLANT GRID //////////////////////////
        plantGrid = new JPanel(new GridLayout(2, 3, 10, 10));
        plantGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        plantGrid.setBackground(Color.WHITE);
        int totalSlots = 6; /// 2X3 GRID, MAXIMUM OF 6 SLOTS
        
        ////////////// LOADS PLANTS FROM CSV FILE ///////////////
        plantList = AccessCSV.loadPlants("data/plantInfo.csv");
        int plantCount = plantList.size(); // NUMBER OF PLANTS
        int cardsAdded = 0; // COUNTER
     
        ////////// ADDS EACH PLANT TO THE GRID ////////////
        for (Plant plant: plantList) {
            plantGrid.add(createPlantPanel(plant));
            cardsAdded++;
        }
        
        //////////////// UPDATES HEADER //////////
        updateSensor(); 
        new Timer(20000, e -> updateSensor()).start(); // UPDATES EVERY 20 SEC

        

        ////////////////////////////// CREATE NEW + PANEL ////////////////////////////////

        //////// IF THERE IS SPACE ADD CREATE CARD //////////
        if(plantCount < totalSlots) {
        //////////////// CREATES THE "CREATE NEW" CARD ///////////////////
            JPanel createNewPanel = new JPanel();
            createNewPanel.setLayout(new BorderLayout());
            createNewPanel.setBackground(new Color(240, 240, 240));
            
            JLabel plus = new JLabel("Create New +", SwingConstants.CENTER);
            plus.setFont(new Font("Arial", Font.BOLD, 14));
            createNewPanel.add(plus, BorderLayout.CENTER);
            
            plantGrid.add(createNewPanel);

            ///////////////// WHEN CLICKED, OPENS CREATION SCREEN ///////////////////
            createNewPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(DashboardPanel.this);
                    ////////////// OPENS PLANT CREATION ////////////
                    topFrame.setContentPane(new PlantCreationPanel(() -> {
                        //////// WHEN DONE, RETURNS TO A NEW DASHBOARD //////////////
                        topFrame.setContentPane(new DashboardPanel());
                        topFrame.revalidate();
                        topFrame.repaint();
                    }));
                    topFrame.revalidate();
                    topFrame.repaint();
                }
            });
            plantGrid.add(createNewPanel);
            cardsAdded++;
        }
        
        ////////// FILLS EMPTY GRID SPOTS WITH BLANK PANELS //////////////
        while (cardsAdded < totalSlots) {
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            plantGrid.add(filler);
            cardsAdded++;
}
        
        /////////// ADDS PLANT GRID TO THE DASHBOARD /////////////
        add(plantGrid, BorderLayout.CENTER);
    }
    
    ////////////////// UPDATES TEMPERATURE, HUMIDITY, AND TIME /////////////////
    private void updateSensor() {
        /////////////////// READS DATA FROM SENSORS ////////////////////
        float temp = sensor.readTemperature();
        float humid = sensor.readHumidity();
        
        /////////////////// UPDATES TEMPERATURE AND HUMIDITY ///////////////////
        tempLabel.setText(String.format("Temp: %.1f°F", temp));
        humidLabel.setText(String.format("Humidity: %.1f%%", humid));
        
        /////////////////// UPDATES TIME ///////////////////
        String currentTime = java.time.LocalTime.now().withSecond(0).withNano(0).toString();
        timeLabel.setText("Time: " + currentTime);
        
        /////////////////// UPDATES  MOISTURE FOR EACH PLANT///////////////////
        for (int i = 0; i < plantList.size(); i++) {
            int moisture = soilSensor.readChannel(plantList.get(i).getChannel());
            moistureLabels.get(i).setText("  Soil Moisture: " + moisture);
        }
    }
    
    /////////////////////// BUILDS PLANT CARDS /////////////////////
    private JPanel createPlantPanel(Plant plant) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(new Color(245, 255, 250));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        /////////////// DATA FROM PLANTS //////////////////////
        String tempText = String.format("  Temp: %.1f - %.1f °F",plant.getMinTemp(),plant.getMaxTemp());
        String humidText = String.format("  Humidity: %.1f - %.1f %%",plant.getMinHumidity(),plant.getMaxHumidity());
        String soilText = String.format("  Soil Moisture: %d - %d", plant.getMinSoilMoisture(),plant.getMaxSoilMoisture());
        String moistureText = "  Soil Moisture: ---";
        
        //////////// CREATES LABELS ////////////////
        JLabel nameLabel = new JLabel("  " + plant.getName());
        JLabel tempLabel = new JLabel(tempText);
        JLabel humidLabel = new JLabel(humidText);
        JLabel soilLabel = new JLabel(soilText);
        JLabel moistureLabel = new JLabel(moistureText);
        
        moistureLabels.add(moistureLabel);
        
        
        /////////////// ALLIGNS LABELS TO THE LEFT ///////////
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tempLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        humidLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        soilLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        moistureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        ///////////////// FORMAT THE LABELS //////////////////
        panel.add(Box.createVerticalStrut(15));
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(tempLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(humidLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(soilLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(moistureLabel);
        panel.add(Box.createVerticalGlue());

 
        //////////////////// OPEN DETAIL PAGE WHEN PLANT CARD IS CLICKED ////////////////////
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(DashboardPanel.this);
                topFrame.setContentPane(new PlantDetailPanel(plant, () -> {
                    topFrame.setContentPane(new DashboardPanel());
                    topFrame.revalidate();
                    topFrame.repaint();
                }));
                topFrame.revalidate();
                topFrame.repaint();
            }
        });
        return panel;
    }
}