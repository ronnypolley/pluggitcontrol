package de.randomwords.modbus.packet;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Ronny on 27.12.2016.
 */
public abstract class ModBusDataPacket {

    private static final Random rnd = new Random(System.currentTimeMillis());

    private final int address;

    private int transactionId;

    public ModBusDataPacket(int address) {
        this(address, rnd.nextInt());
    }

    public ModBusDataPacket(int address, int transactionId) {
        this.address = address;
        this.transactionId = transactionId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public byte[] createArrayToSend() {
        // initialize the result array
        byte[] result = new byte[10 + getDataLengthInBytes()];
        byte[] header = new byte[]{
                // set the transaction id
                (byte) (getTransactionId() >> 8),
                (byte) (getTransactionId() & 255),
                // protocol id -> set to zero
                0, 0,
                // length of the rest
                0, (byte) (getDataLengthInBytes() + 4),
                // unit number
                0,
                // modbus code function
                getFunctionCode(),
                // address to use the function at
                (byte) ((address - getBaseAddress()) >> 8),
                (byte) ((address - getBaseAddress()) & 255)};
        System.arraycopy(header, 0, result, 0, header.length);
        System.arraycopy(getPayload(), 0, result, header.length, getDataLengthInBytes());

        return result;
    }

    public abstract int getBaseAddress();

    public abstract byte[] getPayload();

    public abstract byte getFunctionCode();

    public abstract byte getDataLengthInBytes();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + Arrays.toString(createArrayToSend());
    }
}
