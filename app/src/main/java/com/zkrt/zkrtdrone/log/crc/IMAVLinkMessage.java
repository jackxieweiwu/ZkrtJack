package com.zkrt.zkrtdrone.log.crc;

/**
 * Created by root on 16-12-21.
 */
public interface IMAVLinkMessage {
    /**
     * Packet start in MAVLink V1.0
     */
    public final static byte MAVPROT_PACKET_START_V10 = (byte) 0xFE;

    /**
     * Packet start in MAVLink V0.9
     */
    public final static byte MAVPROT_PACKET_START_V09 = (byte) 0x55;

    /**
     * Len to add to payload for CRC computing
     */
    public final static int CRC_LEN = 5;

    /**
     * Use to initialize CRC before computing
     */
    public final static int X25_INIT_CRC = 0x0000ffff;

    /**
     * Use to validate CRC
     */
    public final static int X25_VALIDATE_CRC = 0x0000f0b8;
}
