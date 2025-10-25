package udistrital.avanzada.taller.control;


/**
 * Clase que representa el controlador principal de la lógica del programa
 * <b>ConjurosConHilos</b>.
 * <p>
 * Esta clase actúa como intermediaria entre la interfaz gráfica y la capa de
 * lógica del dominio. Su responsabilidad es coordinar el flujo general del
 * sistema, procesar las acciones del usuario recibidas desde la interfaz
 * y mantener la comunicación con los componentes encargados de la simulación
 * de los duelos mágicos.
 * </p>
 *
 * <p>
 * Forma parte de la capa de control dentro del patrón arquitectónico MVC
 * (Modelo–Vista–Controlador).
 * </p>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-10-25
 */
public class ControlLogica {
    
    /**
     * Controlador de la interfaz gráfica
     */
    private ControlInterfaz cInterfaz;

    public ControlLogica() {
        this.cInterfaz = new ControlInterfaz(this);
    }
   
}
