package org.json;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementación sencilla de un objeto JSON compatible con el uso del proyecto.
 */
public class JSONObject {
    public static final Object NULL = new Null();

    final Map<String, Object> map;

    public JSONObject() {
        this.map = new LinkedHashMap<>();
    }

    public JSONObject(String source) {
        Object parsed = SimpleJsonParser.parse(source);
        if (!(parsed instanceof Map)) {
            throw new JSONException("La cadena no representa un objeto JSON");
        }
        //noinspection unchecked
        this.map = (Map<String, Object>) parsed;
    }

    public JSONObject(Map<?, ?> m) {
        this();
        if (m != null) {
            for (Map.Entry<?, ?> entry : m.entrySet()) {
                if (entry.getKey() != null) {
                    map.put(String.valueOf(entry.getKey()), wrap(entry.getValue()));
                }
            }
        }
    }

    public boolean has(String key) {
        return map.containsKey(key);
    }

    public boolean isNull(String key) {
        return map.containsKey(key) && map.get(key) == NULL;
    }

    public Object get(String key) {
        if (!map.containsKey(key)) {
            throw new JSONException("Llave no encontrada: " + key);
        }
        return map.get(key);
    }

    public String getString(String key) {
        Object value = get(key);
        if (value == NULL) return null;
        return String.valueOf(value);
    }

    public String optString(String key) {
        return optString(key, "");
    }

    public String optString(String key, String defaultValue) {
        Object value = map.get(key);
        if (value == null || value == NULL) return defaultValue;
        return String.valueOf(value);
    }

    public double getDouble(String key) {
        Object value = get(key);
        if (value instanceof Number) return ((Number) value).doubleValue();
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException ex) {
            throw new JSONException("No es un número: " + key);
        }
    }

    public double optDouble(String key, double defaultValue) {
        Object value = map.get(key);
        if (value == null || value == NULL) return defaultValue;
        if (value instanceof Number) return ((Number) value).doubleValue();
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public int getInt(String key) {
        Object value = get(key);
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException ex) {
            throw new JSONException("No es un entero: " + key);
        }
    }

    public int optInt(String key, int defaultValue) {
        Object value = map.get(key);
        if (value == null || value == NULL) return defaultValue;
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key) {
        Object value = get(key);
        if (value instanceof Boolean) return (Boolean) value;
        return Boolean.parseBoolean(String.valueOf(value));
    }

    public boolean optBoolean(String key, boolean defaultValue) {
        Object value = map.get(key);
        if (value == null || value == NULL) return defaultValue;
        if (value instanceof Boolean) return (Boolean) value;
        return Boolean.parseBoolean(String.valueOf(value));
    }

    public JSONObject getJSONObject(String key) {
        Object value = get(key);
        if (value instanceof JSONObject) return (JSONObject) value;
        if (value instanceof Map) return new JSONObject((Map<?, ?>) value);
        throw new JSONException("No es un objeto JSON: " + key);
    }

    public JSONObject optJSONObject(String key) {
        Object value = map.get(key);
        if (value == null || value == NULL) return null;
        if (value instanceof JSONObject) return (JSONObject) value;
        if (value instanceof Map) return new JSONObject((Map<?, ?>) value);
        return null;
    }

    public JSONArray getJSONArray(String key) {
        Object value = get(key);
        if (value instanceof JSONArray) return (JSONArray) value;
        if (value instanceof Collection) return new JSONArray((Collection<?>) value);
        throw new JSONException("No es un arreglo JSON: " + key);
    }

    public JSONArray optJSONArray(String key) {
        Object value = map.get(key);
        if (value == null || value == NULL) return null;
        if (value instanceof JSONArray) return (JSONArray) value;
        if (value instanceof Collection) return new JSONArray((Collection<?>) value);
        return null;
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public JSONObject put(String key, Object value) {
        if (key == null) {
            throw new NullPointerException("La llave no puede ser nula");
        }
        map.put(key, wrap(value));
        return this;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public String toString() {
        return toString(0);
    }

    public String toString(int indentFactor) {
        return JsonSerializer.toJson(map, indentFactor, 0);
    }

    static Object wrap(Object value) {
        if (value == null) return NULL;
        if (value instanceof Map) return new JSONObject((Map<?, ?>) value);
        if (value instanceof Collection) return new JSONArray((Collection<?>) value);
        return value;
    }

    private static final class Null {
        @Override
        public boolean equals(Object obj) {
            return obj == null || obj == this;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "null";
        }
    }
}
