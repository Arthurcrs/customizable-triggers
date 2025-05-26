package core.action.impl;

import com.google.gson.JsonObject;

import core.action.IAction;
import core.context.Context;
import core.context.CtxKeys;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

/**
 * Applies a PotionEffect to a target entity.
 * 
 * JSON example: { "id": "apply_potion", "potion": "minecraft:speed",
 * "duration": 100, "amplifier": 1, "target": "attacker" // or "victim", or
 * "both" }
 */
public final class ApplyPotionEffectAction implements IAction {

	public enum Target {
		ATTACKER, VICTIM, BOTH
	}

	private final Potion potion;
	private final int duration;
	private final int amplifier;
	private final Target target;

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

		java.util.function.Consumer<EntityLivingBase> give = entity -> {
			if (entity != null && !entity.world.isRemote) {
				entity.addPotionEffect(new PotionEffect(potion, duration, amplifier));
			}
		};

		if (target == Target.ATTACKER || target == Target.BOTH) {
			give.accept(ctx.get(CtxKeys.ATTACKER, EntityLivingBase.class));
		}

		if (target == Target.VICTIM || target == Target.BOTH) {
			give.accept(ctx.get(CtxKeys.VICTIM_OF_ATTACK, EntityLivingBase.class));
		}
	}

	public static ApplyPotionEffectAction fromJson(JsonObject obj) {
		Potion potion = Potion.REGISTRY.getObject(new ResourceLocation(obj.get("potion").getAsString()));
		int duration = obj.get("duration").getAsInt();
		int amplifier = obj.has("amplifier") ? obj.get("amplifier").getAsInt() : 0;
		Target target = obj.has("target") ? Target.valueOf(obj.get("target").getAsString().toUpperCase())
				: Target.VICTIM;

		return new ApplyPotionEffectAction(potion, duration, amplifier, target);
	}
}
