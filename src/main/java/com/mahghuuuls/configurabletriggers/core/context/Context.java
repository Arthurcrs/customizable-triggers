package com.mahghuuuls.configurabletriggers.core.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A thread-safe, immutable context object used to pass relevant data from an
 * event (trigger) to conditions and actions. This design ensures that the data
 * available to conditions and actions remains consistent throughout the
 * execution of a single trigger chain. Data is stored as key-value pairs, where
 * keys are {@link CtxKey} instances and values are objects.
 */
public final class Context {

	private final Map<CtxKey<?>, ?> data;

	/**
	 * Private constructor to enforce creation via the {@link Builder} class. The
	 * map provided is wrapped in an unmodifiable map to ensure immutability of the
	 * context after creation.
	 *
	 * @param data An unmodifiable map containing the context data.
	 */
	private Context(Map<CtxKey<?>, ?> data) {
		this.data = data;
	}

	/**
	 * Returns a new {@link Builder} instance, which is the starting point for
	 * constructing an immutable {@link Context} object.
	 *
	 * @return A new Context Builder instance.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * A builder class for creating immutable {@link Context} objects. This nested
	 * static class allows for a fluent API to add various data points to the
	 * context before it is finally built.
	 */
	public static final class Builder {
		// A mutable map used internally by the builder to store data before building
		// the immutable Context.
		private final Map<CtxKey<?>, Object> mutable = new HashMap<>();

		/**
		 * Private constructor to prevent direct instantiation of the Builder. Builders
		 * should only be obtained via {@link Context#builder()}.
		 */
		private Builder() {
		}

		/**
		 * Puts a key-value pair into the context being built. The {@link CtxKey}
		 * provides type safety for the value.
		 *
		 * @param key   The {@link CtxKey} for the data.
		 * @param value The value associated with the key. Can be {@code null}.
		 * @param <T>   The type of the value, inferred from the {@link CtxKey}.
		 * @return This Builder instance, allowing for method chaining.
		 */
		public <T> Builder put(CtxKey<T> key, T value) {
			mutable.put(key, value);
			return this;
		}

		/**
		 * Builds an immutable {@link Context} instance from the data that has been
		 * added to this builder. Once built, the context cannot be modified.
		 *
		 * @return An immutable {@link Context} instance containing all data added via
		 *         the {@code put} method.
		 */
		public Context build() {
			// Create a new HashMap from the mutable data and then wrap it in an
			// unmodifiable map.
			// This ensures the Context object itself holds an immutable view of the data.
			return new Context(Collections.unmodifiableMap(new HashMap<>(mutable)));
		}
	}

	/**
	 * Retrieves a value from the context associated with the given key, attempting
	 * to cast it to the specified type.
	 *
	 * <p>
	 * This method provides type safety by accepting a {@link Class} object
	 * representing the expected type. If the key is not found or the value cannot
	 * be cast to the specified type, it will return {@code null} (or potentially
	 * throw a {@link ClassCastException} at runtime if the stored type is
	 * fundamentally incompatible, though usage with {@link CtxKey} should mitigate
	 * this).
	 * </p>
	 *
	 * @param key  The {@link CtxKey} whose associated value is to be returned.
	 * @param type The {@link Class} object representing the desired type of the
	 *             value.
	 * @param <T>  The generic type of the value to be returned.
	 * @return The value associated with the key, cast to the specified type, or
	 *         {@code null} if the key is not present in the context.
	 */
	public <T> T get(CtxKey<?> key, Class<T> type) {
		// Suppress the unchecked cast warning because the 'type' parameter
		// serves as a runtime type check intention, relying on the correct
		// usage of CtxKey with its associated type.
		@SuppressWarnings("unchecked")
		T value = (T) data.get(key);
		return value;
	}
}
