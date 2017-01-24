package de.randomwords.pluggit.packets;

import de.randomwords.pluggit.enums.PluggitRegisterAddress;

/**
 * Created by Ronny Polley on 28.12.2016.
 */
public class PluggitReadRegister extends PluggitModBusRequest {

    private static final byte[] READ_TWO_BYTES_PAYLOAD = new byte[]{0, 2};

    public PluggitReadRegister(PluggitRegisterAddress address) {
        super(address.getPmr());
    }

    @Override
    public byte[] getPayload() {
        return READ_TWO_BYTES_PAYLOAD;
    }

}
