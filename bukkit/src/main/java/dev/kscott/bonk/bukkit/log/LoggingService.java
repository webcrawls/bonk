package dev.kscott.bonk.bukkit.log;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.logging.Logger;

/**
 * Handles logging.
 */
public final class LoggingService {

    /**
     * Controls whether or not INFO messages should be logged.
     */
    private static final boolean INFO = true;

    /**
     * Controls whether or not DEBUG messages should be logged.
     */
    private static final boolean DEBUG = true;

    /**
     * Controls whether or not WARN messages should be logged.
     */
    private static final boolean WARN = true;

    /**
     * The internal logger.
     */
    private final @NonNull Logger logger;

    /**
     * Constructs {@code LoggingService}.
     *
     * @param logger the logger
     */
    @Inject
    public LoggingService(
            final @NonNull @Named("pluginLogger") Logger logger
    ) {
        this.logger = logger;
    }

    /**
     * Writes an INFO message to the log.
     *
     * @param message the message to log
     */
    public void info(final @NonNull String message) {
        this.logger.info(message);
    }

    /**
     * Writes a WARN message to the log.
     *
     * @param message the message to log
     */
    public void warn(final @NonNull String message) {
        this.logger.warning(message);
    }

    /**
     * Writes a DEBUG message to the log.
     *
     * @param message the message to log
     */
    public void debug(final @NonNull String message) {
        if (DEBUG) {
            this.logger.info("[BONK_DBG] " + message);
        }
    }

}
