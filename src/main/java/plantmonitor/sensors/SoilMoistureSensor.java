package plantmonitor.sensors;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class SoilMoistureSensor {

    private final String scriptPath;

    /**
     * Default constructor assuming the script is named "read_soil_channel.py"
     * and located in the project root directory.
     */
    public SoilMoistureSensor() {
        this("scripts/read_SoilMoisture.py");
    }

    /**
     * Custom constructor that allows you to specify a path to the Python script.
     * @param scriptPath the path to the Python script used to read the MCP3008
     */
    public SoilMoistureSensor(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    /**
     * Reads the soil moisture value from the given MCP3008 channel (0–7).
     * @param channel the MCP3008 channel number
     * @return an integer between 0–1023 representing analog sensor reading
     */
    public int readChannel(int channel) {
        if (channel < 0 || channel > 7) {
            throw new IllegalArgumentException("Channel must be between 0 and 7");
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("python3", scriptPath, String.valueOf(channel));
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.readLine();

            if (output != null) {
                return Integer.parseInt(output.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // Indicates failure
    }
}