package core.action;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.JsonObject;

public final class ActionRegistry {
	private static final Map<String, Function<JsonObject, IAction>> MAP = new HashMap<>();

	public static void register(String id, Function<JsonObject, IAction> factory) {
		MAP.put(id, factory);
	}

	public static IAction fromJson(JsonObject obj) {
		String id = obj.get("id").getAsString();
		Function<JsonObject, IAction> f = MAP.get(id);
		if (f == null)
			throw new IllegalArgumentException("Unknown action id: " + id);
		return f.apply(obj);
	}

	private ActionRegistry() {
	}
}