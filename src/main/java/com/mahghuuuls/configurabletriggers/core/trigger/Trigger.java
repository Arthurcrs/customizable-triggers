package com.mahghuuuls.configurabletriggers.core.trigger;

import java.util.List;

import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.util.CTLogger;

public abstract class Trigger implements ITrigger {

	protected final String name;
	protected final List<ICondition> conditions;
	protected final List<IAction> actions;

	protected Trigger(String name, List<ICondition> conditions, List<IAction> actions) {
		this.name = name;
		this.conditions = conditions == null ? java.util.Collections.emptyList() : conditions;
		this.actions = actions == null ? java.util.Collections.emptyList() : actions;
	}

	protected final void run(Context ctx) {
		if (this.testAll(ctx)) {
			this.executeAll(ctx);
		}
	}

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
