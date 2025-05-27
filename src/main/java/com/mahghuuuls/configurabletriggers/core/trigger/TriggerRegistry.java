package com.mahghuuuls.configurabletriggers.core.trigger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.mahghuuuls.configurabletriggers.annotation.RegisterTrigger;
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;

public final class TriggerRegistry {

	private static final Map<String, TriggerFactory> MAP = new HashMap<>();

	public static void init() {
		Reflections refs = new Reflections("com.mahghuuuls.configurabletriggers.core.trigger.impl");
		Set<Class<?>> classes = refs.getTypesAnnotatedWith(RegisterTrigger.class);

		for (Class<?> cls : classes) {
			RegisterTrigger anno = cls.getAnnotation(RegisterTrigger.class);
			String triggerEvent = anno.value();
			try {
				Constructor<?> ctor = cls.getConstructor(String.class, List.class, List.class);
				MAP.put(triggerEvent, (name, conds, acts) -> {
					try {
						return (ITrigger) ctor.newInstance(name, conds, acts);
					} catch (Exception e) {
						throw new RuntimeException("[Configurable Triggers] Failed to instantiate trigger: " + cls, e);
					}
				});
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(
						"[Configurable Triggers] Missing constructor (String, List<ICondition>, List<IAction>) in "
								+ cls,
						e);
			}
		}
	}

	public static void register(String triggerEvent, TriggerFactory triggerFactory) {
		MAP.put(triggerEvent, triggerFactory);
	}

	public static ITrigger create(String name, String triggerEvent, List<ICondition> conditions,
			List<IAction> actions) {
		TriggerFactory triggerFactory = MAP.get(triggerEvent);
		if (triggerFactory == null)
			throw new IllegalArgumentException("[Configurable Triggers] Unknown trigger id: " + triggerEvent);
		return triggerFactory.create(name, conditions, actions);
	}

	private TriggerRegistry() {
	}
}
