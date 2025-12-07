package org.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser JSON muy ligero usado por {@link JSONObject} y {@link JSONArray}.
 */
class SimpleJsonParser {
    private final String text;
    private int pos;

    private SimpleJsonParser(String text) {
        this.text = text;
        this.pos = 0;
    }

    static Object parse(String text) {
        if (text == null) {
            throw new JSONException("Cadena nula pasada al parser JSON");
        }
        SimpleJsonParser parser = new SimpleJsonParser(text);
        parser.skipWhitespace();
        Object value = parser.parseValue();
        parser.skipWhitespace();
        if (parser.pos != parser.text.length()) {
            throw new JSONException("Contenido extra después del JSON");
        }
        return value;
    }

    private Object parseValue() {
        skipWhitespace();
        if (pos >= text.length()) {
            throw new JSONException("JSON incompleto");
        }
        char c = text.charAt(pos);
        switch (c) {
            case '{':
                return parseObject();
            case '[':
                return parseArray();
            case '"':
                return parseString();
            case 't':
            case 'f':
                return parseBoolean();
            case 'n':
                return parseNull();
            default:
                if (c == '-' || (c >= '0' && c <= '9')) {
                    return parseNumber();
                }
                throw new JSONException("Carácter inesperado en JSON: " + c);
        }
    }

    private Map<String, Object> parseObject() {
        expect('{');
        skipWhitespace();
        Map<String, Object> map = new LinkedHashMap<>();
        if (peek('}')) {
            pos++;
            return map;
        }
        while (true) {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            expect(':');
            skipWhitespace();
            Object value = parseValue();
            map.put(key, value);
            skipWhitespace();
            if (peek('}')) {
                pos++;
                break;
            }
            expect(',');
        }
        return map;
    }

    private List<Object> parseArray() {
        expect('[');
        skipWhitespace();
        List<Object> list = new ArrayList<>();
        if (peek(']')) {
            pos++;
            return list;
        }
        while (true) {
            skipWhitespace();
            list.add(parseValue());
            skipWhitespace();
            if (peek(']')) {
                pos++;
                break;
            }
            expect(',');
        }
        return list;
    }

    private String parseString() {
        expect('"');
        StringBuilder sb = new StringBuilder();
        while (pos < text.length()) {
            char c = text.charAt(pos++);
            if (c == '"') {
                return sb.toString();
            }
            if (c == '\\') {
                if (pos >= text.length()) {
                    throw new JSONException("Secuencia de escape incompleta en cadena JSON");
                }
                char esc = text.charAt(pos++);
                switch (esc) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/': sb.append('/'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case 'u':
                        if (pos + 4 > text.length()) {
                            throw new JSONException("Secuencia Unicode incompleta en cadena JSON");
                        }
                        String hex = text.substring(pos, pos + 4);
                        try {
                            sb.append((char) Integer.parseInt(hex, 16));
                        } catch (NumberFormatException ex) {
                            throw new JSONException("Código Unicode inválido: " + hex);
                        }
                        pos += 4;
                        break;
                    default:
                        throw new JSONException("Escape inválido: " + esc);
                }
            } else {
                sb.append(c);
            }
        }
        throw new JSONException("Cadena JSON sin cierre de comillas");
    }

    private Number parseNumber() {
        int start = pos;
        if (peek('-')) pos++;
        while (pos < text.length() && Character.isDigit(text.charAt(pos))) pos++;
        if (peek('.')) {
            pos++;
            while (pos < text.length() && Character.isDigit(text.charAt(pos))) pos++;
        }
        if (pos < text.length() && (text.charAt(pos) == 'e' || text.charAt(pos) == 'E')) {
            pos++;
            if (peek('+') || peek('-')) pos++;
            while (pos < text.length() && Character.isDigit(text.charAt(pos))) pos++;
        }
        String num = text.substring(start, pos);
        try {
            if (num.contains(".") || num.contains("e") || num.contains("E")) {
                return Double.parseDouble(num);
            }
            long l = Long.parseLong(num);
            if (l <= Integer.MAX_VALUE && l >= Integer.MIN_VALUE) {
                return (int) l;
            }
            return l;
        } catch (NumberFormatException ex) {
            throw new JSONException("Número inválido en JSON: " + num, ex);
        }
    }

    private Boolean parseBoolean() {
        if (text.startsWith("true", pos)) {
            pos += 4;
            return Boolean.TRUE;
        } else if (text.startsWith("false", pos)) {
            pos += 5;
            return Boolean.FALSE;
        }
        throw new JSONException("Valor booleano inválido en JSON");
    }

    private Object parseNull() {
        if (text.startsWith("null", pos)) {
            pos += 4;
            return JSONObject.NULL;
        }
        throw new JSONException("Valor null inválido en JSON");
    }

    private void expect(char expected) {
        if (pos >= text.length() || text.charAt(pos) != expected) {
            throw new JSONException("Se esperaba '" + expected + "' en posición " + pos);
        }
        pos++;
    }

    private boolean peek(char c) {
        return pos < text.length() && text.charAt(pos) == c;
    }

    private void skipWhitespace() {
        while (pos < text.length()) {
            char c = text.charAt(pos);
            if (c == ' ' || c == '\n' || c == '\r' || c == '\t') {
                pos++;
            } else {
                break;
            }
        }
    }
}
