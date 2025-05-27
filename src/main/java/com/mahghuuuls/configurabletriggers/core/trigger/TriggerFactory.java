package com.mahghuuuls.configurabletriggers.core.trigger;

import java.util.List;

import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;

public interface TriggerFactory {
	ITrigger create(String name, List<ICondition> conditions, List<IAction> actions);
}