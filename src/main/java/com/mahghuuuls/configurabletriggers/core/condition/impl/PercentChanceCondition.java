package com.mahghuuuls.configurabletriggers.core.condition.impl;

import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;

@RegisterCondition("percent_chance")
public final class PercentChanceCondition implements ICondition {
	private final double percentChance;

	public PercentChanceCondition(double chance) {
		if (chance < 0 || chance > 100) {
			throw new IllegalArgumentException("'chance' must be between 0 and 100");
		}
		this.percentChance = chance;
	}

	@Override
	public boolean test(Context ctx) {
		double roll = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
		return roll <= percentChance;
	}

	public static PercentChanceCondition fromJson(JsonObject obj) {
		if (!obj.has("chance")) {
			throw new IllegalArgumentException("Condition 'percent_chance' is missing required field 'chance'");
		}
		double percentChance = obj.get("chance").getAsDouble();
		return new PercentChanceCondition(percentChance);
	}
}
