package de.randomwords.modbus;

import de.randomwords.modbus.exception.ModBusCommunicationException;
import de.randomwords.modbus.packet.ModBusDataPacket;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by Ronny Polley on 19.12.2016.
 *
 * @author Ronny Polley
 */
public class ModBusSyncTCPProtocolHandler {

    private static final Logger logger = LogManager.getLogger(ModBusDataPacket.class);

    private Socket modbusSocket;

    public ModBusSyncTCPProtocolHandler(Socket modbusSocket) {
        this.modbusSocket = modbusSocket;
    }

    public byte[] getFromConnection(ModBusDataPacket dataPacket) throws ModBusCommunicationException {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(modbusSocket.getOutputStream())) {
            outputStream.write(dataPacket.createArrayToSend());
            outputStream.flush();
            return getResponse(dataPacket.getTransactionId());
        } catch (IOException e) {
            logger.error("error communicating with modbus master on {} trying to send {}", modbusSocket, dataPacket);
            throw new ModBusCommunicationException("error communicating with modbus master", e);
        }
    }

    private byte[] getResponse(int transactionId) throws ModBusCommunicationException {
        try (DataInputStream dataInputStream = new DataInputStream(modbusSocket.getInputStream())) {
            // read header
            byte[] header = new byte[8];
            dataInputStream.read(header);

            if (header[0] != (byte) (transactionId >> 8) || header[1] != (byte) (transactionId & 255)) {
                logger.debug("The transactionId of the found response ({}) does not match the sent one ({})", Arrays.copyOfRange(header, 0, 1), transactionId);
                throw new ModBusCommunicationException("TransactionId does not match");
            }

            // for now we do not check everything of the other values.
            // TODO: check other parameters

            byte[] payload = new byte[header[5]];
            dataInputStream.read(payload);

            return ArrayUtils.addAll(header, payload);
        } catch (IOException e) {
            logger.error("error communicating with modbus master on {} trying to get a response", modbusSocket);
            throw new ModBusCommunicationException("error communicating with modbus master", e);
        }
    }

}
