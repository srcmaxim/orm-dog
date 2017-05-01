package org.dog.util;

import java.util.HashMap;
import java.util.Map;

public final class Primitives {

    private final static Map<Class<?>, Class<?>> map = new HashMap<>();

    static {
        map.put(Boolean.class, boolean.class);
        map.put(Byte.class, byte.class);
        map.put(Short.class, short.class);
        map.put(Character.class, char.class);
        map.put(Integer.class, int.class);
        map.put(Long.class, long.class);
        map.put(Float.class, float.class);
        map.put(Double.class, double.class);
    }

    private Primitives() {
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrap(final Class<T> wrapper) {
        Class<T> primitive = (Class<T>) map.get(wrapper);
        if (primitive == null) {
            return wrapper;
        }
        return primitive;
    }

}
