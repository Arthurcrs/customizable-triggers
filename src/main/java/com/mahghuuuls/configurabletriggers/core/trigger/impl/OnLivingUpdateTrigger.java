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
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Trigger that fires every tick for living entities. It provides the entity
 * involved in the {@link Context} under {@link CtxKeys#ENTITY}. If the entity
 * is a player, {@link CtxKeys#PLAYER} is also populated.
 */
@RegisterTrigger("on_living_update")
public final class OnLivingUpdateTrigger extends Trigger {

	public OnLivingUpdateTrigger(String name, List<ICondition> conditions, List<IAction> actions) {
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
	public void onLivingUpdate(LivingUpdateEvent evt) {
		if (evt.getEntity().world.isRemote)
			return;

		EntityLivingBase entity = evt.getEntityLiving();
		Context.Builder ctxBuilder = Context.builder();
		ctxBuilder.put(CtxKeys.ENTITY, entity);

		Context ctx = ctxBuilder.build();
		run(ctx);
	}
}