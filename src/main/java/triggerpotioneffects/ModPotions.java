package triggerpotioneffects;

import triggerpotioneffects.CustomPotionEffectConfig;
import triggerpotioneffects.PotionConfigLoader;
import triggerpotioneffects.ConfigurablePotion;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class ModPotions {

    private static final List<Potion> POTIONS_TO_REGISTER = new ArrayList<>();

    public static void loadFromConfig(File configDir) {
        CustomPotionEffectConfig config = PotionConfigLoader.load(configDir);
        for (CustomPotionEffectConfig.EffectEntry entry : config.effects) {
            POTIONS_TO_REGISTER.add(new ConfigurablePotion(entry.name));
        }
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(POTIONS_TO_REGISTER.toArray(new Potion[0]));
    }
}
