package com.mahghuuuls.configurabletriggers.core.condition.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

/**
 * Condition that checks if the entity from the context matches a specific
 * Minecraft entity ID.
 */
@RegisterCondition("entity_matches_id")
public final class EntityMatchesIdCondition implements ICondition {
	private final ResourceLocation targetId;
	private final CtxKey<?> entityKey;

	private static final String FIELD_ENTITY = "entity";
	private static final String FIELD_ID = "id";

	private EntityMatchesIdCondition(ResourceLocation id, CtxKey<?> key) {
		this.targetId = id;
		this.entityKey = key;
	}

	@Override
	public boolean test(Context ctx) {
		Entity entity = ctx.get(entityKey, Entity.class);
		if (entity == null) {
			return false;
		}
		ResourceLocation id = EntityList.getKey(entity);
		return id != null && id.equals(targetId);
	}

	/**
	 * Creates this condition from its JSON representation. Required fields are
	 * "entity" and "id".
	 */
	public static EntityMatchesIdCondition fromJson(JsonObject jsonObj) {
		if (!jsonObj.has(FIELD_ENTITY)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_ENTITY);
		}
		if (!jsonObj.has(FIELD_ID)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_ID);
		}

		String entityName = jsonObj.get(FIELD_ENTITY).getAsString();
		String id = jsonObj.get(FIELD_ID).getAsString();

		CtxKey<?> key = CtxKeys.getKeyFromId(entityName);
		ResourceLocation rl = new ResourceLocation(id);

		return new EntityMatchesIdCondition(rl, key);
	}
}