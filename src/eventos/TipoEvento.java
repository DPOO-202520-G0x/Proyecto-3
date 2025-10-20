package eventos;


/**
 * Enumera los tipos de evento admitidos por la plataforma BoletaMaster.
 * <p>
 * Reglas de dominio relacionadas:
 * <ul>
 *   <li>El administrador puede fijar cargos de servicio (porcentaje) diferenciados por tipo de evento.</li>
 *   <li>El tipo de evento clasifica la configuración y el cálculo de precios/cargos en la compra de tiquetes.</li>
 * </ul>
 */
public enum TipoEvento {
    CONCIERTO,
    CULTURAL,
    RELIGIOSO,
    DEPORTIVO
}