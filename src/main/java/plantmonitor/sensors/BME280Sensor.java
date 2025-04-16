package plantmonitor.sensors;

import com.pi4j.io.i2c.I2C;
import com.pi4j.context.Context;
import com.pi4j.Pi4J;

public class BME280Sensor {
    private final I2C device;

    public BME280Sensor() {
        Context pi4j = Pi4J.newAutoContext();
        device = pi4j.create(I2C.newConfigBuilder(pi4j)
                .bus(1) // I2C bus 1
                .device(0x76) // BME280 address (can also be 0x77)
                .id("BME280")
                .build());
        
        initializeSensor();
    }

    private void initializeSensor() {
        // Humidity oversampling x1
        device.writeRegister(0xF2, (byte) 0x01);
        // Temp + pressure oversampling x1, mode = normal
        device.writeRegister(0xF4, (byte) 0x27);
        // Config: standby = 1000ms, filter off
        device.writeRegister(0xF5, (byte) 0xA0);
    }
      
    public float readTemperature() {
        byte[] tempData = new byte[3];
        device.readRegister(0xFA, tempData); // Read temp regs FA, FB, FC

        int rawTemp = ((tempData[0] & 0xFF) << 12) | ((tempData[1] & 0xFF) << 4) | ((tempData[2] & 0xF0) >> 4);

        // Quick approximate calculation (not Bosch-compensated)
        return rawTemp / 100.0f;

    }

    public float readHumidity() {
        byte[] humData = new byte[2];
        device.readRegister(0xFD, humData); // Read humidity regs FD, FE

        int rawHum = ((humData[0] & 0xFF) << 8) | (humData[1] & 0xFF);

        // Approximate scale
        return rawHum / 1024.0f;
    }
}