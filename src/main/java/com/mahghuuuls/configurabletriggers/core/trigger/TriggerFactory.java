package com.mahghuuuls.configurabletriggers.core.trigger;

import java.util.List;

import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;

/**
 * A functional interface for creating instances of {@link ITrigger}. This
 * factory is used by the
 * {@link com.mahghuuuls.configurabletriggers.core.registry.TriggerRegistry} to
 * dynamically create trigger objects based on their registered event type and
 * configuration loaded from JSON.
 */
@FunctionalInterface
public interface TriggerFactory {

	/**
	 * Creates a new instance of an {@link ITrigger} with the given properties.
	 * Implementations of this method will typically call the constructor of a
	 * concrete {@link ITrigger} class.
	 *
	 * @param name       The name assigned to the trigger instance, typically loaded
	 *                   from JSON configuration.
	 * @param conditions A list of {@link ICondition} instances that the trigger
	 *                   will evaluate.
	 * @param actions    A list of {@link IAction} instances that the trigger will
	 *                   execute if conditions are met.
	 * @return A new {@link ITrigger} instance, configured with the provided name,
	 *         conditions, and actions.
	 */
	ITrigger create(String name, List<ICondition> conditions, List<IAction> actions);
}