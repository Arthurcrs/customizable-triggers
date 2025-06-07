package com.mahghuuuls.configurabletriggers.core.trigger;

import java.util.List;

import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.util.CTLogger;

/**
 * An abstract base class for all configurable triggers within the mod. This
 * class provides the fundamental structure and logic for a trigger, including
 * storage for its associated conditions and actions, and methods for evaluating
 * conditions and executing actions based on a provided {@link Context}.
 * <p>
 * Concrete trigger implementations (e.g.,
 * {@link com.mahghuuuls.configurabletriggers.core.trigger.impl.OnLivingHurtTrigger})
 * will extend this class and define how they subscribe to specific
 * MinecraftForge events and how they build the {@link Context} for those events
 * before calling the {@link #run(Context)} method.
 * </p>
 */
public abstract class Trigger implements ITrigger {

	/**
	 * The unique name of this trigger instance, typically loaded from the
	 * `triggers.json` configuration file. Used for identification and logging
	 * purposes.
	 */
	protected final String name;

	/**
	 * A non-null, immutable list of {@link ICondition} instances. All conditions in
	 * this list must evaluate to {@code true} for the trigger's actions to be
	 * executed. If the list is empty, all conditions are considered met.
	 */
	protected final List<ICondition> conditions;

	/**
	 * A non-null, immutable list of {@link IAction} instances. These actions will
	 * be executed sequentially if all associated conditions pass.
	 */
	protected final List<IAction> actions;

	/**
	 * Constructs a new Trigger instance. Initializes the trigger with a name, a
	 * list of conditions, and a list of actions. If the provided lists of
	 * conditions or actions are {@code null}, they will be replaced with empty,
	 * immutable lists to prevent {@link NullPointerException}s.
	 *
	 * @param name       The name of this trigger instance.
	 * @param conditions A list of {@link ICondition} instances for this trigger.
	 *                   Can be {@code null}.
	 * @param actions    A list of {@link IAction} instances for this trigger. Can
	 *                   be {@code null}.
	 */
	protected Trigger(String name, List<ICondition> conditions, List<IAction> actions) {
		this.name = name;
		this.conditions = conditions == null ? java.util.Collections.emptyList() : conditions;
		this.actions = actions == null ? java.util.Collections.emptyList() : actions;
	}

	/**
	 * This is the primary execution method for a trigger. It first evaluates all
	 * associated conditions using {@link #testAll(Context)}. If all conditions
	 * pass, it then executes all associated actions using
	 * {@link #executeAll(Context)}. This method should be called by concrete
	 * trigger implementations when their corresponding MinecraftForge event is
	 * fired and the {@link Context} has been prepared.
	 *
	 * @param ctx The {@link Context} object containing all relevant data for the
	 *            current event.
	 */
	protected final void run(Context ctx) {
		if (this.testAll(ctx)) {
			this.executeAll(ctx);
		}
	}

	/**
	 * Iterates through all {@link ICondition}s associated with this trigger and
	 * calls their {@link ICondition#test(Context)} method.
	 *
	 * <p>
	 * If any condition returns {@code false}, or if any condition throws an
	 * {@link Exception} during its execution, this method immediately returns
	 * {@code false}, and an error is logged via {@link CTLogger}. This ensures that
	 * a single problematic condition does not prevent the evaluation of other
	 * conditions, but stops the current trigger's action execution.
	 * </p>
	 *
	 * @param ctx The {@link Context} to pass to each condition for evaluation.
	 * @return {@code true} if all conditions pass successfully, {@code false}
	 *         otherwise.
	 */
	private boolean testAll(Context ctx) {
		for (ICondition condition : conditions) {
			try {
				if (!condition.test(ctx))
					return false;
			} catch (Exception ex) {
				CTLogger.error("Condition {} in trigger {} failed to execute due to an error", condition, this.name,
						ex);
				return false;
			}
		}
		return true;
	}

	/**
	 * Iterates through all {@link IAction}s associated with this trigger and calls
	 * their {@link IAction#execute(Context)} method.
	 *
	 * <p>
	 * If any action throws an {@link Exception} during its execution, this method
	 * catches the exception and logs an error via {@link CTLogger}, but it
	 * continues to execute subsequent actions. This ensures that a single
	 * problematic action does not prevent other actions within the same trigger
	 * from executing.
	 * </p>
	 *
	 * @param ctx The {@link Context} to pass to each action for execution.
	 */
	private void executeAll(Context ctx) {
		for (IAction action : actions) {
			try {
				action.execute(ctx);
			} catch (Exception ex) {
				CTLogger.error("Action {} in trigger {} failed to execute due to an error", action, this.name, ex);
			}
		}
	}

}
