package de.randomwords.pluggit;

import de.randomwords.modbus.ModBusSyncTCPProtocolHandler;
import de.randomwords.modbus.exception.ModBusCommunicationException;
import de.randomwords.pluggit.enums.AlarmType;
import de.randomwords.pluggit.enums.OperationMode;
import de.randomwords.pluggit.enums.PluggitRegisterAddress;
import de.randomwords.pluggit.packets.PluggitReadRegister;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by Ronny on 09.12.2016.
 */
public class Connection {

    private static final Logger LOGGER = LogManager.getLogger(Connection.class);
    public static final String ERROR_COMMUNICATING = "error communicating";

    private Socket socket;

    public void connect(InetSocketAddress pluggitAddress) throws IOException {
        if (socket == null) {
            socket = new Socket();
        }
        if (!socket.isConnected()) {
            socket.connect(pluggitAddress);
        }
    }


    public void disconnect() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }

    public void sendToPluggit(int pmrIpAdress) throws IOException {
        sendToPluggit(pmrIpAdress, new byte[]{0, 2}, true);
    }

    public void sendToPluggit(int pmrIpAdress, byte[] value, boolean read) throws IOException {
        if (value.length <= Byte.MAX_VALUE * 2) {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            byte[] registerAddress = new byte[]{(byte) ((pmrIpAdress - 40001) >> 8), (byte) ((pmrIpAdress - 40001) % 256)};
            // just for this to include apache commons ... does that make sense?
            byte[] toSend = ArrayUtils.addAll(new byte[]{0, 10, 0, 0, 0, (byte) (4 + value.length + (read ? 0 : 3)), 0, (byte) (read ? 3 : 16)}, registerAddress);
            if (!read) {
                toSend = ArrayUtils.addAll(toSend, new byte[]{0, (byte) (value.length / 2), (byte) value.length});
            }
            toSend = ArrayUtils.addAll(toSend, value);

            bufferedOutputStream.write(toSend);
            bufferedOutputStream.flush();
        }
    }

    protected byte[] sendToPluggit(PluggitRegisterAddress address, byte[] payload) {
        return new byte[]{};
    }

    protected byte[] getFromPluggit(PluggitRegisterAddress address) throws ModBusCommunicationException {
        return new ModBusSyncTCPProtocolHandler(socket).getFromConnection(new PluggitReadRegister(address));
    }

    public InetAddress getIPAddress() {
        try {
            byte[] response = getFromPluggit(PluggitRegisterAddress.IPAddress);
            return InetAddress.getByAddress(new byte[]{response[11], response[12], response[9], response[10]});
        } catch (IOException | ModBusCommunicationException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return null;
    }


    public Firmware getFirmwareVersion() {
        try {
            byte[] response = getFromPluggit(PluggitRegisterAddress.Firmware);
            return new Firmware(response[0], response[1]);
        } catch (ModBusCommunicationException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return null;
    }

    public LocalDateTime getCurrentDateTime() {
        try {
            sendToPluggit(PluggitRegisterAddress.CurrentDateTime.getPmr());

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (dataInputStream.readByte() != (byte) 4) {
                // do nothing, skip unnecessary modbus code here
            }

            int a = dataInputStream.readShort();
            int b = dataInputStream.readShort();

            return LocalDateTime.ofInstant(Instant.ofEpochMilli(((long) (a & 0xFFFF | b << 16)) * 1000), ZoneId.of("UTC"));
        } catch (IOException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return null;
    }

    public int getWorktime() {
        try {
            sendToPluggit(PluggitRegisterAddress.Worktime.getPmr());

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (dataInputStream.readByte() != (byte) 4) {
                // do nothing, skip unnecessary modbus code here
            }

            int a = dataInputStream.readShort();
            int b = dataInputStream.readShort();

            return (a | b << 16);
        } catch (IOException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return 0;
    }

    public OperationMode getOperationMode() {
        try {
            sendToPluggit(PluggitRegisterAddress.OperationMode.getPmr());

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (dataInputStream.readByte() != (byte) 4) {
                // do nothing, skip unnecessary modbus code here
            }

            int a = dataInputStream.readShort();

            return OperationMode.values()[a];
        } catch (IOException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return null;
    }

    public float getOutdoorTemp() {
        try {
            sendToPluggit(PluggitRegisterAddress.OutdoorTemp.getPmr());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (dataInputStream.readByte() != (byte) 4) {
                // do nothing, skip unnecessary modbus code here
            }

            byte c = (byte) dataInputStream.readUnsignedByte();
            byte d = (byte) dataInputStream.readUnsignedByte();
            byte a = (byte) dataInputStream.readUnsignedByte();
            byte b = (byte) dataInputStream.readUnsignedByte();

            return ByteBuffer.wrap(new byte[]{a, b, c, d}).getFloat();

        } catch (IOException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return 0.0f;
    }

    public float getSupplyTemp() {
        try {
            sendToPluggit(PluggitRegisterAddress.SupplyTemp.getPmr());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (dataInputStream.readByte() != (byte) 4) {
                // do nothing, skip unnecessary modbus code here
            }

            byte c = (byte) dataInputStream.readUnsignedByte();
            byte d = (byte) dataInputStream.readUnsignedByte();
            byte a = (byte) dataInputStream.readUnsignedByte();
            byte b = (byte) dataInputStream.readUnsignedByte();

            return ByteBuffer.wrap(new byte[]{a, b, c, d}).getFloat();

        } catch (IOException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return 0.0f;
    }

    public float getExtractTemp() {
        try {
            sendToPluggit(PluggitRegisterAddress.ExtractTemp.getPmr());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (dataInputStream.readByte() != (byte) 4) {
                // do nothing, skip unnecessary modbus code here
            }

            byte c = (byte) dataInputStream.readUnsignedByte();
            byte d = (byte) dataInputStream.readUnsignedByte();
            byte a = (byte) dataInputStream.readUnsignedByte();
            byte b = (byte) dataInputStream.readUnsignedByte();

            return ByteBuffer.wrap(new byte[]{a, b, c, d}).getFloat();

        } catch (IOException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return 0.0f;
    }

    public float getExhaustTemp() {
        try {
            sendToPluggit(PluggitRegisterAddress.ExhaustTemp.getPmr());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (dataInputStream.readByte() != (byte) 4) {
                // do nothing, skip unnecessary modbus code here
            }

            byte c = (byte) dataInputStream.readUnsignedByte();
            byte d = (byte) dataInputStream.readUnsignedByte();
            byte a = (byte) dataInputStream.readUnsignedByte();
            byte b = (byte) dataInputStream.readUnsignedByte();

            return ByteBuffer.wrap(new byte[]{a, b, c, d}).getFloat();

        } catch (IOException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return 0.0f;
    }

    public int getFilterRemainingTime() {
        try {
            sendToPluggit(PluggitRegisterAddress.FilterRemainingTime.getPmr());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (dataInputStream.readByte() != (byte) 4) {
                // do nothing, skip unnecessary modbus code here
            }

            short a = dataInputStream.readShort();
            short b = dataInputStream.readShort();

            return (a | b << 16);
        } catch (IOException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return 0;
    }

    public AlarmType getActiveAlarm() {
        try {
            sendToPluggit(PluggitRegisterAddress.ActiveAlarm.getPmr());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (dataInputStream.readByte() != (byte) 4) {
                // do nothing, skip unnecessary modbus code here
            }

            short a = dataInputStream.readShort();
            short b = dataInputStream.readShort();

            return AlarmType.values()[(a | b << 16)];
        } catch (IOException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return AlarmType.None;
    }

    public float getFan1Speed() {
        try {
            sendToPluggit(PluggitRegisterAddress.Fan1Speed.getPmr());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (dataInputStream.readByte() != (byte) 4) {
                // do nothing, skip unnecessary modbus code here
            }

            byte c = (byte) dataInputStream.readUnsignedByte();
            byte d = (byte) dataInputStream.readUnsignedByte();
            byte a = (byte) dataInputStream.readUnsignedByte();
            byte b = (byte) dataInputStream.readUnsignedByte();

            return ByteBuffer.wrap(new byte[]{a, b, c, d}).getFloat();

        } catch (IOException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return 0.0f;
    }

    public float getFan2Speed() {
        try {
            sendToPluggit(PluggitRegisterAddress.Fan2Speed.getPmr());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (dataInputStream.readByte() != (byte) 4) {
                // do nothing, skip unnecessary modbus code here
            }

            byte c = (byte) dataInputStream.readUnsignedByte();
            byte d = (byte) dataInputStream.readUnsignedByte();
            byte a = (byte) dataInputStream.readUnsignedByte();
            byte b = (byte) dataInputStream.readUnsignedByte();

            return ByteBuffer.wrap(new byte[]{a, b, c, d}).getFloat();

        } catch (IOException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
        return 0.0f;
    }

    public void setLevel(int ventilationLevel) {
        try {
            byte[] bytes = ByteBuffer.allocate(4).putInt(ventilationLevel).array();
            sendToPluggit(40325, new byte[]{bytes[2], bytes[3], bytes[0], bytes[1]}, false);
        } catch (IOException e) {
            LOGGER.error(ERROR_COMMUNICATING, e);
        }
    }
}
