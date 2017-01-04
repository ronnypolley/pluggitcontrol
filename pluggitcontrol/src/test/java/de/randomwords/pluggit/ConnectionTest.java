package de.randomwords.pluggit;

import de.randomwords.pluggit.enums.AlarmType;
import de.randomwords.pluggit.enums.OperationMode;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Ronny Polley on 09.12.2016.
 *
 * @author Ronny Polley
 */
public class ConnectionTest {

    private Connection connection;

    @BeforeEach
    public void setUp() throws IOException {
        connection = new Connection();
        connection.connect(new InetSocketAddress("192.168.178.25", 502));
    }

    @AfterEach
    public void tearDown() throws IOException {
        connection.disconnect();
    }

    @Test
    public void testConnection() throws IOException {
        Connection connection = new Connection();
        connection.connect(new InetSocketAddress("192.168.178.25", 502));
        assertThat(connection.getIPAddress(), IsNull.notNullValue());
        connection.disconnect();
    }

    @Test
    public void testIpAddress() throws Exception {
        InetAddress ipAddress = connection.getIPAddress();
        assertThat(ipAddress, equalTo(InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, (byte) 178, 25})));
        System.out.println("IP Address: " + ipAddress);
    }

    @Test
    public void testFirmewareVersion() throws Exception {
        Firmware firmwareVersion = connection.getFirmwareVersion();
        System.out.println("Firmware version: " + firmwareVersion);
        assertThat(firmwareVersion.getMajorVersion(), equalTo(2));
        assertThat(firmwareVersion.getMinorVersion(), equalTo(40));
    }

    @Test
    public void testCurrentDateTime() throws Exception {
        LocalDateTime currentDateTime = connection.getCurrentDateTime();
        System.out.println("Current date time: " + currentDateTime);
        assertThat(currentDateTime, Matchers.lessThan(LocalDateTime.now(ZoneId.systemDefault()).plus(5, ChronoUnit.MINUTES)));
        assertThat(currentDateTime, Matchers.greaterThan(LocalDateTime.now(ZoneId.systemDefault()).minus(5, ChronoUnit.MINUTES)));
    }

    @Test
    public void testWorktime() throws Exception {
        int worktime = connection.getWorktime();
        System.out.println("work time: " + worktime + " hours");
        System.out.println("work time: " + worktime / 24 + " days");
        assertThat(worktime, Matchers.greaterThan(0));
    }

    @Test
    public void testOperationMode() throws Exception {
        OperationMode operationMode = connection.getOperationMode();
        System.out.println("operation mode: " + operationMode);
        assertThat(operationMode, IsNull.notNullValue());
    }

    @Test
    public void testOutdoorTemp() throws Exception {
        float outTemp = connection.getOutdoorTemp();
        System.out.println("outdoor temp: " + outTemp);
        assertThat(outTemp, IsNull.notNullValue());
    }

    @Test
    public void testSupplyTemp() throws Exception {
        float outTemp = connection.getSupplyTemp();
        System.out.println("supply temp: " + outTemp);
        assertThat(outTemp, IsNull.notNullValue());
    }

    @Test
    public void testExtractTemp() throws Exception {
        float outTemp = connection.getExtractTemp();
        System.out.println("extract temp: " + outTemp);
        assertThat(outTemp, IsNull.notNullValue());
    }

    @Test
    public void testExhaustTemp() throws Exception {
        float outTemp = connection.getExhaustTemp();
        System.out.println("exhaust temp: " + outTemp);
        assertThat(outTemp, IsNull.notNullValue());
    }

    @Test
    public void testFilterRemainigTime() throws Exception {
        int filterTime = connection.getFilterRemainingTime();
        System.out.println("filter remaining time: " + filterTime);
        assertThat(filterTime, Matchers.greaterThan(0));
    }

    @Test
    public void testActiveAlarm() throws Exception {
        AlarmType activeAlarm = connection.getActiveAlarm();
        System.out.println("Active alarm: " + activeAlarm);
        assertThat(activeAlarm, equalTo(AlarmType.None));
    }

    @Test
    public void testFan1Speed() throws Exception {
        float fan1Speed = connection.getFan1Speed();
        System.out.println("fan 1 speed: " + fan1Speed);
        assertThat(fan1Speed, Matchers.greaterThan(0.0f));
    }

    @Test
    public void testFan2Speed() throws Exception {
        float fan1Speed = connection.getFan2Speed();
        System.out.println("fan 2 speed: " + fan1Speed);
        assertThat(fan1Speed, Matchers.greaterThan(0.0f));
    }

    @Test
    public void testSettingVentilationLevel() throws Exception {
        connection.setLevel(2);
    }
}
