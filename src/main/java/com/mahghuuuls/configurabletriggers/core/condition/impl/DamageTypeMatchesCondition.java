package com.mahghuuuls.configurabletriggers.core.condition.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

/**
 * Condition that checks if a damage type from the context matches a target
 * value.
 */
@RegisterCondition("damage_type_matches")
public final class DamageTypeMatchesCondition implements ICondition {
	private final CtxKey<?> typeKey;
	private final String targetType;

	private static final String FIELD_INPUT_TYPE = "input_type";
	private static final String FIELD_TARGET_VALUE = "target_value";

	public DamageTypeMatchesCondition(CtxKey<?> typeKey, String targetType) {
		this.typeKey = typeKey;
		this.targetType = targetType;
	}

	@Override
	public boolean test(Context ctx) {
		String value = ctx.get(typeKey, String.class);
		return value != null && value.equals(targetType);
	}

	/**
	 * Creates a DamageTypeMatchesCondition from its JSON representation. Required
	 * fields are "damage_type" and "target".
	 */
	public static DamageTypeMatchesCondition fromJson(JsonObject jsonObj) {
		if (!jsonObj.has(FIELD_INPUT_TYPE)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_INPUT_TYPE);
		}
		if (!jsonObj.has(FIELD_TARGET_VALUE)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_TARGET_VALUE);
		}

		String keyId = jsonObj.get(FIELD_INPUT_TYPE).getAsString();
		String target = jsonObj.get(FIELD_TARGET_VALUE).getAsString();

		CtxKey<?> key = CtxKeys.getKeyFromId(keyId);
		return new DamageTypeMatchesCondition(key, target);
	}
}