package de.randomwords.modbus.exception;

/**
 * Created by Ronny Polley on 28.12.2016.
 */
public class ModBusCommunicationException extends Exception {

    public ModBusCommunicationException(String message) {
        super(message);
    }

    public ModBusCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModBusCommunicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
