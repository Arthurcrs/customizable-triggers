package com.mahghuuuls.configurabletriggers.core.registry;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.mahghuuuls.configurabletriggers.annotation.RegisterTrigger;
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.trigger.ITrigger;
import com.mahghuuuls.configurabletriggers.core.trigger.TriggerFactory;
import com.mahghuuuls.configurabletriggers.util.CTLogger;

/**
 * A static registry for managing and creating {@link ITrigger} instances. This
 * registry automatically discovers trigger classes annotated with
 * {@link RegisterTrigger} and provides methods to create trigger objects from
 * parsed configuration data. It maps trigger event IDs (defined in JSON) to
 * their corresponding factory methods.
 */
public final class TriggerRegistry {

	/**
	 * A map storing {@link TriggerFactory} instances, keyed by their registered
	 * trigger event ID. This map is populated during the mod's initialization
	 * phase.
	 */
	private static final Map<String, TriggerFactory> TRIGGER_FACTORIES = new HashMap<>();

	/**
	 * Initializes the TriggerRegistry by scanning the classpath for classes
	 * annotated with {@link RegisterTrigger}. For each discovered class, it
	 * attempts to find a constructor matching (String, {@code List<ICondition>},
	 * {@code List<IAction>}) and registers a {@link TriggerFactory} for it.
	 *
	 * <p>
	 * This method should be called once during the mod's pre-initialization phase.
	 * It throws a {@link RuntimeException} if a registered trigger class is missing
	 * the required constructor, indicating a critical development error that halts
	 * mod loading.
	 * </p>
	 */
	public static void init() {
		Reflections refs = new Reflections("com.mahghuuuls.configurabletriggers.core.trigger.impl");
		Set<Class<?>> classes = refs.getTypesAnnotatedWith(RegisterTrigger.class);

		CTLogger.info("Initializing TriggerRegistry. Found {} potential trigger classes.", classes.size());

		for (Class<?> cls : classes) {
			RegisterTrigger anno = cls.getAnnotation(RegisterTrigger.class);
			String triggerEvent = anno.value();
			try {
				// Get the constructor that matches the signature (name, conditions, actions).
				Constructor<?> ctor = cls.getConstructor(String.class, List.class, List.class);
				// Register a lambda function as the factory for this trigger event ID.
				// This function will be used later to instantiate triggers from JSON.
				TRIGGER_FACTORIES.put(triggerEvent, (name, conds, acts) -> {
					try {
						// Invoke the constructor to create a new ITrigger instance.
						return (ITrigger) ctor.newInstance(name, conds, acts);
					} catch (Exception e) {
						// Log an error if instantiation fails during runtime (e.g., due to
						// configuration issues).
						// This exception is caught by TriggerJsonLoader, allowing it to skip this
						// trigger.
						CTLogger.error("Failed to instantiate trigger '{}' of class '{}' due to error: {}", name,
								cls.getName(), e.getMessage(), e);
						return null; // Return null if instantiation fails.
					}
				});
				CTLogger.debug("Registered trigger factory for event ID: '{}' (Class: {})", triggerEvent,
						cls.getName());
			} catch (NoSuchMethodException e) {
				// Critical error: an annotated trigger class is missing its required
				// constructor.
				CTLogger.error(
						"Trigger class '{}' is missing the required constructor (String, List<ICondition>, List<IAction>). This trigger will not be registered.",
						cls.getName(), e);
				// Throw RuntimeException to indicate a fundamental mod setup issue and halt
				// initialization.
				throw new RuntimeException("Trigger class '" + cls.getName()
						+ "' is missing the required constructor (String, List<ICondition>, List<IAction>)", e);
			} catch (Exception e) {
				// Catch any other unexpected errors during the reflection or registration
				// process.
				CTLogger.error("Unexpected error while registering trigger '{}' from class '{}': {}", triggerEvent,
						cls.getName(), e.getMessage(), e);
			}
		}
		CTLogger.info("TriggerRegistry initialized. Total trigger types registered: {}", TRIGGER_FACTORIES.size());
	}

	/**
	 * Registers a custom {@link TriggerFactory} for a given trigger event ID. This
	 * allows for programmatic registration of new trigger types at runtime, beyond
	 * those discovered via annotations.
	 *
	 * @param triggerEvent   The unique string ID for the trigger event (e.g.,
	 *                       "on_living_hurt").
	 * @param triggerFactory The {@link TriggerFactory} responsible for creating
	 *                       instances of this specific trigger type.
	 */
	public static void register(String triggerEvent, TriggerFactory triggerFactory) {
		TRIGGER_FACTORIES.put(triggerEvent, triggerFactory);
		CTLogger.debug("Manually registered trigger event: {}", triggerEvent);
	}

	/**
	 * Creates a new {@link ITrigger} instance based on its registered trigger event
	 * ID. This method retrieves the appropriate {@link TriggerFactory} from the
	 * registry and uses it to construct a new trigger object.
	 *
	 * @param name         The name of the trigger instance, typically provided in
	 *                     the `triggers.json`.
	 * @param triggerEvent The unique string ID of the trigger event type (e.g.,
	 *                     "on_living_hurt").
	 * @param conditions   A list of {@link ICondition} instances that this trigger
	 *                     will evaluate.
	 * @param actions      A list of {@link IAction} instances that this trigger
	 *                     will execute.
	 * @return A new {@link ITrigger} instance, or {@code null} if the specified
	 *         {@code triggerEvent} ID is unknown (i.e., no factory is registered
	 *         for it) or if the factory fails to create the instance.
	 */
	public static ITrigger create(String name, String triggerEvent, List<ICondition> conditions,
			List<IAction> actions) {
		TriggerFactory triggerFactory = TRIGGER_FACTORIES.get(triggerEvent);
		if (triggerFactory == null) {
			// Log an error if an attempt is made to create a trigger with an unregistered
			// event ID.
			CTLogger.error(
					"Unknown trigger event ID: {}. Trigger '{}' will not be created. Please check your triggers.json configuration.",
					triggerEvent, name);
			return null; // Indicate failure to create the trigger.
		}
		// Use the retrieved factory to create and return the new trigger instance.
		// Any errors during factory.create() will be handled internally by the
		// factory's lambda.
		return triggerFactory.create(name, conditions, actions);
	}

	/**
	 * Private constructor to prevent instantiation of this utility class. All
	 * methods in this class are static.
	 */
	private TriggerRegistry() {
	}
}
