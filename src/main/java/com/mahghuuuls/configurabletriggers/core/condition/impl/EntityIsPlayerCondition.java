package com.mahghuuuls.configurabletriggers.core.condition.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.player.EntityPlayer;

@RegisterCondition("entity_is_player")
public final class EntityIsPlayerCondition implements ICondition {
	private final CtxKey<?> entityKey;

	private static final String FIELD_ENTITY = "entity";

	public EntityIsPlayerCondition(CtxKey<?> key) {
		this.entityKey = key;
	}

	@Override
	public boolean test(Context ctx) {
		return ctx.get(entityKey, Object.class) instanceof EntityPlayer;
	}

	public static EntityIsPlayerCondition fromJson(JsonObject obj) {

		if (!obj.has(FIELD_ENTITY)) {
			throw new IllegalArgumentException("Condition is missing required field " + FIELD_ENTITY);
		}

		String entityName = obj.get(FIELD_ENTITY).getAsString();

		CtxKey<?> key;
		if (entityName.equals(CtxKeys.TRUE_SOURCE.getId())) {
			key = CtxKeys.TRUE_SOURCE;
		} else if (entityName.equals(CtxKeys.DAMAGED_ENTITY.getId())) {
			key = CtxKeys.DAMAGED_ENTITY;
		} else if (entityName.equals(CtxKeys.PLAYER.getId())) {
			key = CtxKeys.PLAYER;
		} else if (entityName.equals(CtxKeys.IMMEDIATE_SOURCE.getId())) {
			key = CtxKeys.IMMEDIATE_SOURCE;
		} else {
			throw new IllegalArgumentException("Unknown entity context: " + entityName);
		}

		return new EntityIsPlayerCondition(key);
	}
}