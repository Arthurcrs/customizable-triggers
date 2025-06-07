package com.mahghuuuls.configurabletriggers.core.registry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.reflections.Reflections;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterAction;
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.util.CTLogger;

/**
 * A static registry for managing and creating {@link IAction} instances. This
 * registry automatically discovers action classes annotated with
 * {@link RegisterAction} and provides a method to create action instances from
 * JSON configuration. It ensures that actions can be dynamically instantiated
 * based on their registered IDs.
 */
public final class ActionRegistry {
	/**
	 * A map storing factory functions to create {@link IAction} instances. The keys
	 * are the unique string IDs registered via the {@link RegisterAction}
	 * annotation, and the values are {@link Function}s that take a
	 * {@link JsonObject} and return an {@link IAction}.
	 */
	private static final Map<String, Function<JsonObject, IAction>> ACTION_FACTORIES = new HashMap<>();

	/**
	 * Initializes the ActionRegistry by scanning the classpath for classes
	 * annotated with {@link RegisterAction}. For each discovered class, it attempts
	 * to find a static `fromJson(JsonObject)` method. If found, a factory function
	 * that invokes this method is registered in {@link #ACTION_FACTORIES}.
	 *
	 * <p>
	 * This method should be called once during the mod's pre-initialization phase.
	 * It throws a {@link RuntimeException} if a registered action class is missing
	 * the required `fromJson` method, as this indicates a critical development
	 * error.
	 * </p>
	 */
	public static void init() {
		// Initialize the Reflections library to scan for classes in the action
		// implementation package.
		Reflections refs = new Reflections("com.mahghuuuls.configurabletriggers.core.action.impl");
		// Get all classes that are annotated with @RegisterAction.
		Set<Class<?>> actions = refs.getTypesAnnotatedWith(RegisterAction.class);

		CTLogger.info("Initializing ActionRegistry. Found {} potential action classes.", actions.size());

		for (Class<?> cls : actions) {
			// Retrieve the RegisterAction annotation to get the unique ID for this action.
			RegisterAction anno = cls.getAnnotation(RegisterAction.class);
			String id = anno.value();
			try {
				// Attempt to find the static "fromJson" method that takes a JsonObject.
				Method fromJson = cls.getMethod("fromJson", JsonObject.class);
				// Register a lambda function as the factory. This function will be called
				// when an action of this ID needs to be created from JSON.
				ACTION_FACTORIES.put(id, json -> {
					try {
						// Invoke the fromJson method to create an instance of the IAction.
						return (IAction) fromJson.invoke(null, json);
					} catch (Exception e) {
						// This catch block handles exceptions during the *actual creation* of an action
						// from JSON (e.g., malformed JSON for a specific action entry).
						// It re-throws as a RuntimeException, which will be caught by TriggerJsonLoader
						// allowing it to log the specific problematic action and potentially skip it.
						throw new RuntimeException(
								"[Configurable Triggers] Failed to create action from JSON for class: " + cls.getName(),
								e);
					}
				});
				CTLogger.debug("Registered action factory for ID: '{}' (Class: {})", id, cls.getName());
			} catch (NoSuchMethodException e) {
				// This catch block handles the critical error where an annotated class
				// does not have the expected static fromJson method. This is a mod development
				// error.
				CTLogger.error(
						"[Configurable Triggers] Action class '{}' is missing the required static 'fromJson(JsonObject)' method. This action will not be registered.",
						cls.getName(), e);
				// Throw a RuntimeException to halt mod initialization if a critical component
				// is malformed.
				throw new RuntimeException("[Configurable Triggers] Missing fromJson method in " + cls.getName(), e);
			} catch (Exception e) {
				// Catch any other unexpected errors during the registration process.
				CTLogger.error(
						"[Configurable Triggers] Unexpected error while registering action '{}' from class '{}'.", id,
						cls.getName(), e);
			}
		}
		CTLogger.info("ActionRegistry initialized. Total actions registered: {}", ACTION_FACTORIES.size());
	}

	/**
	 * Creates a new {@link IAction} instance based on the provided JSON object. The
	 * JSON object *must* contain an "id" field, which corresponds to a registered
	 * action's unique identifier. The rest of the JSON object's content is passed
	 * to the action's specific `fromJson` method for parsing its configuration.
	 *
	 * @param obj The {@link JsonObject} containing the action's ID and its specific
	 *            configuration.
	 * @return A new {@link IAction} instance.
	 * @throws IllegalArgumentException If the "id" field is missing in the JSON
	 *                                  object, or if the specified action ID is not
	 *                                  recognized (i.e., no factory is registered
	 *                                  for it).
	 * @throws RuntimeException         If the action's `fromJson` method fails to
	 *                                  instantiate the action (e.g., due to
	 *                                  malformed action-specific JSON data).
	 */
	public static IAction fromJson(JsonObject obj) {
		// Validate that the JSON object has an "id" field and that it's a string.
		if (!obj.has("id") || !obj.get("id").isJsonPrimitive() || !obj.get("id").getAsJsonPrimitive().isString()) {
			CTLogger.error("Action JSON is missing 'id' field or 'id' is not a string: {}", obj);
			throw new IllegalArgumentException("Action JSON is missing 'id' field or 'id' is not a string.");
		}

		String id = obj.get("id").getAsString();
		Function<JsonObject, IAction> f = ACTION_FACTORIES.get(id); // Retrieve the factory function for the given ID.

		if (f == null) {
			// If no factory is found for the ID, it means an unknown action ID was provided
			// in the config.
			CTLogger.error("Unknown action ID '{}' in config: {}. Please check your triggers.json.", id, obj);
			throw new IllegalArgumentException("[Configurable Triggers] Unknown action id: " + id);
		}
		// Apply the factory function to the JSON object to create the IAction instance.
		// Any RuntimeException from the factory will propagate up.
		return f.apply(obj);
	}

	/**
	 * Private constructor to prevent instantiation of this utility class. All
	 * methods in this class are static.
	 */
	private ActionRegistry() {
	}
}
