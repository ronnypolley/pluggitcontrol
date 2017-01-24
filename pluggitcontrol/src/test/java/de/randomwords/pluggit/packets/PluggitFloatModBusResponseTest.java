package de.randomwords.pluggit.packets;

import de.randomwords.modbus.exception.ModBusCommunicationException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

/**
 * Created by Ronny Polley on 23.01.2017.
 */
class PluggitFloatModBusResponseTest {
    @Test
    void getValue() throws ModBusCommunicationException {
        PluggitFloatModBusResponse response = new PluggitFloatModBusResponse(new byte[]{0, 0, 0, 0, 0, 7}, new byte[]{3, 4, 1, 2, 3, 4});
        MatcherAssert.assertThat(response.getValue(), CoreMatchers.is(CoreMatchers.equalTo(1.1f)));
    }

}