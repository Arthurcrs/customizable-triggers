package com.mahghuuuls.configurabletriggers.core.action.impl;

import java.util.function.Consumer;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterAction;
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

@RegisterAction("apply_potion_effect")
public final class ApplyPotionEffectAction implements IAction {

	public enum Target {
		ATTACKER, VICTIM, BOTH
	}

	private final Potion potion;
	private final int duration;
	private final int amplifier;
	private final Target target;

	private final static String FIELD_TARGET = "target";
	private final static String FIELD_AMPLIFIER = "amplifier";
	private final static String FIELD_DURATION = "duration";
	private final static String FIELD_POTION = "potion";

	public ApplyPotionEffectAction(Potion potion, int duration, int amplifier, Target target) {
		this.potion = potion;
		this.duration = duration;
		this.amplifier = amplifier;
		this.target = target;
	}

	@Override
	public void execute(Context ctx) {
		if (potion == null)
			return;

		Consumer<EntityLivingBase> give = entity -> {
			if (entity != null && !entity.world.isRemote) {
				entity.addPotionEffect(new PotionEffect(potion, duration, amplifier));
			}
		};

		if (target == Target.ATTACKER || target == Target.BOTH) {
			give.accept(ctx.get(CtxKeys.TRUE_SOURCE, EntityLivingBase.class));
		}

		if (target == Target.VICTIM || target == Target.BOTH) {
			give.accept(ctx.get(CtxKeys.DAMAGED_ENTITY, EntityLivingBase.class));
		}
	}

	public static ApplyPotionEffectAction fromJson(JsonObject obj) {
		Potion potion = Potion.REGISTRY.getObject(new ResourceLocation(obj.get(FIELD_POTION).getAsString()));
		int duration = obj.get(FIELD_DURATION).getAsInt();
		int amplifier = obj.has(FIELD_AMPLIFIER) ? obj.get(FIELD_AMPLIFIER).getAsInt() : 0;
		Target target = obj.has(FIELD_TARGET) ? Target.valueOf(obj.get(FIELD_TARGET).getAsString().toUpperCase())
				: Target.VICTIM;

		return new ApplyPotionEffectAction(potion, duration, amplifier, target);
	}
}
