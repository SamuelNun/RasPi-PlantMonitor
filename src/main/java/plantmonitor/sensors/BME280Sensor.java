package plantmonitor.sensors;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BME280Sensor {

    // Reads temperature from Python script
    public float readTemperature() {
        return readFromPython("scripts/read_Temperature.py");
    }

    // Reads humidity from Python script
    public float readHumidity() {
        return readFromPython("scripts/read_Humidity.py");
    }

    // Generic method to run Python and parse float result
    private float readFromPython(String scriptName) {
        try {
            ProcessBuilder pb = new ProcessBuilder("python3", scriptName);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();

            if (line != null) {
                return Float.parseFloat(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -999; // Return error code if failed
    }
}