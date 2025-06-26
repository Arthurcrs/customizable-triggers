package com.mahghuuuls.configurabletriggers.core.condition.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

@RegisterCondition("entity_has_potion_effect")
public final class EntityHasPotionEffectCondition implements ICondition {
	private final Potion potion;
	private final CtxKey<?> entityKey;

	private static final String FIELD_EFFECT = "effect";
	private static final String FIELD_ENTITY = "entity";

	public EntityHasPotionEffectCondition(Potion potionEffect, CtxKey<?> entityKey) {
		this.potion = potionEffect;
		this.entityKey = entityKey;
	}

	@Override
	public boolean test(Context ctx) {
		EntityLivingBase entity = ctx.get(entityKey, EntityLivingBase.class);
		if (entity == null || potion == null)
			return false;
		return entity.getActivePotionEffect(potion) != null;
	}

	public static EntityHasPotionEffectCondition fromJson(JsonObject obj) {
		if (!obj.has(FIELD_EFFECT)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_EFFECT);
		}
		if (!obj.has(FIELD_ENTITY)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_ENTITY);
		}

		String effectName = obj.get(FIELD_EFFECT).getAsString();
		Potion potionEffect = Potion.REGISTRY.getObject(new ResourceLocation(effectName));
		String entity = obj.get(FIELD_ENTITY).getAsString();

		CtxKey<?> key = CtxKeys.getKeyFromId(entity);

		return new EntityHasPotionEffectCondition(potionEffect, key);
	}
}