package core.trigger.impl;

import java.util.List;

import core.action.IAction;
import core.condition.ICondition;
import core.context.Context;
import core.context.CtxKeys;
import core.trigger.Trigger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class OnLivingHurtTrigger extends Trigger {

	public OnLivingHurtTrigger(List<ICondition> conditions, List<IAction> actions) {
		super(conditions,actions);
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
		if (evt.getEntity().world.isRemote) return;
        
        Context ctx = Context.builder()
        		.put(CtxKeys.VICTIM_OF_ATTACK, evt.getEntityLiving())
        		.put(CtxKeys.ATTACKER, evt.getSource().getTrueSource())
        		.build();
        
        run(ctx);
    }
}
