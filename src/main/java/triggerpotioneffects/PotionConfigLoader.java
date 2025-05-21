package triggerpotioneffects;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.minecraftforge.fml.common.FMLLog;

import java.io.File;
import java.io.FileReader;
import java.util.Collections;

public class PotionConfigLoader {

    public static CustomPotionEffectConfig load(File configDir) {
        File file = new File(configDir, "trigger_potion_effects.json");
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, CustomPotionEffectConfig.class);
        } catch (JsonSyntaxException e) {
            FMLLog.log.error("Malformed JSON in trigger_potion_effects.json", e);
        } catch (Exception e) {
            FMLLog.log.error("Failed to load trigger_potion_effects.json", e);
        }

        CustomPotionEffectConfig fallback = new CustomPotionEffectConfig();
        fallback.effects = Collections.emptyList();
        return fallback;
    }
}

