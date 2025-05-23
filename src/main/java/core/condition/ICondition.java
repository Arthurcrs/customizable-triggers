package core.condition;

import core.context.Context;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public interface ICondition {
    boolean test(Context ctx);
}