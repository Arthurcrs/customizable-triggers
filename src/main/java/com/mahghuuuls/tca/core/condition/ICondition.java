package com.mahghuuuls.tca.core.condition;

import com.mahghuuuls.tca.core.context.Context;

public interface ICondition {
	boolean test(Context ctx);
}