package com.mahghuuuls.configurabletriggers.core.action;

import com.mahghuuuls.configurabletriggers.core.context.Context;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public interface IAction {
	void execute(Context ctx);
}