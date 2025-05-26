package core.trigger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import org.reflections.Reflections;

import annotation.RegisterTrigger;
import core.action.IAction;
import core.condition.ICondition;

public final class TriggerRegistry {

	private static final Map<String, BiFunction<List<ICondition>, List<IAction>, ITrigger>> MAP = new HashMap<>();

	public static void init() {
		Reflections refs = new Reflections("core.trigger.impl");
		Set<Class<?>> classes = refs.getTypesAnnotatedWith(RegisterTrigger.class);

		for (Class<?> cls : classes) {
			RegisterTrigger anno = cls.getAnnotation(RegisterTrigger.class);
			String id = anno.value();
			try {
				Constructor<?> ctor = cls.getConstructor(List.class, List.class);
				MAP.put(id, (conds, acts) -> {
					try {
						return (ITrigger) ctor.newInstance(conds, acts);
					} catch (Exception e) {
						throw new RuntimeException("Failed to create trigger: " + cls, e);
					}
				});
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Missing (List, List) constructor in " + cls, e);
			}
		}
	}

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
