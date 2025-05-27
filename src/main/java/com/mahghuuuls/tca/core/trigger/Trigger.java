package com.mahghuuuls.tca.core.trigger;

import java.util.List;

import com.mahghuuuls.tca.core.action.IAction;
import com.mahghuuuls.tca.core.condition.ICondition;
import com.mahghuuuls.tca.core.context.Context;

public abstract class Trigger implements ITrigger {
	protected final List<ICondition> conditions;
	protected final List<IAction> actions;
	
	protected Trigger(List<ICondition> conditions, List<IAction> actions) {
	    this.conditions = conditions == null ? java.util.Collections.emptyList() : conditions;
	    this.actions    = actions    == null ? java.util.Collections.emptyList() : actions;
	}
	
	protected final void run(Context ctx) {
		if (this.testAll(ctx)) {
            this.executeAll(ctx);
        }
	}
	
    private boolean testAll(Context ctx) {
        for (ICondition condition : conditions) {
            if (!condition.test(ctx)) return false;
        }
        return true;
    }
    
	private void executeAll(Context ctx) {
		for (IAction action : actions) {
			action.execute(ctx);
		}
	}
	
}
