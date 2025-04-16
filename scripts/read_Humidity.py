import smbus

bus = smbus.SMBus(1)
address = 0x76

# Read raw humidity data from registers
hum_msb = bus.read_byte_data(address, 0xFD)
hum_lsb = bus.read_byte_data(address, 0xFE)

raw_hum = (hum_msb << 8) | hum_lsb

# Quick approximate conversion
humidity = raw_hum / 1024.0
print(f"{humidity:.2f}")