package com.mahghuuuls.tca.core.action;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.reflections.Reflections;

import com.google.gson.JsonObject;
import com.mahghuuuls.tca.annotation.RegisterAction;

public final class ActionRegistry {
	private static final Map<String, Function<JsonObject, IAction>> MAP = new HashMap<>();

	public static void init() {
		Reflections refs = new Reflections("com.mahghuuuls.tca.core.action.impl");
		Set<Class<?>> actions = refs.getTypesAnnotatedWith(RegisterAction.class);

		for (Class<?> cls : actions) {
			RegisterAction anno = cls.getAnnotation(RegisterAction.class);
			String id = anno.value();
			try {
				Method fromJson = cls.getMethod("fromJson", JsonObject.class);
				MAP.put(id, json -> {
					try {
						return (IAction) fromJson.invoke(null, json);
					} catch (Exception e) {
						throw new RuntimeException("Failed to create action: " + cls, e);
					}
				});
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Missing fromJson in " + cls, e);
			}
		}
	}

	public static IAction fromJson(JsonObject obj) {
		String id = obj.get("id").getAsString();
		Function<JsonObject, IAction> f = MAP.get(id);
		if (f == null)
			throw new IllegalArgumentException("Unknown action id: " + id);
		return f.apply(obj);
	}
}