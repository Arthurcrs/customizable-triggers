package com.mahghuuuls.configurabletriggers.core.condition.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;

/**
 * Condition that checks whether an entity is classified as passive, hostile, or
 * neutral.
 */
@RegisterCondition("entity_classification")
public final class EntityClassificationCondition implements ICondition {
	private final CtxKey<?> entityKey;
	private final Type expected;

	private static final String FIELD_ENTITY = "entity";
	private static final String FIELD_TYPE = "type";

	private enum Type {
		PASSIVE("passive"), HOSTILE("hostile"), NEUTRAL("neutral");

		private final String id;

		Type(String id) {
			this.id = id;
		}

		static Type fromString(String str) {
			for (Type t : values()) {
				if (t.id.equalsIgnoreCase(str))
					return t;
			}
			throw new IllegalArgumentException("Unknown type: " + str);
		}
	}

	public EntityClassificationCondition(CtxKey<?> entityKey, Type expected) {
		this.entityKey = entityKey;
		this.expected = expected;
	}

	@Override
	public boolean test(Context ctx) {
		Entity entity = ctx.get(entityKey, Entity.class);
		if (entity == null)
			return false;

		boolean isHostile = entity instanceof IMob;
		boolean isAnimal = entity instanceof IAnimals;

		switch (expected) {
		case HOSTILE:
			return isHostile;
		case PASSIVE:
			return isAnimal && !isHostile;
		case NEUTRAL:
			return !isHostile && !isAnimal;
		default:
			return false;
		}
	}

	/**
	 * Creates an EntityClassificationCondition from its JSON representation.
	 * Required fields are "entity" and "type".
	 */
	public static EntityClassificationCondition fromJson(JsonObject jsonObj) {
		if (!jsonObj.has(FIELD_ENTITY)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_ENTITY);
		}
		if (!jsonObj.has(FIELD_TYPE)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_TYPE);
		}

		String keyId = jsonObj.get(FIELD_ENTITY).getAsString();
		String typeName = jsonObj.get(FIELD_TYPE).getAsString();

		CtxKey<?> key = CtxKeys.getKeyFromId(keyId);
		Type type = Type.fromString(typeName);

		return new EntityClassificationCondition(key, type);
	}
}