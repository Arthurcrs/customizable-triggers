package com.mahghuuuls.configurabletriggers.core.trigger.impl;

import java.util.List;

import com.mahghuuuls.configurabletriggers.annotation.RegisterTrigger;
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;
import com.mahghuuuls.configurabletriggers.core.trigger.Trigger;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Trigger fired when a potion effect is added to a living entity.
 */
@RegisterTrigger("on_potion_added")
public final class OnPotionEffectAddedTrigger extends Trigger {

	public OnPotionEffectAddedTrigger(String name, List<ICondition> conditions, List<IAction> actions) {
		super(name, conditions, actions);
	}

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void unregister() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void onPotionAdded(PotionAddedEvent evt) {
		EntityLivingBase entity = evt.getEntityLiving();
		if (entity.world.isRemote)
			return;

		PotionEffect effect = evt.getPotionEffect();

		Context.Builder ctxBuilder = Context.builder();
		ctxBuilder.put(CtxKeys.ENTITY, entity);
		ctxBuilder.put(CtxKeys.POTION_EFFECT, effect);
		Context ctx = ctxBuilder.build();
		run(ctx);
	}
}