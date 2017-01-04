package de.randomwords.modbus;

/**
 * Created by Ronny on 27.12.2016.
 */
public enum ModBusFunctionCodes {

    ReadHoldingRegisters((byte) 0x03),
    WriteMultipleRegisters((byte) 0x10);

    private final byte code;

    ModBusFunctionCodes(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
