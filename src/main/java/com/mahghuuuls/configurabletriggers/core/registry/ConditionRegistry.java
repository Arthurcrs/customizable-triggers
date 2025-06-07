package com.mahghuuuls.configurabletriggers.core.registry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.reflections.Reflections;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.util.CTLogger;

/**
 * A static registry for managing and creating {@link ICondition} instances.
 * This registry automatically discovers condition classes annotated with
 * {@link RegisterCondition} and provides a method to create condition instances
 * from JSON configuration. It supports an optional "value" field in the JSON to
 * invert the condition's test result.
 */
public final class ConditionRegistry {
	/**
	 * A map storing factory functions to create {@link ICondition} instances. The
	 * keys are the unique string IDs registered via the {@link RegisterCondition}
	 * annotation, and the values are {@link Function}s that take a
	 * {@link JsonObject} and return an {@link ICondition}.
	 */
	private static final Map<String, Function<JsonObject, ICondition>> CONDITION_FACTORIES = new HashMap<>();

	/**
	 * Initializes the ConditionRegistry by scanning the classpath for classes
	 * annotated with {@link RegisterCondition}. For each discovered class, it
	 * attempts to find a static `fromJson(JsonObject)` method. If found, a factory
	 * function that invokes this method is registered in
	 * {@link #CONDITION_FACTORIES}.
	 *
	 * <p>
	 * This method should be called once during the mod's pre-initialization phase.
	 * It throws a {@link RuntimeException} if a registered condition class is
	 * missing the required `fromJson` method, as this indicates a critical
	 * development error, halting mod initialization to prevent unpredictable
	 * behavior.
	 * </p>
	 */
	public static void init() {
		Reflections refs = new Reflections("com.mahghuuuls.configurabletriggers.core.condition.impl");
		Set<Class<?>> classes = refs.getTypesAnnotatedWith(RegisterCondition.class);

		CTLogger.info("Initializing ConditionRegistry. Found {} potential condition classes.", classes.size());

		for (Class<?> cls : classes) {
			RegisterCondition anno = cls.getAnnotation(RegisterCondition.class);
			String id = anno.value();
			try {
				Method fromJson = cls.getMethod("fromJson", JsonObject.class);
				CONDITION_FACTORIES.put(id, json -> {
					try {
						return (ICondition) fromJson.invoke(null, json);
					} catch (Exception e) {
						// This catches exceptions during the *actual creation* of a condition
						// from JSON (e.g., malformed JSON for a specific condition entry).
						// It re-throws as a RuntimeException, which will be caught by TriggerJsonLoader
						// allowing it to log the problematic condition and potentially skip it.
						throw new RuntimeException(
								"[Configurable Triggers] Failed to create condition from JSON for class: "
										+ cls.getName(),
								e);
					}
				});
				CTLogger.debug("Registered condition factory for ID: '{}' (Class: {})", id, cls.getName());
			} catch (NoSuchMethodException e) {
				// This handles the critical error where an annotated class
				// does not have the expected static fromJson method. This is a mod development
				// error.
				CTLogger.error(
						"[Configurable Triggers] Condition class '{}' is missing the required static 'fromJson(JsonObject)' method. This condition will not be registered.",
						cls.getName(), e);
				// Throw a RuntimeException to halt mod initialization if a critical component
				// is malformed.
				throw new RuntimeException("[Configurable Triggers] Missing fromJson method in " + cls.getName(), e);
			} catch (Exception e) {
				// Catch any other unexpected errors during the registration process.
				CTLogger.error(
						"[Configurable Triggers] Unexpected error while registering condition '{}' from class '{}'.",
						id, cls.getName(), e);
			}
		}
		CTLogger.info("ConditionRegistry initialized. Total conditions registered: {}", CONDITION_FACTORIES.size());
	}

	/**
	 * Creates a new {@link ICondition} instance based on the provided JSON object.
	 * The JSON object *must* contain an "id" field, which corresponds to a
	 * registered condition's unique identifier. It also supports an optional
	 * "value" field (boolean, default {@code true}) which can invert the result of
	 * the condition's
	 * {@link ICondition#test(com.mahghuuuls.configurabletriggers.core.context.Context)}
	 * method.
	 *
	 * @param jsonObj The {@link JsonObject} containing the condition's ID and its
	 *                specific configuration.
	 * @return A new {@link ICondition} instance, potentially wrapped to invert its
	 *         test result.
	 * @throws IllegalArgumentException If the "id" field is missing in the JSON
	 *                                  object, or if the specified condition ID is
	 *                                  not recognized (i.e., no factory is
	 *                                  registered for it).
	 * @throws RuntimeException         If the condition's `fromJson` method fails
	 *                                  to instantiate the condition (e.g., due to
	 *                                  malformed condition-specific JSON data).
	 */
	public static ICondition fromJson(JsonObject jsonObj) {
		// Validate that the JSON object has an "id" field and that it's a string.
		if (!jsonObj.has("id") || !jsonObj.get("id").isJsonPrimitive()
				|| !jsonObj.get("id").getAsJsonPrimitive().isString()) {
			CTLogger.error("Condition JSON is missing 'id' field or 'id' is not a string: {}", jsonObj);
			throw new IllegalArgumentException("Condition JSON is missing 'id' field or 'id' is not a string.");
		}

		String id = jsonObj.get("id").getAsString();
		Function<JsonObject, ICondition> factory = CONDITION_FACTORIES.get(id); // Retrieve the factory function for the
																				// given ID.

		if (factory == null) {
			// If no factory is found for the ID, it means an unknown condition ID was
			// provided in the config.
			CTLogger.error("Unknown condition ID '{}' in config: {}. Please check your triggers.json.", id, jsonObj);
			throw new IllegalArgumentException("Unknown condition id: " + id);
		}

		// Apply the factory function to the JSON object to create the base ICondition
		// instance.
		// Any RuntimeException from the factory will propagate up.
		ICondition base = factory.apply(jsonObj);

		// Check for the optional "value" field to determine if the condition's result
		// should be inverted.
		// If "value" is false, we wrap the base condition to negate its test result.
		boolean expected = jsonObj.has("value") ? jsonObj.get("value").getAsBoolean() : true;

		if (!expected) {
			// Return a new ICondition that negates the result of the base condition.
			return ctx -> !base.test(ctx);
		}

		return base;
	}

	/**
	 * Private constructor to prevent instantiation of this utility class. All
	 * methods in this class are static.
	 */
	private ConditionRegistry() {
	}
}
