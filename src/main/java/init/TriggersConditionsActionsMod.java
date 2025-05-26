package init;

import java.io.File;
import java.util.List;

import core.action.ActionRegistry;
import core.condition.ConditionRegistry;
import core.loader.TriggerJsonLoader;
import core.trigger.ITrigger;
import core.trigger.TriggerRegistry;
import net.minecraftforge.fml.common.FMLLog;
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
		ConditionRegistry.init();
		ActionRegistry.init();
		TriggerRegistry.init();

		try {
			File configFile = new File(e.getModConfigurationDirectory(), "tca/");
			List<ITrigger> triggers = TriggerJsonLoader.loadAll(configFile);
			triggers.forEach(ITrigger::register);
		} catch (Exception ex) {
			FMLLog.log.error("[TCA] Failed to load triggers.json", ex);
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {

	}
}