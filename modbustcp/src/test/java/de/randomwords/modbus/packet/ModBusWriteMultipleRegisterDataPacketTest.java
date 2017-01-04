package de.randomwords.modbus.packet;

import de.randomwords.modbus.ModBusFunctionCodes;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ronny Polley on 28.12.2016.
 */
class ModBusWriteMultipleRegisterDataPacketTest {
    @Test
    void getFunctionCode() {
        assertThat(new ModBusWriteMultipleRegisterDataPacket(0) {
            @Override
            public int getBaseAddress() {
                return 0;
            }

            @Override
            public byte[] getPayload() {
                return new byte[]{};
            }

            @Override
            public byte getDataLengthInBytes() {
                return 0;
            }
        }.getFunctionCode(), equalTo(ModBusFunctionCodes.WriteMultipleRegisters.getCode()));
    }

}