package init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TriggersConditionsActionsMod.MODID, name = TriggersConditionsActionsMod.NAME, version = TriggersConditionsActionsMod.VERSION, acceptedMinecraftVersions = "[1.12.2]")
public class TriggersConditionsActionsMod {
	public static final String MODID = "tca";
	public static final String NAME = "Trigger Conditions and Actions";
	public static final String VERSION = "1.0.0";

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {

	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {

	}
}