package com.mahghuuuls.configurabletriggers.core.condition.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.Entity;

/**
 * Condition that checks if an entity in the context is currently running
 * (sprinting) in the game.
 */
@RegisterCondition("entity_is_running")
public final class EntityIsRunningCondition implements ICondition {
	private final CtxKey<?> entityKey;

	private static final String FIELD_ENTITY = "entity";

	public EntityIsRunningCondition(CtxKey<?> key) {
		this.entityKey = key;
	}

	@Override
	public boolean test(Context ctx) {
		Entity entity = ctx.get(entityKey, Entity.class);
		return entity != null && entity.isSprinting();
	}

	/**
	 * Creates an EntityIsRunningCondition from its JSON representation. The JSON
	 * object must contain an "entity" field referencing a context key that should
	 * resolve to an {@link Entity} at runtime.
	 */
	public static EntityIsRunningCondition fromJson(JsonObject jsonObj) {
		if (!jsonObj.has(FIELD_ENTITY)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_ENTITY);
		}

		String id = jsonObj.get(FIELD_ENTITY).getAsString();
		CtxKey<?> key = CtxKeys.getKeyFromId(id);

		return new EntityIsRunningCondition(key);
	}
}