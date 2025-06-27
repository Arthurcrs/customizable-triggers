package com.mahghuuuls.configurabletriggers.core.action.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterAction;
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.EntityLivingBase;

/**
 * Action that heals a living entity.
 */
@RegisterAction("heal_entity")
public final class HealEntityAction implements IAction {

	private final CtxKey<?> targetKey;
	private final float amount;

	private static final String FIELD_TARGET = "target";
	private static final String FIELD_AMOUNT = "amount";

	public HealEntityAction(CtxKey<?> targetKey, float amount) {
		this.targetKey = targetKey;
		this.amount = amount;
	}

	@Override
	public void execute(Context ctx) {
		EntityLivingBase entity = ctx.get(targetKey, EntityLivingBase.class);
		if (entity != null) {
			entity.heal(amount);
		}
	}

	public static HealEntityAction fromJson(JsonObject obj) {
		if (!obj.has(FIELD_TARGET))
			throw new IllegalArgumentException("Action is missing required field: " + FIELD_TARGET);
		if (!obj.has(FIELD_AMOUNT))
			throw new IllegalArgumentException("Action is missing required field: " + FIELD_AMOUNT);

		CtxKey<?> target = CtxKeys.getKeyFromId(obj.get(FIELD_TARGET).getAsString());
		float amt = obj.get(FIELD_AMOUNT).getAsFloat();

		return new HealEntityAction(target, amt);
	}
}