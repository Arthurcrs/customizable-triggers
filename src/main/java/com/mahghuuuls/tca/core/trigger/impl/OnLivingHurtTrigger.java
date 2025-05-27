package com.mahghuuuls.tca.core.trigger.impl;

import java.util.List;

import com.mahghuuuls.tca.annotation.RegisterTrigger;
import com.mahghuuuls.tca.core.action.IAction;
import com.mahghuuuls.tca.core.condition.ICondition;
import com.mahghuuuls.tca.core.context.Context;
import com.mahghuuuls.tca.core.context.CtxKeys;
import com.mahghuuuls.tca.core.trigger.Trigger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterTrigger("on_living_hurt")
public final class OnLivingHurtTrigger extends Trigger {

	public OnLivingHurtTrigger(List<ICondition> conditions, List<IAction> actions) {
		super(conditions, actions);
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

		Context ctx = Context.builder().put(CtxKeys.VICTIM, evt.getEntityLiving())
				.put(CtxKeys.ATTACKER, evt.getSource().getTrueSource()).build();

		run(ctx);
	}
}
