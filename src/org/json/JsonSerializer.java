package org.json;

import java.util.Collection;
import java.util.Map;

class JsonSerializer {
    static String toJson(Object value, int indentFactor, int indent) {
        StringBuilder sb = new StringBuilder();
        write(value, indentFactor, indent, sb);
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private static void write(Object value, int indentFactor, int indent, StringBuilder sb) {
        if (value == null || value == JSONObject.NULL) {
            sb.append("null");
        } else if (value instanceof JSONObject) {
            write(((JSONObject) value).map, indentFactor, indent, sb);
        } else if (value instanceof JSONArray) {
            write(((JSONArray) value).list, indentFactor, indent, sb);
        } else if (value instanceof Map) {
            writeObject((Map<String, Object>) value, indentFactor, indent, sb);
        } else if (value instanceof Collection) {
            writeArray((Collection<?>) value, indentFactor, indent, sb);
        } else if (value instanceof String) {
            writeString((String) value, sb);
        } else if (value instanceof Number || value instanceof Boolean) {
            sb.append(String.valueOf(value));
        } else {
            writeString(value.toString(), sb);
        }
    }

    private static void writeObject(Map<String, Object> map, int indentFactor, int indent, StringBuilder sb) {
        sb.append('{');
        if (!map.isEmpty()) {
            boolean first = true;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                newline(indentFactor, indent + indentFactor, sb);
                writeString(entry.getKey(), sb);
                sb.append(':');
                if (indentFactor > 0) sb.append(' ');
                write(entry.getValue(), indentFactor, indent + indentFactor, sb);
            }
            newline(indentFactor, indent, sb);
        }
        sb.append('}');
    }

    private static void writeArray(Collection<?> list, int indentFactor, int indent, StringBuilder sb) {
        sb.append('[');
        if (!list.isEmpty()) {
            boolean first = true;
            for (Object val : list) {
                if (!first) sb.append(',');
                first = false;
                newline(indentFactor, indent + indentFactor, sb);
                write(val, indentFactor, indent + indentFactor, sb);
            }
            newline(indentFactor, indent, sb);
        }
        sb.append(']');
    }

    private static void writeString(String s, StringBuilder sb) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
    }

    private static void newline(int indentFactor, int indent, StringBuilder sb) {
        if (indentFactor <= 0) return;
        sb.append('\n');
        for (int i = 0; i < indent; i++) {
            sb.append(' ');
        }
    }
}
