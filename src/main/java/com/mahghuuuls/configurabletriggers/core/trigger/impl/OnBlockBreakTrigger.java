package com.mahghuuuls.configurabletriggers.core.trigger.impl;

import java.util.List;

import com.mahghuuuls.configurabletriggers.annotation.RegisterTrigger;
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;
import com.mahghuuuls.configurabletriggers.core.trigger.Trigger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Trigger fired when a block is broken by a player.
 */
@RegisterTrigger("on_block_break")
public final class OnBlockBreakTrigger extends Trigger {

	public OnBlockBreakTrigger(String name, List<ICondition> conditions, List<IAction> actions) {
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
	public void onBreak(BreakEvent evt) {
		EntityPlayer player = evt.getPlayer();
		if (player == null || player.world.isRemote)
			return;

		Context.Builder ctxBuilder = Context.builder();
		ctxBuilder.put(CtxKeys.PLAYER, player);
		ctxBuilder.put(CtxKeys.ENTITY, player);
		ctxBuilder.put(CtxKeys.BLOCK_POS, evt.getPos());
		ctxBuilder.put(CtxKeys.BLOCK_STATE, evt.getState());
		Context ctx = ctxBuilder.build();
		run(ctx);
	}
}