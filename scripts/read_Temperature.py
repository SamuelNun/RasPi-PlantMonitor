import smbus
import time

# BME280 default address.
BME280_I2C_ADDRESS = 0x76

# Register addresses
REG_ID = 0xD0
REG_RESET = 0xE0
REG_CTRL_HUM = 0xF2
REG_STATUS = 0xF3
REG_CTRL_MEAS = 0xF4
REG_CONFIG = 0xF5
REG_TEMP_MSB = 0xFA

# Calibration register base
CALIBRATION_REG = 0x88

bus = smbus.SMBus(1)

def read_unsigned_16_le(reg):
    lsb = bus.read_byte_data(BME280_I2C_ADDRESS, reg)
    msb = bus.read_byte_data(BME280_I2C_ADDRESS, reg + 1)
    return msb << 8 | lsb

def read_signed_16_le(reg):
    result = read_unsigned_16_le(reg)
    if result > 32767:
        result -= 65536
    return result

def read_calibration_data():
    calib = {}
    calib['T1'] = read_unsigned_16_le(0x88)
    calib['T2'] = read_signed_16_le(0x8A)
    calib['T3'] = read_signed_16_le(0x8C)
    return calib

def compensate_temperature(adc_T, calib):
    var1 = (((adc_T / 16384.0) - (calib['T1'] / 1024.0)) * calib['T2'])
    var2 = ((((adc_T / 131072.0) - (calib['T1'] / 8192.0)) *
             ((adc_T / 131072.0) - (calib['T1'] / 8192.0))) * calib['T3'])
    t_fine = var1 + var2
    temperature = t_fine / 5120.0
    return temperature

def read_raw_temperature():
    # Read raw temp registers (MSB, LSB, XLSB)
    data = bus.read_i2c_block_data(BME280_I2C_ADDRESS, REG_TEMP_MSB, 3)
    adc_T = (data[0] << 12) | (data[1] << 4) | (data[2] >> 4)
    return adc_T

# Initialize sensor
bus.write_byte_data(BME280_I2C_ADDRESS, REG_CTRL_HUM, 0x01)
bus.write_byte_data(BME280_I2C_ADDRESS, REG_CTRL_MEAS, 0x27)
bus.write_byte_data(BME280_I2C_ADDRESS, REG_CONFIG, 0xA0)
time.sleep(0.1)

# Read calibration and temperature
calib = read_calibration_data()
adc_T = read_raw_temperature()
temp = compensate_temperature(adc_T, calib)
temp = (temp*9/5)+32

# Print only the temperature so Java can read it
print(round(temp, 2))
