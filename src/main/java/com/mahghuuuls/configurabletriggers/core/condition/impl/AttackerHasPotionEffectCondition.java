package com.mahghuuuls.configurabletriggers.core.condition.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

@RegisterCondition("attacker_has_potion_effect")
public final class AttackerHasPotionEffectCondition implements ICondition {
	private final Potion potion;

	public AttackerHasPotionEffectCondition(Potion potionEffect) {
		this.potion = potionEffect;
	}

	@Override
	public boolean test(Context ctx) {
		EntityLivingBase attacker = ctx.get(CtxKeys.ATTACKER, EntityLivingBase.class);
		if (attacker == null || potion == null)
			return false;
		return attacker.getActivePotionEffect(potion) != null;
	}

	public static AttackerHasPotionEffectCondition fromJson(JsonObject obj) {
		String effectName = obj.get("effect").getAsString();
		Potion potionEffect = Potion.REGISTRY.getObject(new ResourceLocation(effectName));
		return new AttackerHasPotionEffectCondition(potionEffect);
	}
}
