
package plantmonitor;

public class Plant {
    private String name;
    private float minTemp;
    private float maxTemp;
    private float minHumidity;
    private float maxHumidity;
    private float minSoilMoisture;
    private float maxSoilMoisture;
    private float currentSoilMoisture;
    
    public Plant(String name, float minTemp, 
            float maxTemp, float minHumidity, 
            float maxHumidity, float minSoilMoisture, 
            float maxSoilMoisture, float initialSoilMoisture) {
        
        this.name = name;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
        this.minSoilMoisture = minSoilMoisture;
        this.maxSoilMoisture = maxSoilMoisture;
        this.currentSoilMoisture = initialSoilMoisture;
        
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

    public float getMinSoilMoisture() {
        return minSoilMoisture;
    }

    public float getMaxSoilMoisture() {
        return maxSoilMoisture;
    }

    public float getCurrentSoilMoisture() {
        return currentSoilMoisture;
    }

    public void setCurrentSoilMoisture(float moisture) {
        this.currentSoilMoisture = moisture;
    }
}
