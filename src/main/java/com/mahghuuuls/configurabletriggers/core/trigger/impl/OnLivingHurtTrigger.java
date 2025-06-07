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

/**
 * A concrete trigger implementation that fires when any living entity in the
 * game takes damage. This trigger subscribes to the MinecraftForge's
 * {@link LivingHurtEvent}. It is registered with the ID "on_living_hurt" for
 * use in the `triggers.json` configuration.
 *
 * <p>
 * When this trigger activates, it constructs a {@link Context} object
 * containing various details about the damage event. This context is then used
 * to evaluate the trigger's conditions and execute its actions.
 * </p>
 *
 * <p>
 * The following context keys are populated by this trigger:
 * <ul>
 * <li>{@link CtxKeys#DAMAGED_ENTITY}: The {@link EntityLivingBase} that took
 * damage.</li>
 * <li>{@link CtxKeys#DAMAGE_AMOUNT}: The {@link Float} value of the damage
 * dealt.</li>
 * <li>{@link CtxKeys#DAMAGE_TYPE}: The {@link String} identifier of the damage
 * type (e.g., "generic", "magic").</li>
 * <li>{@link CtxKeys#TRUE_SOURCE}: The true source of the damage (e.g., the
 * attacking {@link EntityLivingBase},
 * {@link net.minecraft.entity.player.EntityPlayer}, or {@link Object} for
 * non-living sources).</li>
 * <li>{@link CtxKeys#IMMEDIATE_SOURCE}: The immediate source of the damage
 * (e.g., an arrow projectile, or the attacking entity itself if direct).</li>
 * <li>{@link CtxKeys#ATTACK_WEAPON}: (Optional) The
 * {@link net.minecraft.item.ItemStack} held by the true source if it's an
 * {@link EntityLivingBase}.</li>
 * <li>{@link CtxKeys#ATTACKER_ENTITY_ID}: (Optional) The {@link Integer} entity
 * ID of the true source if it's an {@link EntityLivingBase}.</li>
 * </ul>
 */
@RegisterTrigger("on_living_hurt")
public final class OnLivingHurtTrigger extends Trigger {

	/**
	 * Constructs an OnLivingHurtTrigger instance.
	 *
	 * @param name       The unique name of this trigger instance.
	 * @param conditions A list of {@link ICondition} instances that must all pass
	 *                   for this trigger to fire.
	 * @param actions    A list of {@link IAction} instances to execute if all
	 *                   conditions are met.
	 */
	public OnLivingHurtTrigger(String name, List<ICondition> conditions, List<IAction> actions) {
		super(name, conditions, actions);
	}

	/**
	 * Registers this trigger instance with the {@link MinecraftForge#EVENT_BUS}.
	 * This makes the {@link #onLivingHurt(LivingHurtEvent)} method eligible to
	 * receive events.
	 */
	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	/**
	 * Unregisters this trigger instance from the {@link MinecraftForge#EVENT_BUS}.
	 * This stops the trigger from listening to further events.
	 */
	@Override
	public void unregister() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	/**
	 * Event handler method for {@link LivingHurtEvent}. This method is
	 * automatically called by Minecraft Forge whenever a living entity takes
	 * damage.
	 *
	 * <p>
	 * This method first checks if the event is occurring on the logical client side
	 * and returns early if so, ensuring that trigger logic only runs on the server.
	 * It then constructs a comprehensive {@link Context} object with relevant event
	 * data and finally calls the inherited {@link Trigger#run(Context)} method to
	 * evaluate conditions and execute actions.
	 * </p>
	 *
	 * @param evt The {@link LivingHurtEvent} instance containing details about the
	 *            damage event.
	 */
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent evt) {
		// Only process events on the logical server side to prevent redundant execution
		// and client-side issues.
		if (evt.getEntity().world.isRemote)
			return;

		Object attacker = evt.getSource().getTrueSource(); // Get the true source of the damage.

		// Build the context object with data from the LivingHurtEvent.
		Context.Builder ctxBuilder = Context.builder();

		ctxBuilder.put(CtxKeys.DAMAGED_ENTITY, evt.getEntityLiving()); // The entity that was hurt.
		ctxBuilder.put(CtxKeys.DAMAGE_AMOUNT, evt.getAmount()); // The amount of damage dealt.
		ctxBuilder.put(CtxKeys.DAMAGE_TYPE, evt.getSource().getDamageType()); // The type of damage.
		ctxBuilder.put(CtxKeys.TRUE_SOURCE, evt.getSource().getTrueSource()); // The ultimate source of the damage
																				// (e.g., a Skeleton for an arrow).
		ctxBuilder.put(CtxKeys.IMMEDIATE_SOURCE, evt.getSource().getImmediateSource()); // The direct source (e.g., the
																						// Arrow itself).

		// If the true source is a living entity, add more specific context data about
		// the attacker.
		if (attacker instanceof EntityLivingBase) {
			EntityLivingBase livingAttacker = (EntityLivingBase) attacker;
			ctxBuilder.put(CtxKeys.ATTACK_WEAPON, livingAttacker.getHeldItemMainhand()); // The item held by the
																							// attacker.
			ctxBuilder.put(CtxKeys.ATTACKER_ENTITY_ID, livingAttacker.getEntityId()); // The entity ID of the attacker.
		}

		Context ctx = ctxBuilder.build(); // Create the immutable context.
		run(ctx); // Run the trigger logic (conditions and actions) with the prepared context.
	}
}
