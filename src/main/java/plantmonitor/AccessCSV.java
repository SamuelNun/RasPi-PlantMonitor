package plantmonitor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccessCSV {

    ////////////////////////////////// CREATES A PLANT TO ADD TO DATA /////////////////////////////////
    public static void createNewPlant(String name, float minTemp, float maxTemp, float minHumid, float maxHumid, int minSoil, int maxSoil) {
        
        ///////////////// GET NEXT PLANT NUM /////////////////
        List<Plant> existingPlants = loadPlants("data/plantInfo.csv");
        int plantNumber = existingPlants.size() + 1;
        
        ///////////// CREATE & ADD PLANT OBJECT ///////////////
        Plant newPlant = new Plant(plantNumber, name, minTemp, maxTemp, minHumid, maxHumid, minSoil, maxSoil);  
        addPlant(newPlant); // RUNS METHOD TO ADD TO CSV FILE
        
        /////// CREATE PLANT DATA FILE ///////////////
        String DATAfilePath = String.format("data/plant%dData.csv", plantNumber);
        try (FileWriter writer = new FileWriter(DATAfilePath)) {
            writer.write("Timestamp,Temperature,Humidity,SoilMoisture\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////////// ADDS THE PLANT TO THE CSV FILE /////////////////////
    private static void addPlant(Plant plant){
        
        ///////////// CHECK IF FILE EXISTS ////////////
        File file = new File("data/plantInfo.csv");
        boolean fileExists = file.exists() && file.length() > 0;
        
        /////////////// ADD PLANT //////////////////////
        try (FileWriter writer = new FileWriter(file, true)){ // APPENDS 
            
            ////////////// WRITE HEADER ///////////////////////
            if (!fileExists) { // IF HEADER DOESN'T EXUST CREATES ONE
                writer.write("PlantNum,Name,MinTemp,MaxTemp,MinHumidity,MaxHumidity,MinSoil,MaxSoil\n");
            }
            
            ////////////////// ADD PLANT INFO /////////////
            writer.write(String.format("%d,%s,%.1f,%.1f,%.1f,%.1f,%d,%d\n",
                plant.getPlantNum(),
                plant.getName(),
                plant.getMinTemp(),
                plant.getMaxTemp(),
                plant.getMinHumidity(),
                plant.getMaxHumidity(),
                plant.getMinSoilMoisture(),
                plant.getMaxSoilMoisture()));
        } catch(IOException e) {
            e.printStackTrace();
        }
 
    }

    ////////////////// LOADS CSV DATA /////////////////
    public static List<Plant> loadPlants(String filePath) {
        
        /////////// LIST TO STORE PLANTS /////////////////
        List<Plant> plantList = new ArrayList<>();
        
        ////////////////////// READS FILE ///////////////////////
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // SKIPS THE HEADER
            return loadPlantsList(reader, plantList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return plantList;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    ///                                                                             ///
    ///                          REQ: RECURSIVE METHOD                              ///
    ///                                    \/                                       ///
    ///////////////////////////////////////////////////////////////////////////////////
    
    
    //////////////////////// RECURSIVE TO ADD PLANTS PLANTS TO LIST //////////////////////
    private static List<Plant> loadPlantsList(BufferedReader reader, List<Plant> plantList) throws IOException { 
        /////////////// READS THE NEXT LINE IN CSV ////////////
        String line = reader.readLine();
        if (line == null){ // END OF CSV
            return plantList;
        } 
        /////////////////// SPLIT THE STRING INTO COLUMNS ////////////////////
        String[] column = line.split(","); // LIST TO STORE COLUMN DATA
        
        ///////////////// CONVERTS DATA INTO ITS DATA VALUES ////////////////////
        int plantNum = Integer.parseInt(column[0]);
        String name = column[1];
        float minTemp = Float.parseFloat(column[2]);
        float maxTemp = Float.parseFloat(column[3]);
        float minHumid = Float.parseFloat(column[4]);
        float maxHumid = Float.parseFloat(column[5]);
        int minSoil = Integer.parseInt(column[6]);
        int maxSoil = Integer.parseInt(column[7]);
        
        ////////// CREATES A PLANT OBJECT WITH DATA ///////////////
        Plant plant = new Plant(plantNum, name, minTemp, maxTemp, minHumid, maxHumid, minSoil, maxSoil);
        plantList.add(plant); ///// ADDS TO THE LOAD LIST
        
        /////////////RECURSIVE CALL ///////////////////
        return loadPlantsList(reader, plantList);
    }
}
