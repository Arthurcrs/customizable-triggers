package com.mahghuuuls.configurabletriggers.core.condition.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Condition that enforces a per-player cooldown. Once the condition passes for
 * a player, it will return {@code false} for that player until the specified
 * number of ticks has elapsed.
 */
@RegisterCondition("cooldown")
public final class PlayerCooldownCondition implements ICondition {
    private static final Map<UUID, Long> LAST_TRIGGER_TICK = new HashMap<>();

    private final int cooldownTicks;
    private final CtxKey<?> playerKey;

    private static final String FIELD_TICKS = "ticks";
    private static final String FIELD_PLAYER = "player";

    private PlayerCooldownCondition(int ticks, CtxKey<?> key) {
        this.cooldownTicks = ticks;
        this.playerKey = key;
    }

    @Override
    public boolean test(Context ctx) {
        EntityPlayer player = ctx.get(playerKey, EntityPlayer.class);
        if (player == null)
            return false;

        long current = player.ticksExisted;
        UUID playerId = player.getUniqueID();
        Long last = LAST_TRIGGER_TICK.get(playerId);
        if (last == null || current - last >= cooldownTicks) {
            LAST_TRIGGER_TICK.put(playerId, current);
            return true;
        }
        return false;
    }

    /**
     * Creates this condition from its JSON representation. Required fields are
     * "player" and "ticks".
     */
    public static PlayerCooldownCondition fromJson(JsonObject jsonObj) {
        if (!jsonObj.has(FIELD_PLAYER)) {
            throw new IllegalArgumentException("Condition is missing required field: " + FIELD_PLAYER);
        }
        if (!jsonObj.has(FIELD_TICKS)) {
            throw new IllegalArgumentException("Condition is missing required field: " + FIELD_TICKS);
        }

        String playerId = jsonObj.get(FIELD_PLAYER).getAsString();
        int ticks = jsonObj.get(FIELD_TICKS).getAsInt();
        if (ticks < 0) {
            throw new IllegalArgumentException(FIELD_TICKS + " must be non-negative");
        }

        CtxKey<?> key = CtxKeys.getKeyFromId(playerId);
        return new PlayerCooldownCondition(ticks, key);
    }
}