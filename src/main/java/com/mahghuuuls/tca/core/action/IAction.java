package com.mahghuuuls.tca.core.action;

import com.mahghuuuls.tca.core.context.Context;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public interface IAction {
	void execute(Context ctx);
}