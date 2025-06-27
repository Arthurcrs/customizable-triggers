package com.mahghuuuls.configurabletriggers.core.action.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterAction;
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

@RegisterAction("apply_potion_effect")
public final class ApplyPotionEffectAction implements IAction {

	private final Potion potion;
	private final int duration;
	private final int amplifier;
	private final CtxKey<?> entityKey;

	private final static String FIELD_ENTITY = "entity";
	private final static String FIELD_AMPLIFIER = "amplifier";
	private final static String FIELD_DURATION = "duration";
	private final static String FIELD_POTION = "potion";

	public ApplyPotionEffectAction(Potion potion, int duration, int amplifier, CtxKey<?> key) {
		this.potion = potion;
		this.duration = duration;
		this.amplifier = amplifier;
		this.entityKey = key;
	}

	@Override
	public void execute(Context ctx) {
		if (potion == null)
			return;

		EntityLivingBase entity = ctx.get(entityKey, EntityLivingBase.class);
		if (entity != null && !entity.world.isRemote) {
			entity.addPotionEffect(new PotionEffect(potion, duration, amplifier));
		}
	}

	public static ApplyPotionEffectAction fromJson(JsonObject jsonObj) {

		if (!jsonObj.has(FIELD_ENTITY)) {
			throw new IllegalArgumentException("Action is missing required field: " + FIELD_ENTITY);
		}

		if (!jsonObj.has(FIELD_DURATION)) {
			throw new IllegalArgumentException("Action is missing required field: " + FIELD_DURATION);
		}

		if (!jsonObj.has(FIELD_POTION)) {
			throw new IllegalArgumentException("Action is missing required field: " + FIELD_POTION);
		}

		Potion potion = Potion.REGISTRY.getObject(new ResourceLocation(jsonObj.get(FIELD_POTION).getAsString()));
		int duration = jsonObj.get(FIELD_DURATION).getAsInt();
		int amplifier = jsonObj.has(FIELD_AMPLIFIER) ? jsonObj.get(FIELD_AMPLIFIER).getAsInt() : 0;
		String entity = jsonObj.get(FIELD_ENTITY).getAsString();

		CtxKey<?> key = CtxKeys.getKeyFromId(entity);

		return new ApplyPotionEffectAction(potion, duration, amplifier, key);
	}
}
