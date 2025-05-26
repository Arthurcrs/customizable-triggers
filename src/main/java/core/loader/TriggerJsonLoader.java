package core.loader;

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

import core.action.ActionRegistry;
import core.action.IAction;
import core.condition.ConditionRegistry;
import core.condition.ICondition;
import core.trigger.ITrigger;
import core.trigger.TriggerRegistry;
import net.minecraftforge.fml.common.FMLLog;

public final class TriggerJsonLoader {

	public static List<ITrigger> loadAll(File configDir) {
		ensureFileExists(configDir);

		File file = new File(configDir, "triggers.json");
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

				String id = jsonObj.get("trigger").getAsString();
				triggers.add(TriggerRegistry.create(id, conditions, actions));
			}

			return triggers;

		} catch (JsonSyntaxException e) {
			FMLLog.log.error("Malformed JSON in triggers.json", e);
		} catch (Exception e) {
			FMLLog.log.error("Failed to load triggers.json", e);
		}

		return Collections.emptyList();
	}

	private static void ensureFileExists(File file) {
		if (file.exists())
			return;

		try {
			File folder = file.getParentFile();
			if (!folder.exists())
				folder.mkdirs();

			try (FileWriter writer = new FileWriter(file)) {
				writer.write("[]");
			}

			FMLLog.log.info("[TCA] triggers.json not found — created empty config.");
		} catch (IOException e) {
			FMLLog.log.error("[TCA] Failed to create empty triggers.json", e);
		}
	}

	private TriggerJsonLoader() {
	}
}