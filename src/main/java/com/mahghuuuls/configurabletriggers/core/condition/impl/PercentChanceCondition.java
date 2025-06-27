package com.mahghuuuls.configurabletriggers.core.condition.impl;

import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;

@RegisterCondition("percent_chance")
public final class PercentChanceCondition implements ICondition {
	private final double percentChance;

	private final static String FIELD_CHANCE = "chance";

	public PercentChanceCondition(double chance) {
		if (chance < 0 || chance > 100) {
			throw new IllegalArgumentException(FIELD_CHANCE + " must be between 0 and 100");
		}
		this.percentChance = chance;
	}

	@Override
	public boolean test(Context ctx) {
		double roll = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
		return roll <= percentChance;
	}

	public static PercentChanceCondition fromJson(JsonObject jsonObj) {
		if (!jsonObj.has(FIELD_CHANCE)) {
			throw new IllegalArgumentException("Condition is missing required field " + FIELD_CHANCE);
		}
		double percentChance = jsonObj.get(FIELD_CHANCE).getAsDouble();
		return new PercentChanceCondition(percentChance);
	}
}
