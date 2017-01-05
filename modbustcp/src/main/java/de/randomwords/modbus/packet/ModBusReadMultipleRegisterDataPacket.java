package de.randomwords.modbus.packet;

import de.randomwords.modbus.ModBusFunctionCodes;

/**
 * Created by Ronny on 27.12.2016.
 *
 */
public abstract class ModBusReadMultipleRegisterDataPacket extends ModBusDataPacket {

    public ModBusReadMultipleRegisterDataPacket(int address) {
        super(address);
    }

    @Override
    public byte getFunctionCode() {
        return ModBusFunctionCodes.ReadHoldingRegisters.getCode();
    }
}
