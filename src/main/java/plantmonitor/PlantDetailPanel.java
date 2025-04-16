package plantmonitor;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.None;


////////////////////// THIS PANEL DISPLATS DETAILED DATA //////////////////////
public class PlantDetailPanel extends JPanel {
    
    private final Plant plant;
    private final Runnable onBack; // CREATES A RUNNABLE TO GO BACK TO DASHBOARD
    private final String csvFilePath;
    
    ///////// DROPDOWN INPUT /////////
    private JComboBox<String> dataTypeDropdown;
    
    ///////// GRAPH OBJECT /////////
    private XYChart chart;
    private XChartPanel<XYChart> chartPanel;
    
    
    
    //////////// CONSTRUCTOR /////////////
    public PlantDetailPanel(Plant plant, Runnable onBack) {
        this.plant = plant;
        this.onBack = onBack;
        this.csvFilePath = String.format("data/plant%dData.csv", plant.getPlantNum());
        setupUI();
    }
    ///////////////////// USER INTERFACE //////////////////
    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        //////////////////// TITLE //////////////////
        JLabel title = new JLabel("Details for: " + plant.getName(), JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        
        ///////////////////////// PLANT INFO ///////////////////////////
        JPanel plantInfoPanel = new JPanel(new GridLayout(3, 1));
        String tempText = String.format("Temp Range: %.1f°F - %.1f°F", plant.getMinTemp(), plant.getMaxTemp());
        String humidText = String.format("Humidity Range: %.0f%% - %.0f%%", plant.getMinHumidity(), plant.getMaxHumidity());
        String SoilText = String.format("Soil Moisture Range: %d - %d", plant.getMinSoilMoisture(), plant.getMaxSoilMoisture());
        
        plantInfoPanel.add(new JLabel(tempText));
        plantInfoPanel.add(new JLabel(humidText));
        plantInfoPanel.add(new JLabel(SoilText));
        
        
        ///////////////////////// DROPDOWN ///////////////////////////
        String[] dataTypes = {"Temperature", "Humidity", "Soil Moisture"};
        dataTypeDropdown = new JComboBox<>(dataTypes);
        JPanel dropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dropdownPanel.add(new JLabel("Select: "));
        dropdownPanel.add(dataTypeDropdown);
        
        dataTypeDropdown.addActionListener(e -> updateChart());
        
        //////////////////// CHART SETUP //////////////////
        chart = new XYChartBuilder()
            .width(600).height(300)
            .title("Plant Data")
            .xAxisTitle("Time")
            .yAxisTitle("Value")
            .build();
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setMarkerSize(4);
        chartPanel = new XChartPanel<>(chart);

        
        ///////////// BACK BUTTON /////////////////
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> onBack.run());

        ////////////////// COMBINE ALL COMPONENTS //////////////////////

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(plantInfoPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(dropdownPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(chartPanel);
        
        //////////////BACKROUND COLOR CHANGE //////////////////
        Color backgroundColor = new Color(245, 255, 250); // LIGHT MINTISH
        setBackground(backgroundColor);;
        mainPanel.setBackground(backgroundColor);
        dropdownPanel.setBackground(backgroundColor);
        plantInfoPanel.setBackground(backgroundColor);
        chartPanel.setBackground(backgroundColor);
        
        ////////////// ADD TO LAYOUT ///////////
        add(mainPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
        
        /////// UPDATES THE CHART DATA ///////////
        updateChart();
    }
    
   
    
    ////////////////// READS THE CSV AND UPDATES BASED ON DROPDOWN //////////////////
    private void updateChart() {
        String selected = (String) dataTypeDropdown.getSelectedItem();
        if (selected == null) return;
        
        //////// SET COLUMN NAME ////////////
        String columnName;
        if ("Temperature".equals(selected)) {
            columnName = "Temperature";
        } else if ("Humidity".equals(selected)) {
            columnName = "Humidity";
        } else if ("Soil Moisture".equals(selected)) {
            columnName = "SoilMoisture";
        } else {
            columnName = "";
        }
        
        
        List<Date> times = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        
        ////////////////////FORMATS TIME /////////////////////
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) { // READ LINE BY LINE
            String header = br.readLine(); ///// READS THE HEADER LINE
            if (header == null) return;

            ///////////////// SPLITS THE HEADER /////////////////
            String[] headers = header.split(",");
            int timeIndex = Arrays.asList(headers).indexOf("Timestamp"); //// GETS INDEX OF X VALUE
            int dataIndex = Arrays.asList(headers).indexOf(columnName); ///// GETS INDEX OF Y VALUE

            ///////////////////// READS DATA FROM CSV FILE ///////////////
            String line;
            while ((line = br.readLine()) != null) { ////// WHILE NOT AT THE END OF THE CSV
                String[] tokens = line.split(",");  // SPLITS THE ROW BY COLUMNS
                
                /////// GRAB TIME VALUE FROM ROW ////////////////
                String time = tokens[timeIndex].trim();
                double value = Double.parseDouble(tokens[dataIndex].trim());

                ///////////////// CONVERTS TIME INTO A TIME VALUE /////////////////
                try {
                    Date timeParsed = timeFormat.parse(tokens[timeIndex].trim());
                    times.add(timeParsed); ////// THIS IS THE X AXIS OF THE CHART
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                values.add(value); /// THIS IS THE Y AXIS OF THE CHART
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        /////////////// UPDATES CHART /////////////////////
        chart.removeSeries("Data"); //CLEARS OLD DATA
        chart.addSeries("Data", times, values).setMarker(new None()); // ADDS NEW DATA

        chartPanel.revalidate();
        chartPanel.repaint();
        }
    
}