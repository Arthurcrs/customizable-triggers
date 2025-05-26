package core.condition;

import core.context.Context;

public interface ICondition {
	boolean test(Context ctx);
}