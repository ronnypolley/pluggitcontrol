package de.randomwords.pluggit.enums;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ronny on 19.12.2016.
 */
public class OperationModeTest {

    @Test
    public void testAmount() throws Exception {
        assertThat("Are there any changes to the OperationModes?", OperationMode.values().length, equalTo(17));
    }
}
