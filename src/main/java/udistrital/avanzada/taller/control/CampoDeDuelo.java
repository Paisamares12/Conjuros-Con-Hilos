package udistrital.avanzada.taller.control;

import java.util.List;
import java.util.Random;
import udistrital.avanzada.taller.modelo.*;

/**
 * Gestiona un duelo mágico entre dos magos utilizando hilos concurrentes.
 * <p>
 * Cada mago lanza hechizos de forma alternada hasta que uno de ellos alcanza
 * los 250 puntos o más. El proceso simula pausas y turnos de lanzamiento,
 * reflejando una ejecución concurrente y sincronizada.
 * </p>
 * 
 * <p>
 * Creada por Juan Sebastián Btavo Rojas
 * Modificada por Juan Ariza
 * </p>
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 3.0
 * @since 2025-10-29
 */
public class CampoDeDuelo {

    private final Mago mago1;
    private final Mago mago2;
    private final LibroHechizos libro;
    private final Random random;
    private ResultadoDuelo resultado;
    
    // Control de turnos con Volatile para mejorar la sincronización
    private volatile boolean turnoMago1 = true;
    private volatile boolean dueloActivo = true;
    private final Object monitor = new Object();
    
    // Observador para actualizar la UI
    private ObservadorDuelo observador;

    private static final int META_PUNTOS = 250;
    private static final int TIEMPO_MIN = 250;
    private static final int TIEMPO_MAX = 500;
    private static final double PROBABILIDAD_ATURDIR = 0.10; // 10%

    /**
     * Crea un nuevo campo de duelo entre dos magos y un libro de hechizos.
     *
     * @param mago1 primer mago
     * @param mago2 segundo mago
     * @param libro libro de hechizos disponible
     */
    public CampoDeDuelo(Mago mago1, Mago mago2, LibroHechizos libro) {
        this.mago1 = mago1;
        this.mago2 = mago2;
        this.libro = libro;
        this.random = new Random();
        this.resultado = new ResultadoDuelo();
    }

    /**
     * Establece el observador que será notificado de los eventos del duelo.
     *
     * @param observador observador del duelo
     */
    public void setObservador(ObservadorDuelo observador) {
        this.observador = observador;
    }

    /**
     * Ejecuta el duelo entre los dos magos.
     * <p>
     * Cada mago se ejecuta en un hilo separado. Se alternan los turnos hasta que
     * uno supera los 250 puntos. Al finalizar, se genera un {@link ResultadoDuelo}.
     * </p>
     *
     * @return resultado final del duelo
     */
    public ResultadoDuelo iniciar() {
        // Reinicia el estado del duelo
        mago1.reiniciarPuntaje();
        mago2.reiniciarPuntaje();
        turnoMago1 = true;
        dueloActivo = true;

        // Notificar inicio del duelo
        if (observador != null) {
            observador.onInicioDuelo(mago1, mago2);
        }

        // Crear hilos para cada mago
        Thread hilo1 = new Thread(() -> ejecutarTurnos(mago1, mago2, true), "Hilo-" + mago1.getNombre());
        Thread hilo2 = new Thread(() -> ejecutarTurnos(mago2, mago1, false), "Hilo-" + mago2.getNombre());

        hilo1.start();
        hilo2.start();

        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Notificar fin del duelo
        if (observador != null) {
            observador.onFinDuelo(resultado);
        }

        return resultado;
    }

    /**
     * Ejecuta los turnos de lanzamiento de hechizos para un mago específico.
     * <p>
     * Este método implementa la sincronización correcta usando wait/notify
     * para asegurar que los magos se alternen correctamente.
     * </p>
     *
     * @param atacante mago que lanza el hechizo
     * @param rival mago que recibe el ataque
     * @param esMago1 indica si el atacante es el mago1 (true) o mago2 (false)
     */
    private void ejecutarTurnos(Mago atacante, Mago rival, boolean esMago1) {
        List<Hechizo> lista = libro.getHechizos();

        while (dueloActivo) {
            synchronized (monitor) {
                // Esperar hasta que sea el turno de este mago
                while (turnoMago1 != esMago1 && dueloActivo) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                // Verificar si el duelo sigue activo después de esperar
                if (!dueloActivo) {
                    break;
                }

                // Verificar si el atacante está aturdido
                if (atacante.estaAturdido()) {
                    atacante.recuperar();
                    
                    if (observador != null) {
                        observador.onMagoRecupera(atacante);
                    }
                    
                    // Cambiar turno sin lanzar hechizo
                    turnoMago1 = !turnoMago1;
                    monitor.notifyAll();
                    continue;
                }

                // Seleccionar hechizo aleatorio
                Hechizo elegido = lista.get(random.nextInt(lista.size()));
                atacante.lanzarHechizo(elegido);
                int puntosActuales = atacante.getPuntosAcumulados();

                // Notificar lanzamiento de hechizo
                if (observador != null) {
                    observador.onHechizoLanzado(atacante, elegido, puntosActuales);
                }

                // Verificar condición de victoria
                if (atacante.getPuntosAcumulados() >= META_PUNTOS) {
                    definirResultado();
                    dueloActivo = false;
                    monitor.notifyAll();
                    break;
                }

                // Simular probabilidad de aturdir al rival (10%)
                if (random.nextDouble() < PROBABILIDAD_ATURDIR) {
                    rival.aturdir();
                    
                    if (observador != null) {
                        observador.onMagoAturdido(rival);
                    }
                }

                // Cambiar turno
                turnoMago1 = !turnoMago1;
                monitor.notifyAll();
            }

            // Pausa aleatoria entre 250 y 500 ms (fuera del bloque sincronizado)
            try {
                int pausa = TIEMPO_MIN + random.nextInt(TIEMPO_MAX - TIEMPO_MIN + 1);
                Thread.sleep(pausa);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Determina el ganador y registra los datos finales del duelo.
     */
    private void definirResultado() {
        Mago ganador = (mago1.getPuntosAcumulados() >= META_PUNTOS) ? mago1 : mago2;
        Mago perdedor = (ganador == mago1) ? mago2 : mago1;

        resultado.setGanador(ganador);
        resultado.setPerdedor(perdedor);
        resultado.setPuntosGanador(ganador.getPuntosAcumulados());
        resultado.setPuntosPerdedor(perdedor.getPuntosAcumulados());
        resultado.setHechizosLanzadosGanador(ganador.getHechizosLanzados());
    }

    /**
     * Interfaz para observar eventos durante el duelo.
     * Implementa el patrón Observer para actualizar la UI en tiempo real.
     * <p>
     * Los hilos son los unicos que saben que estan haciendo, se establece un observador para
     * que notifique a la vista el desarrollo del duelo
     * </p>
     */
    public interface ObservadorDuelo {
        /**
         * Se invoca al inicio del duelo.
         * @param mago1
         * @param mago2
         */
        void onInicioDuelo(Mago mago1, Mago mago2);

        /**
         * Se invoca cuando un mago lanza un hechizo.
         * @param mago
         * @param hechizo
         * @param puntosActuales
         */
        void onHechizoLanzado(Mago mago, Hechizo hechizo, int puntosActuales);

        /**
         * Se invoca cuando un mago es aturdido.
         * @param mago
         */
        void onMagoAturdido(Mago mago);

        /**
         * Se invoca cuando un mago se recupera del aturdimiento.
         * @param mago
         */
        void onMagoRecupera(Mago mago);

        /**
         * Se invoca al finalizar el duelo.
         * @param resultado
         */
        void onFinDuelo(ResultadoDuelo resultado);
    }
}