package udistrital.avanzada.taller.control;

/**
 * Clase principal del proyecto <b>ConjurosConHilos</b>.
 * <p>
 * Esta clase actúa como punto de entrada de la aplicación. Su única función es
 * inicializar la capa de control lógico del programa, encargada de gestionar la
 * interacción entre el modelo, la vista y los servicios del sistema.
 * </p>
 *
 * <p>
 * Desde este punto se configuran los componentes principales del simulador de
 * duelos mágicos, aplicando la arquitectura MVC y los principios SOLID.
 * </p>
 *
 * @author Paula Martinez
 * @version 6.0
 * @since 2025-10-25
 */

public class Launcher {

    /**
     * Método principal que inicia la aplicación.
     * <p>
     * Crea una nueva instancia de {@code ControlLogica}, que gestiona la lógica
     * central del programa y el flujo de ejecución inicial del simulador.
     * </p>
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        new ControlLogica();
    }
}
