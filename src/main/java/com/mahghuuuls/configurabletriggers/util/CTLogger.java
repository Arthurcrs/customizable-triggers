package com.mahghuuuls.configurabletriggers.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mahghuuuls.configurabletriggers.init.ConfigurableTriggersMod;

/**
 * A utility class for logging messages within the Configurable Triggers mod. It
 * uses Log4j2 and logs messages with the mod's specific logger instance,
 * prefixing messages with the mod ID.
 */
public final class CTLogger {

	/** The Log4j2 Logger instance for the Configurable Triggers mod. */
	private static final Logger LOGGER = LogManager.getLogger(ConfigurableTriggersMod.MODID);

	/**
	 * Private constructor to prevent instantiation of this utility class.
	 */
	private CTLogger() {
	}

	/**
	 * Logs an informational message.
	 *
	 * @param fmt  The format string (e.g., "Loading {} files").
	 * @param args Arguments to be formatted into the string.
	 */
	public static void info(String fmt, Object... args) {
		LOGGER.info(fmt, args);
	}

	/**
	 * Logs a warning message.
	 *
	 * @param fmt  The format string.
	 * @param args Arguments to be formatted into the string.
	 */
	public static void warn(String fmt, Object... args) {
		LOGGER.warn(fmt, args);
	}

	/**
	 * Logs an error message.
	 *
	 * @param fmt  The format string.
	 * @param args Arguments to be formatted into the string. This can include a
	 *             Throwable as the last argument.
	 */
	public static void error(String fmt, Object... args) {
		LOGGER.error(fmt, args);
	}

	/**
	 * Logs a debug message. These messages are typically only visible when the
	 * logging level is set to DEBUG in the Log4j2 configuration.
	 *
	 * @param fmt  The format string.
	 * @param args Arguments to be formatted into the string.
	 */
	public static void debug(String fmt, Object... args) {
		LOGGER.debug(fmt, args);
	}

}