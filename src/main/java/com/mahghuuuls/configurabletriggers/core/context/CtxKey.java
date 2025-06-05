package com.mahghuuuls.configurabletriggers.core.context;

public final class CtxKey<T> {
	private final String id;

	public CtxKey(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof CtxKey && id.equals(((CtxKey<?>) o).id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}