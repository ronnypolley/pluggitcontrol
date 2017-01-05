package de.randomwords.pluggit.exception;

/**
 * Created by Ronny Polley on 05.01.2017.
 */
public class PluggitControlException extends Exception {

    public PluggitControlException(String message) {
        super(message);
    }

    public PluggitControlException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluggitControlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
