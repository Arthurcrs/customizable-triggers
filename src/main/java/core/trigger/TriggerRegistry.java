package core.trigger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import core.action.IAction;
import core.condition.ICondition;

public final class TriggerRegistry {

	private static final Map<String, BiFunction<List<ICondition>, List<IAction>, ITrigger>> MAP = new HashMap<>();

	public static void register(String id, BiFunction<List<ICondition>, List<IAction>, ITrigger> factory) {
		MAP.put(id, factory);
	}

	public static ITrigger create(String id, List<ICondition> conditions, List<IAction> actions) {
		BiFunction<List<ICondition>, List<IAction>, ITrigger> f = MAP.get(id);
		if (f == null)
			throw new IllegalArgumentException("Unknown trigger id: " + id);
		return f.apply(conditions, actions);
	}

	private TriggerRegistry() {
	}
}
