package de.randomwords.modbus;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ronny Polley on 28.12.2016.
 */
class ModBusFunctionCodesTest {

    @Test
    void testAmountOfValues() {
        assertThat(ModBusFunctionCodes.values().length, equalTo(2));
    }
}