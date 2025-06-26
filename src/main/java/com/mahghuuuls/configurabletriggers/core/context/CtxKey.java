package com.mahghuuuls.configurabletriggers.core.context;

public final class CtxKey<T> {
	private final String id;
	private static final java.util.Map<String, CtxKey<?>> REGISTRY = new java.util.HashMap<>();

	public CtxKey(String id) {
		this.id = id;
		REGISTRY.put(id, this);
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return id;
	}

	/**
	 * Looks up a registered {@link CtxKey} by its id.
	 *
	 * @param id the context id as used in JSON
	 * @return the matching key
	 * @throws IllegalArgumentException if the id is unknown
	 */
	public static CtxKey<?> forId(String id) {
		CtxKey<?> key = REGISTRY.get(id);
		if (key == null) {
			throw new IllegalArgumentException("Unknown context id: " + id);
		}
		return key;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof CtxKey && id.equals(((CtxKey<?>) obj).id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}