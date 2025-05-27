package com.mahghuuuls.configurabletriggers.core.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Context {

    private final Map<String, ?> data;

    private Context(Map<String, ?> data) {
        this.data = data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<String, Object> mutable = new HashMap<>();

        public Builder put(String key, Object value) {
            mutable.put(key, value);
            return this;
        }

        public Context build() {
            return new Context(Collections.unmodifiableMap(new HashMap<>(mutable)));
        }
    }
    
    public <T> T get(String key, Class<T> type) {
        return type.cast(data.get(key));
    }
}