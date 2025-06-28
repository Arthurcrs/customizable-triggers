package com.mahghuuuls.configurabletriggers.core.trigger.impl;

import java.util.List;

import com.mahghuuuls.configurabletriggers.annotation.RegisterTrigger;
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;
import com.mahghuuuls.configurabletriggers.core.trigger.Trigger;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Trigger that fires whenever a living entity jumps.
 */
@RegisterTrigger("on_living_jump")
public final class OnLivingJumpTrigger extends Trigger {

	public OnLivingJumpTrigger(String name, List<ICondition> conditions, List<IAction> actions) {
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
	public void onLivingJump(LivingJumpEvent evt) {
		if (evt.getEntity().world.isRemote)
			return;

		EntityLivingBase entity = evt.getEntityLiving();
		Context.Builder ctxBuilder = Context.builder();
		ctxBuilder.put(CtxKeys.ENTITY, entity);
		if (entity instanceof EntityPlayer) {
			ctxBuilder.put(CtxKeys.PLAYER, (EntityPlayer) entity);
		}

		Context ctx = ctxBuilder.build();
		run(ctx);
	}
}