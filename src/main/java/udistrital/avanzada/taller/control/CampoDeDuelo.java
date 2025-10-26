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
 * Esta clase pertenece a la capa de control (no al modelo), ya que contiene
 * lógica de ejecución y coordinación entre entidades del dominio.
 * </p>
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 2.0
 * @since 2025-10-26
 */
public class CampoDeDuelo {

    private final Mago mago1;
    private final Mago mago2;
    private final LibroHechizos libro;
    private final Random random;
    private ResultadoDuelo resultado;

    private static final int META_PUNTOS = 250;
    private static final int TIEMPO_MIN = 250;
    private static final int TIEMPO_MAX = 500;

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
     * Ejecuta el duelo entre los dos magos.
     * <p>
     * Cada mago se ejecuta en un hilo separado. Se alternan los turnos hasta que
     * uno supera los 250 puntos. Al finalizar, se genera un
     * {@link ResultadoDuelo}.
     * </p>
     *
     * @return resultado final del duelo
     */
    public ResultadoDuelo iniciar() {
        // Reinicia el estado de los magos
        mago1.reiniciarPuntaje();
        mago2.reiniciarPuntaje();

        Thread hilo1 = new Thread(() -> ejecutarTurnos(mago1, mago2));
        Thread hilo2 = new Thread(() -> ejecutarTurnos(mago2, mago1));

        hilo1.start();
        hilo2.start();

        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return resultado;
    }

    /**
     * Ejecuta los turnos de lanzamiento de hechizos entre el mago y su rival.
     *
     * @param atacante mago que lanza el hechizo
     * @param rival mago que recibe el ataque
     */
    private synchronized void ejecutarTurnos(Mago atacante, Mago rival) {
        List<Hechizo> lista = libro.getHechizos();

        while (atacante.getPuntosAcumulados() < META_PUNTOS
                && rival.getPuntosAcumulados() < META_PUNTOS) {

            // Seleccionar hechizo aleatorio
            Hechizo elegido = lista.get(random.nextInt(lista.size()));
            atacante.lanzarHechizo(elegido);

            // Simular probabilidad de aturdir al rival (10%)
            if (random.nextInt(10) == 0) {
                rival.aturdir();
            } else {
                rival.recuperar();
            }

            // Pausa aleatoria entre 250 y 500 ms
            try {
                wait(TIEMPO_MIN + random.nextInt(TIEMPO_MAX - TIEMPO_MIN));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            // Condición de victoria
            if (atacante.getPuntosAcumulados() >= META_PUNTOS
                    || rival.getPuntosAcumulados() >= META_PUNTOS) {
                definirResultado();
                notifyAll();
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
}
