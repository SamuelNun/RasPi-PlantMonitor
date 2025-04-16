
package plantmonitor;

public class Plant {
    private int plantNum;
    private String name;
    private float minTemp;
    private float maxTemp;
    private float minHumidity;
    private float maxHumidity;
    private int minSoilMoisture;
    private int maxSoilMoisture;
    
    private int channel;
    
    ////////////////// PLANT CONSTRUCTOR //////////////////
    public Plant(int plantNum,String name, float minTemp, 
            float maxTemp, float minHumidity, 
            float maxHumidity, int minSoilMoisture, 
            int maxSoilMoisture) {
        
        this.plantNum = plantNum;
        this.name = name;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
        this.minSoilMoisture = minSoilMoisture;
        this.maxSoilMoisture = maxSoilMoisture;
        this.channel = plantNum - 1;
        
    }
    
    ///////////////////////// METHODS TO GET DATA FROM CLASS ////////////////////
    public int getPlantNum() {
        return plantNum;
    }
    
    public String getName() {
        return name;
    }
    
    public float getMinTemp() {
        return minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public float getMinHumidity() {
        return minHumidity;
    }

    public float getMaxHumidity() {
        return maxHumidity;
    }

    public int getMinSoilMoisture() {
        return minSoilMoisture;
    }

    public int getMaxSoilMoisture() {
        return maxSoilMoisture;
    }
    
    public int getChannel() {
        return channel;
    }
    
}
