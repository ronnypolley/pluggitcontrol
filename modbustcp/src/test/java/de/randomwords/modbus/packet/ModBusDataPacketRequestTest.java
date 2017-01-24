package de.randomwords.modbus.packet;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Created by Ronny Polley on 28.12.2016.
 *
 * @author Ronny Polley
 */
class ModBusDataPacketRequestTest {
    @Test
    void createArrayToSend() {
        ModBusDataPacketRequest modBusDataPacketRequest = new ModBusDataPacketRequest(111, 222) {
            @Override
            public int getBaseAddress() {
                return 0;
            }

            @Override
            public byte[] getPayload() {
                return new byte[]{1, 2};
            }

            @Override
            public byte getFunctionCode() {
                return 0;
            }

            @Override
            public byte getDataLengthInBytes() {
                return (byte) getPayload().length;
            }
        };

        byte[] toSend = modBusDataPacketRequest.createArrayToSend();
        byte[] expected = new byte[]{0, (byte) 222, 0, 0, 0, 6, 0, 0, 0, (byte) 111, 1, 2};
        assertThat(toSend, is(equalTo(expected)));

    }

}