package org.json;

/**
 * Excepción ligera para errores de parseo o acceso a estructuras JSON.
 */
public class JSONException extends RuntimeException {
    public JSONException(String message) {
        super(message);
    }

    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }
}
