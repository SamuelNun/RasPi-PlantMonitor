# read_soil_channel.py
import spidev
import sys

def read_channel(channel):
    if not 0 <= channel <= 7:
        raise ValueError("Channel must be between 0 and 7")

    # Setup SPI
    spi = spidev.SpiDev()
    spi.open(0, 0)  # Bus 0, CE0
    spi.max_speed_hz = 1350000

    # MCP3008 protocol
    tx = [1, (8 + channel) << 4, 0]
    rx = spi.xfer2(tx)

    result = ((rx[1] & 0b11) << 8) | rx[2]
    return result

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python3 read_soil_channel.py <channel_number>")
        sys.exit(1)

    try:
        channel = int(sys.argv[1])
        value = read_channel(channel)
        print(value)
    except ValueError as ve:
        print(f"Error: {ve}")
        sys.exit(1)
