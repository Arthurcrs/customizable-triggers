package com.mahghuuuls.configurabletriggers.core.condition.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.Entity;

/**
 * Condition that checks if an entity is currently in a specific dimension.
 */
@RegisterCondition("entity_in_dimension")
public final class EntityInDimensionCondition implements ICondition {
	private final int dimensionId;
	private final CtxKey<?> entityKey;

	private static final String FIELD_ENTITY = "entity";
	private static final String FIELD_DIMENSION = "dimension";

	public EntityInDimensionCondition(int dimensionId, CtxKey<?> entityKey) {
		this.dimensionId = dimensionId;
		this.entityKey = entityKey;
	}

	@Override
	public boolean test(Context ctx) {
		Entity entity = ctx.get(entityKey, Entity.class);
		return entity != null && entity.world.provider.getDimension() == dimensionId;
	}

	/**
	 * Creates an EntityInDimensionCondition from its JSON representation. The JSON
	 * object must contain an "entity" field referencing a context key and a
	 * "dimension" field with the desired dimension id.
	 */
	public static EntityInDimensionCondition fromJson(JsonObject jsonObj) {
		if (!jsonObj.has(FIELD_ENTITY)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_ENTITY);
		}
		if (!jsonObj.has(FIELD_DIMENSION)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_DIMENSION);
		}

		String id = jsonObj.get(FIELD_ENTITY).getAsString();
		int dimension = jsonObj.get(FIELD_DIMENSION).getAsInt();
		CtxKey<?> key = CtxKeys.getKeyFromId(id);

		return new EntityInDimensionCondition(dimension, key);
	}
}