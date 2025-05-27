package com.mahghuuuls.configurabletriggers.core.trigger;

import com.mahghuuuls.configurabletriggers.core.context.Context;

import net.minecraftforge.fml.common.eventhandler.Event;

public class TriggerFireEvent extends Event {
	private final String triggerId;
	private final Context context;

	public TriggerFireEvent(String triggerId, Context context) {
		this.triggerId = triggerId;
		this.context = context;
	}

	public String getTriggerId() {
		return triggerId;
	}

	public Context getContext() {
		return context;
	}
}
