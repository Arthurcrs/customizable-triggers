package config;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import potioneffects.CustomPotionEffect;
import potioneffects.CustomPotionEffectConfig;
import potioneffects.CustomPotionEffectConfig.EffectEntry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class PEffectsRecorder {

	private static final List<Potion> POTIONS_TO_REGISTER = new ArrayList<>();

	public static void loadFromConfig(File configDir) {
		CustomPotionEffectConfig config = PEffectsConfigLoader.load(configDir);
		for (CustomPotionEffectConfig.EffectEntry entry : config.effects) {
			CustomPotionEffect potion = new CustomPotionEffect(entry.name);

			if (entry.triggers != null && entry.triggers.onDamageDealt != null) {
				potion.setOnDamageDealt(entry.triggers.onDamageDealt);
			}

			POTIONS_TO_REGISTER.add(potion);
		}
	}

	@SubscribeEvent
	public static void registerCustomPotionEffects(RegistryEvent.Register<Potion> event) {
		event.getRegistry().registerAll(POTIONS_TO_REGISTER.toArray(new Potion[0]));
	}
}
