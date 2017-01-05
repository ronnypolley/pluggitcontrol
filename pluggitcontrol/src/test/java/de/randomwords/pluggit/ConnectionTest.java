package de.randomwords.pluggit;

import de.randomwords.pluggit.enums.AlarmType;
import de.randomwords.pluggit.enums.OperationMode;
import de.randomwords.pluggit.exception.PluggitControlException;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Created by Ronny Polley on 09.12.2016.
 *
 * @author Ronny Polley
 */
public class ConnectionTest {

    @Spy
    @InjectMocks
    private Connection connection = new Connection();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Socket socket;

    @Mock
    private OutputStream outputStream;

    @Mock
    private InputStream inputStream;

    private InetSocketAddress pluggitAddress = new InetSocketAddress("pluggit", 502);
    private byte transActionId2, transActionId1;
    private byte payloadLength;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        connection.connect(pluggitAddress);
        verify(socket, times(1)).connect(pluggitAddress);

        when(socket.getOutputStream()).thenReturn(outputStream);
        when(socket.getInputStream()).thenReturn(inputStream);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                byte[] bytes = invocationOnMock.getArgumentAt(0, byte[].class);
                transActionId1 = bytes[0];
                transActionId2 = bytes[1];
                return null;
            }
        }).when(outputStream).write(any(byte[].class), anyInt(), anyInt());

        when(inputStream.read(any(byte[].class), eq(0), eq(8))).then(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                byte[] bytes = invocationOnMock.getArgumentAt(0, byte[].class);
                bytes[0] = transActionId1;
                bytes[1] = transActionId2;
                // length of payload
                bytes[5] = payloadLength;
                return null;
            }
        });
    }

    @AfterEach
    void tearDown() {
        reset(socket);

    }

    @Test
    public void testIpAddress_NoPayload() throws Exception {
        Throwable throwable = assertThrows(PluggitControlException.class, () -> {
            connection.getIPAddress();
        });
        assertThat(throwable.getMessage(), equalTo("error getting ip address from pluggit"));
        assertThat(throwable.getCause().getMessage(), equalTo("Not all data needed was retrieved"));
        //        assertThat(ipAddress, equalTo(InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, (byte) 178, 25})));
        //        System.out.println("IP Address: " + ipAddress);
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
