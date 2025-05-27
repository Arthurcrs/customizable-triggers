package com.mahghuuuls.configurabletriggers.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mahghuuuls.configurabletriggers.init.ConfigurableTriggersMod;

public final class CTLogger {

	private static final Logger LOGGER = LogManager.getLogger(ConfigurableTriggersMod.MODID);

	private CTLogger() {
	}

	public static void info(String fmt, Object... args) {
		LOGGER.info(fmt, args);
	}

	public static void warn(String fmt, Object... args) {
		LOGGER.warn(fmt, args);
	}

	public static void error(String fmt, Object... args) {
		LOGGER.error(fmt, args);
	}

	public static void debug(String fmt, Object... args) {
		LOGGER.debug(fmt, args);
	}

}