package de.randomwords.pluggit.packets;

import de.randomwords.modbus.exception.ModBusCommunicationException;
import de.randomwords.modbus.packet.ModBusDataPacketResponse;

import java.nio.ByteBuffer;

/**
 * Created by Ronny Polley on 23.01.2017.
 */
public class PluggitFloatModBusResponse extends ModBusDataPacketResponse<Float> {

    public PluggitFloatModBusResponse(byte[] header, byte[] payload) throws ModBusCommunicationException {
        super(header, payload);
    }

    @Override
    public Float getValue() throws ModBusCommunicationException {
        if (getPayloadLength() < 7) {
            throw new ModBusCommunicationException("payload to small. Should be at least 7, but was " + getPayloadLength());
        }
        return ByteBuffer.allocate(4).put(getPayload()[5]).put(getPayload()[6]).put(getPayload()[3]).put(getPayload()[4]).getFloat();
    }
}
