package org.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementación sencilla de un arreglo JSON.
 */
public class JSONArray {
    final List<Object> list;

    public JSONArray() {
        this.list = new ArrayList<>();
    }

    public JSONArray(String source) {
        Object parsed = SimpleJsonParser.parse(source);
        if (!(parsed instanceof List)) {
            throw new JSONException("La cadena no representa un arreglo JSON");
        }
        //noinspection unchecked
        this.list = (List<Object>) parsed;
    }

    public JSONArray(Collection<?> collection) {
        this();
        if (collection != null) {
            for (Object o : collection) {
                list.add(JSONObject.wrap(o));
            }
        }
    }

    public int length() {
        return list.size();
    }

    public Object get(int index) {
        try {
            return list.get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new JSONException("Índice fuera de rango en JSONArray: " + index);
        }
    }

    public JSONObject getJSONObject(int index) {
        Object value = get(index);
        if (value instanceof JSONObject) return (JSONObject) value;
        if (value instanceof java.util.Map) return new JSONObject((java.util.Map<?, ?>) value);
        throw new JSONException("El elemento no es un objeto JSON en índice " + index);
    }

    public JSONArray getJSONArray(int index) {
        Object value = get(index);
        if (value instanceof JSONArray) return (JSONArray) value;
        if (value instanceof Collection) return new JSONArray((Collection<?>) value);
        throw new JSONException("El elemento no es un arreglo JSON en índice " + index);
    }

    public String getString(int index) {
        Object value = get(index);
        if (value == JSONObject.NULL) return null;
        return String.valueOf(value);
    }

    public int getInt(int index) {
        Object value = get(index);
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException ex) {
            throw new JSONException("No es un entero en posición " + index);
        }
    }

    public double getDouble(int index) {
        Object value = get(index);
        if (value instanceof Number) return ((Number) value).doubleValue();
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException ex) {
            throw new JSONException("No es un número en posición " + index);
        }
    }

    public boolean getBoolean(int index) {
        Object value = get(index);
        if (value instanceof Boolean) return (Boolean) value;
        return Boolean.parseBoolean(String.valueOf(value));
    }

    public JSONArray optJSONArray(int index) {
        if (index < 0 || index >= list.size()) return null;
        Object value = list.get(index);
        if (value instanceof JSONArray) return (JSONArray) value;
        if (value instanceof Collection) return new JSONArray((Collection<?>) value);
        return null;
    }

    public JSONObject optJSONObject(int index) {
        if (index < 0 || index >= list.size()) return null;
        Object value = list.get(index);
        if (value instanceof JSONObject) return (JSONObject) value;
        if (value instanceof java.util.Map) return new JSONObject((java.util.Map<?, ?>) value);
        return null;
    }

    public int optInt(int index, int defaultValue) {
        if (index < 0 || index >= list.size()) return defaultValue;
        Object value = list.get(index);
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public String optString(int index) {
        return optString(index, "");
    }

    public String optString(int index, String defaultValue) {
        if (index < 0 || index >= list.size()) return defaultValue;
        Object value = list.get(index);
        if (value == null || value == JSONObject.NULL) return defaultValue;
        return String.valueOf(value);
    }

    public JSONArray put(Object value) {
        list.add(JSONObject.wrap(value));
        return this;
    }

    public String toString() {
        return toString(0);
    }

    public String toString(int indentFactor) {
        return JsonSerializer.toJson(list, indentFactor, 0);
    }
}
