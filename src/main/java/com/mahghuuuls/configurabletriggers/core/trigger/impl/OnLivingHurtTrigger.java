package com.mahghuuuls.configurabletriggers.core.trigger.impl;

import java.util.List;

import com.mahghuuuls.configurabletriggers.annotation.RegisterTrigger;
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;
import com.mahghuuuls.configurabletriggers.core.trigger.Trigger;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterTrigger("on_living_hurt")
public final class OnLivingHurtTrigger extends Trigger {

	public OnLivingHurtTrigger(String name, List<ICondition> conditions, List<IAction> actions) {
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
	public void onLivingHurt(LivingHurtEvent evt) {
		if (evt.getEntity().world.isRemote)
			return;

		Object attacker = evt.getSource().getTrueSource();

		Context.Builder ctxBuilder = Context.builder();

		ctxBuilder.put(CtxKeys.DAMAGED_ENTITY, evt.getEntityLiving());
		ctxBuilder.put(CtxKeys.DAMAGE_AMOUNT, evt.getAmount());
		ctxBuilder.put(CtxKeys.DAMAGE_TYPE, evt.getSource().getDamageType());
		ctxBuilder.put(CtxKeys.TRUE_SOURCE, evt.getSource().getTrueSource()); // The skeleton
		ctxBuilder.put(CtxKeys.IMMEDIATE_SOURCE, evt.getSource().getImmediateSource()); // The arrow

		if (attacker instanceof EntityLivingBase) {
			EntityLivingBase livingAttacker = (EntityLivingBase) attacker;
			ctxBuilder.put(CtxKeys.ATTACK_WEAPON, livingAttacker.getHeldItemMainhand());
			ctxBuilder.put(CtxKeys.ATTACKER_ENTITY_ID, livingAttacker.getEntityId());
		}

		Context ctx = ctxBuilder.build();
		run(ctx);
	}
}
