package de.randomwords.modbus.packet;

import de.randomwords.modbus.ModBusFunctionCodes;
import de.randomwords.modbus.exception.ModBusCommunicationException;

/**
 * Created by Ronny Polley on 13.01.2017.
 */
public abstract class ModBusDataPacketResponse<T extends Object> {

    private byte[] header, payload;

    public ModBusDataPacketResponse(byte[] header, byte[] payload) throws ModBusCommunicationException {
        if (header.length != 6) {
            throw new ModBusCommunicationException("Wrong length of header. Should be 6 but was " + header.length);
        }
        if (payload.length <= 3) {
            throw new ModBusCommunicationException("Wrong length of payload. Should be at least 3 but was " + payload.length);
        }
        this.header = header;
        this.payload = payload;
    }

    public int getTransactionId() {
        return header[0] & 0xFFFF | header[1] << 16;
    }

    public int getPayloadLength() {
        return header[5];
    }

    public byte[] getPayload() {
        return payload;
    }

    public ModBusFunctionCodes getFunctionCode() throws ModBusCommunicationException {
        for (ModBusFunctionCodes functionCode : ModBusFunctionCodes.values()) {
            if (functionCode.getCode() == header[1]) {
                return functionCode;
            }
        }
        return null;
    }

    public byte getWordCount() {
        return payload[2];
    }

    public abstract T getValue() throws ModBusCommunicationException;
}
