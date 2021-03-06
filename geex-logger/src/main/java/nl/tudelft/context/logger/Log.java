package nl.tudelft.context.logger;

import nl.tudelft.context.logger.message.Message;
import nl.tudelft.context.logger.message.MessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Log class for Geex.
 *
 * @author Gerben Oolbekkink
 * @version 1.0
 * @since 14-6-2015
 */
public final class Log implements ObservableLog {
    /**
     * Instance of the singleton log.
     */
    private static volatile Log instance;

    /**
     * The current loggers to log to.
     */
    List<Logger> listeners;

    /**
     * Create a new Log.
     */
    private Log() {
        listeners = new ArrayList<>();
    }

    /**
     * Get or Create the logger.
     *
     * @return The logger for this application.
     */
    public static Log instance() {
        if (instance == null) {
            synchronized (Log.class) {
                if (instance == null) {
                    instance = new Log();
                }
            }
        }
        return instance;
    }

    /**
     * Log a message of type {@link MessageType}.INFO.
     *
     * @param message Message to log.
     */
    public static void info(final Message message) {
        instance().message(message, MessageType.INFO);
    }

    /**
     * Log a message of type {@link MessageType}.WARNING.
     *
     * @param message Message to log.
     */
    public static void warning(final Message message) {
        instance().message(message, MessageType.WARNING);
    }

    /**
     * Log a message of type {@link MessageType}.DEBUG.
     *
     * @param message Message to log.
     */
    public static void debug(final Message message) {
        instance().message(message, MessageType.DEBUG);
    }

    /**
     * Log a message of type {@link MessageType}.DEBUG.
     *
     * @param message Message to log.
     */
    public static void debug(final String message) {
        instance().message(message, MessageType.DEBUG);
    }

    /**
     * Log a exception of type {@link MessageType}.DEBUG.
     *
     * @param e Exception to log.
     */
    public static void debug(final Exception e) {
        instance().message(e.getMessage(), MessageType.DEBUG);
    }

    /**
     * Extract message.
     *
     * @param message Message to log
     * @param type    Type of message
     */
    private void message(final Message message, final MessageType type) {
        message(message.toString(), type);
    }

    /**
     * Notify listeners which qualify to the type.
     *
     * @param message Message string
     * @param type Type of message
     */
    private void message(final String message, final MessageType type) {
        listeners.stream()
                .filter(logger -> logger.getLevel().getLevel() <= type.getLevel())
                .forEach(logger -> logger.log(message, type));
    }

    /**
     * Register a new logger.
     *
     * @param listener logger to register.
     */
    @Override
    public void addLogger(final Logger listener) {
        listeners.add(listener);
    }

    /**
     * Remove an existing logger.
     *
     * @param listener logger to remove.
     */
    @Override
    public void removeLogger(final Logger listener) {
        listeners.remove(listener);
    }
}
