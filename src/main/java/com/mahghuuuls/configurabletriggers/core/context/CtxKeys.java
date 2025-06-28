package com.mahghuuuls.configurabletriggers.core.context;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;

public final class CtxKeys {
	public static final CtxKey<EntityPlayer> PLAYER = new CtxKey<>("player");
	public static final CtxKey<EntityLivingBase> ENTITY = new CtxKey<>("entity");
	public static final CtxKey<EntityLivingBase> DAMAGED_ENTITY = new CtxKey<>("victim");
	public static final CtxKey<Object> TRUE_SOURCE = new CtxKey<>("attacker");
	public static final CtxKey<ItemStack> ATTACK_WEAPON = new CtxKey<>("attack_weapon");
	public static final CtxKey<Float> DAMAGE_AMOUNT = new CtxKey<>("damage");
	public static final CtxKey<String> DAMAGE_TYPE = new CtxKey<>("damage_type");
	public static final CtxKey<Object> IMMEDIATE_SOURCE = new CtxKey<>("immediate_source");
	public static final CtxKey<PotionEffect> POTION_EFFECT = new CtxKey<>("potion_effect");
	public static final CtxKey<IBlockState> BLOCK_STATE = new CtxKey<>("block_state");
	public static final CtxKey<BlockPos> BLOCK_POS = new CtxKey<>("block_pos");

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