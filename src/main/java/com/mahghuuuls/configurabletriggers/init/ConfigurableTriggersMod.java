package com.mahghuuuls.configurabletriggers.init;

import java.io.File;
import java.util.List;

import com.mahghuuuls.configurabletriggers.core.action.ActionRegistry;
import com.mahghuuuls.configurabletriggers.core.condition.ConditionRegistry;
import com.mahghuuuls.configurabletriggers.core.trigger.ITrigger;
import com.mahghuuuls.configurabletriggers.core.trigger.TriggerRegistry;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ConfigurableTriggersMod.MODID, name = ConfigurableTriggersMod.NAME, version = ConfigurableTriggersMod.VERSION, acceptedMinecraftVersions = "[1.12.2]")
public class ConfigurableTriggersMod {
	public static final String MODID = "configurable-triggers";
	public static final String NAME = "Configurable Triggers";
	public static final String VERSION = "1.0.0";

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		ConditionRegistry.init();
		ActionRegistry.init();
		TriggerRegistry.init();

		try {
			File triggersJsonFile = new File(e.getModConfigurationDirectory(), "Configurable Triggers/triggers.json");
			List<ITrigger> triggers = TriggerJsonLoader.loadAll(triggersJsonFile);
			triggers.forEach(ITrigger::register);
		} catch (Exception ex) {
			FMLLog.log.error("[Configurable Triggers] Failed to load triggers.json", ex);
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {

	}
}