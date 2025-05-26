package core.condition;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.JsonObject;

import core.condition.impl.AttackerIsPlayerCondition;

public final class ConditionRegistry {
	private static final Map<String, Function<JsonObject, ICondition>> MAP = new HashMap<>();

	public static void register(String id, Function<JsonObject, ICondition> factory) {
		MAP.put(id, factory);
	}

	public static ICondition fromJson(JsonObject obj) {
		String id = obj.get("id").getAsString();
		Function<JsonObject, ICondition> f = MAP.get(id);
		if (f == null)
			throw new IllegalArgumentException("Unknown condition id: " + id);
		return f.apply(obj);
	}

	public static void registerAll() {
		register(ConditionKeys.ATTACKER_IS_PLAYER, obj -> new AttackerIsPlayerCondition());
	}

	private ConditionRegistry() {
	}
}