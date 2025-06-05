package com.mahghuuuls.configurabletriggers.core.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Context {

	private final Map<CtxKey<?>, ?> data;

	private Context(Map<CtxKey<?>, ?> data) {
		this.data = data;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private final Map<CtxKey<?>, Object> mutable = new HashMap<>();

		public <T> Builder put(CtxKey<T> key, T value) {
			mutable.put(key, value);
			return this;
		}

		public Context build() {
			return new Context(Collections.unmodifiableMap(new HashMap<>(mutable)));
		}
	}

	public <T> T get(CtxKey<?> key, Class<T> type) {
		@SuppressWarnings("unchecked")
		T value = (T) data.get(key);
		return value;
	}
}