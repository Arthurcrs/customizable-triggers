package com.mahghuuuls.configurabletriggers.init;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mahghuuuls.configurabletriggers.core.action.ActionRegistry;
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.condition.ConditionRegistry;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.trigger.ITrigger;
import com.mahghuuuls.configurabletriggers.core.trigger.TriggerRegistry;

import net.minecraftforge.fml.common.FMLLog;

public final class TriggerJsonLoader {

	public static List<ITrigger> loadAll(File triggersJsonFile) {

		File file = ensureFileExists(triggersJsonFile);
		Gson gson = new Gson();

		try (FileReader reader = new FileReader(file)) {
			JsonArray root = gson.fromJson(reader, JsonArray.class);
			List<ITrigger> triggers = new ArrayList<>();

			for (JsonElement element : root) {
				JsonObject jsonObj = element.getAsJsonObject();

				List<ICondition> conditions = new ArrayList<>();
				jsonObj.getAsJsonArray("conditions")
						.forEach(e -> conditions.add(ConditionRegistry.fromJson(e.getAsJsonObject())));

				List<IAction> actions = new ArrayList<>();
				jsonObj.getAsJsonArray("actions")
						.forEach(e -> actions.add(ActionRegistry.fromJson(e.getAsJsonObject())));

				String event = jsonObj.get("event").getAsString();
				String name = jsonObj.has("name") ? jsonObj.get("name").getAsString() : event;

				triggers.add(TriggerRegistry.create(name, event, conditions, actions));

			}

			return triggers;

		} catch (JsonSyntaxException e) {
			FMLLog.log.error("[Configurable Triggers] Malformed JSON in triggers.json", e);
		} catch (Exception e) {
			FMLLog.log.error("[Configurable Triggers] Failed to load triggers.json", e);
		}

		return Collections.emptyList();
	}

	private static File ensureFileExists(File file) {
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}

		if (!file.exists()) {
			try (FileWriter writer = new FileWriter(file)) {
				writer.write("[]");
				FMLLog.log.info("[Configurable Triggers] triggers.json not found — created empty config at {}", file);
			} catch (IOException e) {
				FMLLog.log.error("[Configurable Triggers] Failed to create empty triggers.json", e);
			}
		}

		return file;
	}

	private TriggerJsonLoader() {
	}
}