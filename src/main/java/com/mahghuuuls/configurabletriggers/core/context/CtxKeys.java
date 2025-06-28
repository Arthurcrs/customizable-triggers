package com.mahghuuuls.configurabletriggers.core.context;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public final class CtxKeys {
	public static final CtxKey<EntityPlayer> PLAYER = new CtxKey<>("player");
	public static final CtxKey<EntityLivingBase> ENTITY = new CtxKey<>("entity");
	public static final CtxKey<EntityLivingBase> DAMAGED_ENTITY = new CtxKey<>("victim");
	public static final CtxKey<Object> TRUE_SOURCE = new CtxKey<>("attacker");
	public static final CtxKey<ItemStack> ATTACK_WEAPON = new CtxKey<>("attack_weapon");
	public static final CtxKey<Float> DAMAGE_AMOUNT = new CtxKey<>("damage");
	public static final CtxKey<String> DAMAGE_TYPE = new CtxKey<>("damage_type");
	public static final CtxKey<Object> IMMEDIATE_SOURCE = new CtxKey<>("immediate_source");

	// Keys register themselves in CtxKey when instantiated

	/**
	 * Returns the {@link CtxKey} associated with the given id.
	 *
	 * @param id the context id as used in JSON
	 * @return the matching {@link CtxKey}
	 * @throws IllegalArgumentException if the id is not recognized
	 */
	public static CtxKey<?> getKeyFromId(String id) {
		return CtxKey.forId(id);
	}

	private CtxKeys() {
	}
}