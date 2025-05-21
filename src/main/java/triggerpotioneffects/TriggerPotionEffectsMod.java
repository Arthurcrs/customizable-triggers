package triggerpotioneffects;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = TriggerPotionEffectsMod.MODID,
     name = TriggerPotionEffectsMod.NAME,
     version = TriggerPotionEffectsMod.VERSION,
     acceptedMinecraftVersions = "[1.12.2]")
public class TriggerPotionEffectsMod {
    public static final String MODID  = "triggerpotioneffects";
    public static final String NAME   = "Trigger Potion Effects";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	ModPotions.loadFromConfig(e.getModConfigurationDirectory());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        
    }
}