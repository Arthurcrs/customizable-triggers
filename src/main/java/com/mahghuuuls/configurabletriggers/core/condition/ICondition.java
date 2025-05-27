package com.mahghuuuls.configurabletriggers.core.condition;

import com.mahghuuuls.configurabletriggers.core.context.Context;

public interface ICondition {
	boolean test(Context ctx);
}