package core.condition;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.reflections.Reflections;

import com.google.gson.JsonObject;

import annotation.RegisterCondition;

public final class ConditionRegistry {
	private static final Map<String, Function<JsonObject, ICondition>> MAP = new HashMap<>();

	public static void init() {
		Reflections refs = new Reflections("core.condition.impl");
		Set<Class<?>> classes = refs.getTypesAnnotatedWith(RegisterCondition.class);

		for (Class<?> cls : classes) {
			RegisterCondition anno = cls.getAnnotation(RegisterCondition.class);
			String id = anno.value();
			try {
				Method fromJson = cls.getMethod("fromJson", JsonObject.class);
				MAP.put(id, json -> {
					try {
						return (ICondition) fromJson.invoke(null, json);
					} catch (Exception e) {
						throw new RuntimeException("Failed to create condition: " + cls, e);
					}
				});
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Missing fromJson method in " + cls, e);
			}
		}
	}

	public static ICondition fromJson(JsonObject obj) {
		String id = obj.get("id").getAsString();
		Function<JsonObject, ICondition> f = MAP.get(id);
		if (f == null)
			throw new IllegalArgumentException("Unknown condition id: " + id);
		return f.apply(obj);
	}

	private ConditionRegistry() {
	}
}