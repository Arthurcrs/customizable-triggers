package com.mahghuuuls.configurabletriggers.core.action.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterAction;
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

/**
 * Action that inflicts damage on a target entity.
 */
@RegisterAction("damage_entity")
public final class DamageEntityAction implements IAction {

	private final CtxKey<?> targetKey;
	private final CtxKey<?> trueSourceKey;
	private final CtxKey<?> immediateSourceKey;
	private final float amount;
	private final String damageType;

	private static final String FIELD_TARGET = "target";
	private static final String FIELD_TRUE_SOURCE = "true_source";
	private static final String FIELD_IMMEDIATE_SOURCE = "immediate_source";
	private static final String FIELD_AMOUNT = "amount";
	private static final String FIELD_DAMAGE_TYPE = "damage_type";
	private static final String DEFAULT_DAMAGE_TYPE = "generic";

	public DamageEntityAction(CtxKey<?> targetKey, CtxKey<?> trueSourceKey, CtxKey<?> immediateSourceKey, float amount,
			String damageType) {
		this.targetKey = targetKey;
		this.trueSourceKey = trueSourceKey;
		this.immediateSourceKey = immediateSourceKey;
		this.amount = amount;
		this.damageType = damageType;
	}

	@Override
	public void execute(Context ctx) {
		Entity target = ctx.get(targetKey, Entity.class);
		if (target == null)
			return;

		Entity trueSource = null;
		Entity immediateSource = null;
		if (trueSourceKey != null)
			trueSource = ctx.get(trueSourceKey, Entity.class);
		if (immediateSourceKey != null)
			immediateSource = ctx.get(immediateSourceKey, Entity.class);

		DamageSource src;
		if (trueSource != null && immediateSource != null) {
			src = new EntityDamageSourceIndirect(damageType, immediateSource, trueSource);
		} else if (trueSource != null) {
			src = new EntityDamageSource(damageType, trueSource);
		} else {
			src = new DamageSource(damageType);
		}

		target.attackEntityFrom(src, amount);
	}

	public static DamageEntityAction fromJson(JsonObject obj) {
		if (!obj.has(FIELD_TARGET))
			throw new IllegalArgumentException("Action is missing required field: " + FIELD_TARGET);
		if (!obj.has(FIELD_AMOUNT))
			throw new IllegalArgumentException("Action is missing required field: " + FIELD_AMOUNT);

		CtxKey<?> target = CtxKeys.getKeyFromId(obj.get(FIELD_TARGET).getAsString());
		CtxKey<?> trueSource = obj.has(FIELD_TRUE_SOURCE)
				? CtxKeys.getKeyFromId(obj.get(FIELD_TRUE_SOURCE).getAsString())
				: null;
		CtxKey<?> immediateSource = obj.has(FIELD_IMMEDIATE_SOURCE)
				? CtxKeys.getKeyFromId(obj.get(FIELD_IMMEDIATE_SOURCE).getAsString())
				: null;
		float amount = obj.get(FIELD_AMOUNT).getAsFloat();
		String type = obj.has(FIELD_DAMAGE_TYPE) ? obj.get(FIELD_DAMAGE_TYPE).getAsString() : DEFAULT_DAMAGE_TYPE;

		return new DamageEntityAction(target, trueSource, immediateSource, amount, type);
	}
}