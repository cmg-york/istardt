package ca.yorku.cmg.istardt.xmlparser.xml.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton logger class for the XML unmarshalling
 */
public class CustomLogger {
    private static CustomLogger instance =  new CustomLogger();
    private boolean debugEnabled = false;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";

    private CustomLogger() {
    }
    public static CustomLogger getInstance() {
        return instance;
    }

    /**
     * Enable/disable debug logging
     */
    public void setDebugEnabled(boolean enabled) {
        this.debugEnabled = enabled;
    }

    /**
     * Check if debug logging is enabled
     */
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    /**
     * Log an info message
     */
    public void info(String message) {
        if (debugEnabled) {
            System.out.println(formatMessage("INFO", message, ANSI_BLUE));
        }
    }

    /**
     * Log an info message from a specific class
     */
    public void info(Class<?> clazz, String message) {
        if (debugEnabled) {
            System.out.println(formatMessage("INFO", clazz.getSimpleName() + ": " + message, ANSI_BLUE));
        }
    }

    /**
     * Log a warning message
     */
    public void warning(String message) {
        System.err.println(formatMessage("WARNING", message, ANSI_RED));
    }

    /**
     * Log a warning message from a specific class
     */
    public void warning(Class<?> clazz, String message) {
        System.err.println(formatMessage("WARNING", clazz.getSimpleName() + ": " + message, ANSI_RED));
    }

    /**
     * Log an error message
     */
    public void error(String message) {
        System.err.println(formatMessage("ERROR", message, ANSI_RED));
    }

    /**
     * Log an error message from a specific class
     */
    public void error(Class<?> clazz, String message) {
        System.err.println(formatMessage("ERROR", clazz.getSimpleName() + ": " + message, ANSI_RED));
    }

    /**
     * Log an error message with exception
     */
    public void error(String message, Throwable throwable) {
        System.err.println(formatMessage("ERROR", message, ANSI_RED));
        if (throwable != null) {
            throwable.printStackTrace(System.err);
        }
    }

    /**
     * Log an error message with exception
     */
    public void error(Class<?> clazz, String message, Throwable throwable) {
        System.err.println(formatMessage("ERROR", clazz.getSimpleName() + ": " + message, ANSI_RED));
        if (throwable != null) {
            throwable.printStackTrace(System.err);
        }
    }

    /**
     * Format message with timestamp and level
     */
    private String formatMessage(String level, String message, String colorCode) {
        String timestamp = LocalDateTime.now().format(formatter);
        // Only use colors if supported
        if (System.console() != null) {
            return String.format("%s[%s] [%s%s%s] %s",
                    ANSI_RESET, timestamp, colorCode, level, ANSI_RESET, message);
        } else {
            return String.format("[%s] [%s] %s", timestamp, level, message);
        }
    }
}