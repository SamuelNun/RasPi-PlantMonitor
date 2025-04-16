package plantmonitor.sensors;

import com.pi4j.io.spi.Spi;
import com.pi4j.io.spi.SpiChipSelect;
import com.pi4j.context.Context;
import com.pi4j.Pi4J;

public class SoilMoistureSensor {
    private final Spi spi;

    public SoilMoistureSensor() {
        Context pi4j = Pi4J.newAutoContext();
        spi = pi4j.create(Spi.newConfigBuilder(pi4j)
                .id("MCP3008")
                .bus(0)
                .chipSelect(SpiChipSelect.CS_0)
                .build());
    }

    public int readChannel(int channel) {
        // MCP3008 communication logic
        if (channel < 0 || channel > 7) throw new IllegalArgumentException("Invalid channel");

        byte[] txBuffer = new byte[3];
        txBuffer[0] = 0b00000001; // Start bit
        txBuffer[1] = (byte)((8 + channel) << 4); // Single-ended + channel number
        txBuffer[2] = 0;

        byte[] rxBuffer = new byte[3]; // Will store the response

        spi.transfer(txBuffer, rxBuffer, 3); // Send request, receive response

        int result = ((rxBuffer[1] & 0b00000011) << 8) | (rxBuffer[2] & 0xFF);
        return result;
    }

    
}