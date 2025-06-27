package com.mahghuuuls.configurabletriggers.core.condition.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Condition that checks if an item from the context matches a specific
 * Minecraft item ID.
 */
@RegisterCondition("item_matches_id")
public final class ItemMatchesIdCondition implements ICondition {
	private final ResourceLocation targetId;
	private final CtxKey<?> itemKey;

	private static final String FIELD_ITEM = "item";
	private static final String FIELD_ID = "id";

	private ItemMatchesIdCondition(ResourceLocation id, CtxKey<?> key) {
		this.targetId = id;
		this.itemKey = key;
	}

	@Override
	public boolean test(Context ctx) {
		ItemStack stack = ctx.get(itemKey, ItemStack.class);
		if (stack == null) {
			return false;
		}
		Item item = stack.getItem();
		ResourceLocation id = Item.REGISTRY.getNameForObject(item);
		return id != null && id.equals(targetId);
	}

	/**
	 * Creates this condition from its JSON representation. Required fields are
	 * "item" and "id".
	 */
	public static ItemMatchesIdCondition fromJson(JsonObject jsonObj) {
		if (!jsonObj.has(FIELD_ITEM)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_ITEM);
		}
		if (!jsonObj.has(FIELD_ID)) {
			throw new IllegalArgumentException("Condition is missing required field: " + FIELD_ID);
		}

		String itemName = jsonObj.get(FIELD_ITEM).getAsString();
		String id = jsonObj.get(FIELD_ID).getAsString();

		CtxKey<?> key = CtxKeys.getKeyFromId(itemName);
		ResourceLocation rl = new ResourceLocation(id);

		return new ItemMatchesIdCondition(rl, key);
	}
}