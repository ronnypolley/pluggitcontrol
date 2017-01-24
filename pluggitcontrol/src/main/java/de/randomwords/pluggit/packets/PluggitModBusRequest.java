package de.randomwords.pluggit.packets;

import de.randomwords.modbus.packet.ModBusReadMultipleRegisterDataPacketRequest;

/**
 * Created by Ronny Polley on 28.12.2016.
 */
public abstract class PluggitModBusRequest extends ModBusReadMultipleRegisterDataPacketRequest {

    public static final int BASE_ADDRESS = 40001;

    public PluggitModBusRequest(int address) {
        super(address);
    }

    @Override
    public int getBaseAddress() {
        return BASE_ADDRESS;
    }

    @Override
    public byte getDataLengthInBytes() {
        return (byte) getPayload().length;
    }
}
