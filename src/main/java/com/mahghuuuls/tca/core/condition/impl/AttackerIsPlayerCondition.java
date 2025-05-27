package com.mahghuuuls.tca.core.condition.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.tca.annotation.RegisterCondition;
import com.mahghuuuls.tca.core.condition.ICondition;
import com.mahghuuuls.tca.core.context.Context;
import com.mahghuuuls.tca.core.context.CtxKeys;

import net.minecraft.entity.player.EntityPlayer;

@RegisterCondition("attacker_is_player")
public final class AttackerIsPlayerCondition implements ICondition {

	@Override
	public boolean test(Context ctx) {
		return ctx.get(CtxKeys.ATTACKER, Object.class) instanceof EntityPlayer;
	}

	public static AttackerIsPlayerCondition fromJson(JsonObject obj) {
		return new AttackerIsPlayerCondition();
	}
}
