package de.randomwords.modbus;

import de.randomwords.modbus.exception.ModBusCommunicationException;
import de.randomwords.modbus.packet.ModBusDataPacketRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Created by Ronny Polley on 04.01.2017.
 */
class ModBusSyncTCPProtocolHandlerTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Socket socket;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ModBusDataPacketRequest dataPacket;

    @BeforeEach
    void setUp() {
        // as there is no extension coming with the mockito project jet.
        MockitoAnnotations.initMocks(this);
        when(dataPacket.createArrayToSend()).thenReturn(new byte[]{0, 1, 3});
    }

    @Test
    void getFromConnection_ExceptionFromSocketOutputStream() throws IOException {
        doThrow(new IOException("bla")).when(socket).getOutputStream();
        Throwable throwable = assertThrows(ModBusCommunicationException.class, () -> {
            new ModBusSyncTCPProtocolHandler(socket).getFromConnection(dataPacket);
        });
        assertThat(throwable.getMessage(), is(equalTo("error communicating with modbus master")));
        assertThat(throwable.getCause().getMessage(), is(equalTo("bla")));
    }

    @Test
    void getFromConnection_ExceptionFromInputStream() throws IOException {
        doThrow(new IOException("bla")).when(socket).getInputStream();
        Throwable throwable = assertThrows(ModBusCommunicationException.class, () -> {
            new ModBusSyncTCPProtocolHandler(socket).getFromConnection(dataPacket);
        });
        assertThat(throwable.getMessage(), is(equalTo("error communicating with modbus master")));
        assertThat(throwable.getCause().getMessage(), is(equalTo("bla")));
    }

    @Test
    void getFromConnection_WrongTransactionIdOnInputStream() throws IOException {
        InputStream inputStream = mock(InputStream.class, Answers.RETURNS_DEEP_STUBS);
        when(socket.getInputStream()).thenReturn(inputStream);
        when(dataPacket.getTransactionId()).thenReturn(1);
        Throwable throwable = assertThrows(ModBusCommunicationException.class, () -> {
            new ModBusSyncTCPProtocolHandler(socket).getFromConnection(dataPacket);
        });
        assertThat(throwable.getMessage(), is(equalTo("TransactionId does not match")));
    }

    @Test
    void getFromConnection_RunThrough() throws IOException, ModBusCommunicationException {
        InputStream inputStream = mock(InputStream.class, Answers.RETURNS_DEEP_STUBS);
        when(inputStream.read(any(byte[].class), eq(0), eq(8))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                byte[] header = invocationOnMock.getArgumentAt(0, byte[].class);
                header[1] = 1;
                header[5] = 1;
                return 1;
            }
        });
        when(socket.getInputStream()).thenReturn(inputStream);
        when(dataPacket.getTransactionId()).thenReturn(1);
        byte[] connection = new ModBusSyncTCPProtocolHandler(socket).getFromConnection(dataPacket);
        assertThat(connection.length, is(equalTo(9)));
        assertThat(connection[1], is(equalTo((byte) 1)));

        verify(inputStream, times(1)).read(any(byte[].class), eq(0), eq(1));
    }
}