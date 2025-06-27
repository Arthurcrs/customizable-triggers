package com.mahghuuuls.configurabletriggers.core.condition.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.Entity;

/**
 * Condition that checks if two entities are within a certain distance of each
 * other.
 */
@RegisterCondition("entities_within_distance")
public final class EntitiesWithinDistanceCondition implements ICondition {
	private final CtxKey<?> entityAKey;
	private final CtxKey<?> entityBKey;
	private final double distance;

	private static final String FIELD_ENTITY_A = "entity_a";
	private static final String FIELD_ENTITY_B = "entity_b";
	private static final String FIELD_DISTANCE = "distance";

	public EntitiesWithinDistanceCondition(CtxKey<?> entityAKey, CtxKey<?> entityBKey, double distance) {
		this.entityAKey = entityAKey;
		this.entityBKey = entityBKey;
		this.distance = distance;
	}

	@Override
	public boolean test(Context ctx) {
		Entity a = ctx.get(entityAKey, Entity.class);
		Entity b = ctx.get(entityBKey, Entity.class);
		if (a == null || b == null)
			return false;

		double dx = a.posX - b.posX;
		double dy = a.posY - b.posY;
		double dz = a.posZ - b.posZ;
		return dx * dx + dy * dy + dz * dz <= distance * distance;
	}

	public static EntitiesWithinDistanceCondition fromJson(JsonObject jsonObj) {
		if (!jsonObj.has(FIELD_ENTITY_A)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_ENTITY_A);
		}
		if (!jsonObj.has(FIELD_ENTITY_B)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_ENTITY_B);
		}
		if (!jsonObj.has(FIELD_DISTANCE)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_DISTANCE);
		}

		String aId = jsonObj.get(FIELD_ENTITY_A).getAsString();
		String bId = jsonObj.get(FIELD_ENTITY_B).getAsString();
		double dist = jsonObj.get(FIELD_DISTANCE).getAsDouble();

		CtxKey<?> aKey = CtxKeys.getKeyFromId(aId);
		CtxKey<?> bKey = CtxKeys.getKeyFromId(bId);

		return new EntitiesWithinDistanceCondition(aKey, bKey, dist);
	}
}