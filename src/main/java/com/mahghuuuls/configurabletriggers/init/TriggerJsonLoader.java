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
import com.mahghuuuls.configurabletriggers.core.action.IAction;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.registry.ActionRegistry;
import com.mahghuuuls.configurabletriggers.core.registry.ConditionRegistry;
import com.mahghuuuls.configurabletriggers.core.registry.TriggerRegistry;
import com.mahghuuuls.configurabletriggers.core.trigger.ITrigger;
import com.mahghuuuls.configurabletriggers.util.CTLogger;

/**
 * A utility class responsible for loading and parsing trigger definitions from
 * a JSON file, specifically "triggers.json". This class handles file existence,
 * JSON parsing, and the creation of {@link ITrigger} instances using the
 * registered {@link ConditionRegistry}, {@link ActionRegistry}, and
 * {@link TriggerRegistry}.
 */
public final class TriggerJsonLoader {

	/**
	 * Loads all trigger definitions from the specified JSON file. The file is
	 * expected to contain a JSON array of trigger objects. Each trigger object must
	 * have an "event" field and can optionally have "name", "conditions" (an
	 * array), and "actions" (an array) fields.
	 *
	 * <p>
	 * If the "conditions" or "actions" arrays are missing or not valid JSON arrays,
	 * a warning will be logged, and empty lists will be used for that trigger,
	 * allowing the mod to continue loading other triggers.
	 * </p>
	 *
	 * @param triggersJsonFile The {@link File} object pointing to the triggers.json
	 *                         file. This file is expected to be in the mod's
	 *                         configuration directory.
	 * @return A {@link List} of {@link ITrigger} instances loaded from the file.
	 *         Returns an empty list if the file is empty, malformed, or any
	 *         critical error occurs during the overall loading process that
	 *         prevents any triggers from being parsed.
	 */
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
			CTLogger.error("Malformed JSON in triggers.json", e);
		} catch (Exception e) {
			CTLogger.error("Failed to load triggers.json", e);
		}

		return Collections.emptyList();
	}

	/**
	 * Ensures that the specified file exists. If the file's parent directories do
	 * not exist, they are created. If the file itself does not exist, an empty JSON
	 * array `[]` is written to it, serving as a default valid configuration.
	 *
	 * @param file The {@link File} object for which existence is to be ensured.
	 * @return The ensured {@link File} object (which may have just been created).
	 */
	private static File ensureFileExists(File file) {
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}

		if (!file.exists()) {
			try (FileWriter writer = new FileWriter(file)) {
				writer.write("[]");
				CTLogger.info("Triggers.json not found — created empty config at {}", file);
			} catch (IOException e) {
				CTLogger.error("Failed to create empty triggers.json", e);
			}
		}

		return file;
	}

	/**
	 * Private constructor to prevent instantiation of this utility class. All
	 * methods are static.
	 */
	private TriggerJsonLoader() {
	}
}