package core.condition.impl;

import core.condition.ICondition;
import core.context.Context;
import core.context.CtxKeys;
import net.minecraft.entity.player.EntityPlayer;

public final class AttackerIsPlayerCondition implements ICondition {

    @Override
    public boolean test(Context ctx) {
        return ctx.get(CtxKeys.ATTACKER, Object.class) instanceof EntityPlayer;
    }
}
