package com.mahghuuuls.configurabletriggers.core.trigger;

/**
 * Represents a configurable trigger that can be registered to listen for
 * Minecraft events. An {@code ITrigger} defines when and how its associated
 * conditions and actions are processed. Implementations of this interface are
 * responsible for subscribing to and unsubscribing from the appropriate
 * MinecraftForge event bus.
 */
public interface ITrigger {

	/**
	 * Registers this trigger instance with the relevant MinecraftForge event bus.
	 * This method should be called when the mod initializes and triggers are loaded
	 * to ensure the trigger starts listening for events.
	 */
	void register();

	/**
	 * Unregisters this trigger instance from the relevant MinecraftForge event bus.
	 * This method can be used to disable or remove triggers dynamically during
	 * runtime, preventing them from responding to further events.
	 */
	void unregister();
}