/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import udistrital.avanzada.taller.modelo.*;

/**
 * Controlador lógico que gestiona el desarrollo de un <b>torneo de duelos
 * mágicos</b>
 * entre múltiples {@link Mago magos}, siguiendo un esquema de eliminación
 * simple.
 *
 * <p>
 * Cada duelo es ejecutado dentro de un {@link CampoDeDuelo}, y el ganador de
 * cada enfrentamiento avanza a la siguiente ronda para enfrentarse al siguiente
 * mago en la cola. El proceso continúa hasta que solo queda un campeón
 * definitivo.
 * </p>
 *
 * <p>
 * La clase implementa las reglas de progresión, control de rondas, y evita la
 * repetición de enfrentamientos previos entre los mismos contendientes mediante
 * un registro de parejas ya jugadas.
 * </p>
 *
 * <p>
 * <b>Ejemplo de funcionamiento:</b><br>
 * - Se cargan los magos desde un archivo de propiedades.<br>
 * - Se crea un {@code GestorTorneo} con un {@link LibroHechizos}.<br>
 * - Se ejecutan los duelos uno a uno con {@link #ejecutarSiguienteDuelo()} o
 * {@link #ejecutarSiguienteDueloConObservador(CampoDeDuelo.ObservadorDuelo)}.<br>
 * - Cada resultado se registra y el campeón avanza automáticamente.
 * </p>
 *
 * <p>
 * Creada por Juan Ariza y modificada por Paula Martínez.
 * </p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 6.0
 * @since 2025-10-29
 */
public class GestorTorneo {

    /**
     * Cola de magos que esperan su turno para participar en un duelo.
     */
    private final Queue<Mago> colaMagos;

    /**
     * Lista con el historial completo de duelos realizados.
     */
    private final List<ResultadoDuelo> historialDuelos;

    /**
     * Libro de hechizos que se utiliza en cada duelo.
     */
    private final LibroHechizos libro;

    /**
     * Mago que actualmente ostenta el título de campeón.
     */
    private Mago campeonActual;

    /**
     * Número de la ronda actual (incrementa con cada duelo).
     */
    private int numeroRonda;

    /**
     * Registro de las parejas de magos que ya se han enfrentado.
     */
    private final java.util.Set<String> parejasJugadas = new java.util.HashSet<>();

    /**
     * Crea un nuevo gestor de torneo con un listado de magos y un libro de
     * hechizos.
     *
     * @param listadoMagos lista de magos participantes
     * @param libro libro de hechizos disponible para los duelos
     * @throws IllegalArgumentException si alguno de los parámetros es nulo
     */
    public GestorTorneo(ListadoMagos listadoMagos, LibroHechizos libro) {
        if (listadoMagos == null || listadoMagos.getMagos() == null) {
            throw new IllegalArgumentException("El listado de magos no puede ser nulo");
        }
        if (libro == null) {
            throw new IllegalArgumentException("El libro de hechizos no puede ser nulo");
        }

        this.colaMagos = new LinkedList<>(listadoMagos.getMagos());
        this.historialDuelos = new ArrayList<>();
        this.libro = libro;
        this.campeonActual = null;
        this.numeroRonda = 0;
    }

    /**
     * Verifica si existen suficientes magos para iniciar un nuevo duelo.
     *
     * @return {@code true} si hay al menos dos magos disponibles para
     * enfrentar; {@code false} si no es posible iniciar un nuevo combate
     */
    public boolean hayDueloDisponible() {
        // Necesitamos al menos 2 magos: el campeón actual y un retador
        if (campeonActual == null) {
            return colaMagos.size() >= 2;
        } else {
            return !colaMagos.isEmpty();
        }
    }

    /**
     * Obtiene los dos magos que participarán en el siguiente duelo.
     *
     * <p>
     * Si no hay campeón actual, toma los dos primeros magos de la cola. Si ya
     * existe un campeón, este se enfrenta contra el siguiente mago disponible
     * que no haya combatido previamente con él.
     * </p>
     *
     * @return un arreglo de dos magos listos para el duelo
     * @throws IllegalStateException si no hay suficientes magos disponibles
     */
    public Mago[] obtenerSiguienteDuelo() {
        if (!hayDueloDisponible()) {
            throw new IllegalStateException("No hay suficientes magos para un duelo");
        }

        Mago mago1;
        Mago mago2;

        if (campeonActual == null) {
            mago1 = colaMagos.poll();
            mago2 = colaMagos.poll();
            numeroRonda = 1;
        } else {
            mago1 = campeonActual;
            mago2 = seleccionarRivalNoRepetido(mago1);

            if (mago2 == null) {
                // Política de fallback: si no hay rivales nuevos, se permite repetición
                mago2 = colaMagos.poll();
            }
            numeroRonda++;
        }

        return new Mago[]{mago1, mago2};
    }

    /**
     * Selecciona el siguiente rival del campeón actual que aún no haya sido
     * enfrentado.
     *
     * @param campeon mago que mantiene el título actual
     * @return un nuevo rival, o {@code null} si todos ya fueron enfrentados
     */
    private Mago seleccionarRivalNoRepetido(Mago campeon) {
        if (colaMagos.isEmpty()) {
            return null;
        }
        int n = colaMagos.size();
        for (int i = 0; i < n; i++) {
            Mago cand = colaMagos.poll(); // saca el primero
            if (!yaJugaron(campeon, cand)) {
                //Este no ha jugado contra el campeón: lo usamos
                return cand;
            } else {
                // lo rotamos al final
                colaMagos.offer(cand);
            }
        }
        return null; // no hay opción sin repetición
    }

    /**
     * Verifica si dos magos ya se han enfrentado previamente.
     *
     * @param a primer mago
     * @param b segundo mago
     * @return {@code true} si la pareja ya aparece registrada, {@code false} si
     * no
     */
    private boolean yaJugaron(Mago a, Mago b) {
        return parejasJugadas.contains(llave(a, b));
    }

    /**
     * Registra una pareja de magos como enfrentada.
     *
     * @param a primer mago
     * @param b segundo mago
     */
    private void registrarPareja(Mago a, Mago b) {
        parejasJugadas.add(llave(a, b));
    }

    /**
     * Crea una clave única y simétrica que representa un enfrentamiento.
     *
     * @param a primer mago
     * @param b segundo mago
     * @return cadena única en formato “nombre1 vs nombre2”
     */
    private String llave(Mago a, Mago b) {
        String n1 = a.getNombre(), n2 = b.getNombre();
        return (n1.compareTo(n2) < 0) ? (n1 + " vs " + n2) : (n2 + " vs " + n1);
    }

    /**
     * Registra el resultado de un duelo, actualizando al nuevo campeón y el
     * historial.
     *
     * @param resultado resultado final del duelo a registrar
     */
    private void registrarResultado(ResultadoDuelo resultado) {
        historialDuelos.add(resultado);
        campeonActual = resultado.getGanador();
        // ✅ registra la pareja jugada
        registrarPareja(resultado.getGanador(), resultado.getPerdedor());
    }

    /**
     * Ejecuta el siguiente duelo disponible.
     *
     * @return resultado del duelo ejecutado
     * @throws IllegalStateException si no hay suficientes magos
     */
    public ResultadoDuelo ejecutarSiguienteDuelo() {
        Mago[] contendientes = obtenerSiguienteDuelo();

        CampoDeDuelo duelo = new CampoDeDuelo(contendientes[0], contendientes[1], libro);
        ResultadoDuelo resultado = duelo.iniciar();

        registrarResultado(resultado);

        return resultado;
    }

    /**
     * Ejecuta el siguiente duelo con un observador para actualizar la UI.
     *
     * @param observador observador que será notificado de los eventos del duelo
     * @return resultado del duelo ejecutado
     * @throws IllegalStateException si no hay suficientes magos
     */
    public ResultadoDuelo ejecutarSiguienteDueloConObservador(CampoDeDuelo.ObservadorDuelo observador) {
        Mago[] contendientes = obtenerSiguienteDuelo();

        CampoDeDuelo duelo = new CampoDeDuelo(contendientes[0], contendientes[1], libro);
        duelo.setObservador(observador);
        ResultadoDuelo resultado = duelo.iniciar();

        registrarResultado(resultado);

        return resultado;
    }

    /**
     * Obtiene el campeón actual del torneo.
     *
     * @return el mago que ha ganado el último duelo, o null si no ha habido
     * duelos
     */
    public Mago getCampeonActual() {
        return campeonActual;
    }

    /**
     * Obtiene el número de la ronda actual.
     *
     * @return número de ronda (comienza en 1)
     */
    public int getNumeroRonda() {
        return numeroRonda;
    }

    /**
     * Obtiene la cantidad de magos restantes en espera.
     *
     * @return número de magos en la cola
     */
    public int getMagosRestantes() {
        return colaMagos.size();
    }

    /**
     * Obtiene el historial completo de duelos realizados.
     *
     * @return lista inmutable con todos los resultados
     */
    public List<ResultadoDuelo> getHistorialDuelos() {
        return new ArrayList<>(historialDuelos);
    }

    /**
     * Obtiene estadísticas del torneo actual.
     *
     * @return objeto con estadísticas del torneo
     */
    public EstadisticasTorneo obtenerEstadisticas() {
        return new EstadisticasTorneo(
                numeroRonda,
                historialDuelos.size(),
                colaMagos.size(),
                campeonActual
        );
    }

    /**
     * Reinicia el torneo con una nueva lista de magos.
     *
     * @param listadoMagos nueva lista de magos participantes
     */
    public void reiniciarTorneo(ListadoMagos listadoMagos) {
        if (listadoMagos == null || listadoMagos.getMagos() == null) {
            throw new IllegalArgumentException("El listado de magos no puede ser nulo");
        }

        colaMagos.clear();
        colaMagos.addAll(listadoMagos.getMagos());
        historialDuelos.clear();
        campeonActual = null;
        numeroRonda = 0;
    }

    // ------------------------------------------------------------
    //   CLASE INTERNA: Estadísticas del torneo
    // ------------------------------------------------------------
    /**
     * Clase auxiliar inmutable que encapsula estadísticas del torneo actual.
     */
    public static class EstadisticasTorneo {

        private final int rondaActual;
        private final int duelosRealizados;
        private final int magosRestantes;
        private final Mago campeonActual;

        /**
         * Crea un nuevo contenedor de estadísticas.
         *
         * @param rondaActual número de ronda actual
         * @param duelosRealizados cantidad total de duelos completados
         * @param magosRestantes número de magos que quedan en la cola
         * @param campeonActual mago que ostenta el título actual
         */
        public EstadisticasTorneo(int rondaActual, int duelosRealizados,
                int magosRestantes, Mago campeonActual) {
            this.rondaActual = rondaActual;
            this.duelosRealizados = duelosRealizados;
            this.magosRestantes = magosRestantes;
            this.campeonActual = campeonActual;
        }

        /**
         * Obtiene el número de la ronda actual del torneo.
         *
         * <p>
         * Este valor se incrementa automáticamente cada vez que se ejecuta un
         * nuevo duelo. Inicia en 1 durante el primer enfrentamiento y aumenta
         * progresivamente hasta el último combate.
         * </p>
         *
         * @return número entero correspondiente a la ronda actual del torneo
         */
        public int getRondaActual() {
            return rondaActual;
        }

        /**
         * Obtiene la cantidad total de duelos que se han realizado hasta el
         * momento.
         *
         * <p>
         * Este número coincide con el tamaño del historial de duelos
         * registrados y permite conocer el avance del torneo.
         * </p>
         *
         * @return número entero que representa la cantidad de duelos
         * completados
         */
        public int getDuelosRealizados() {
            return duelosRealizados;
        }

        /**
         * Devuelve la cantidad de magos que aún no han participado en un duelo.
         *
         * <p>
         * Este valor corresponde al tamaño de la cola de magos en espera de ser
         * enfrentados contra el campeón actual.
         * </p>
         *
         * @return número de magos restantes en la cola del torneo
         */
        public int getMagosRestantes() {
            return magosRestantes;
        }

        /**
         * Obtiene una referencia al mago que actualmente ostenta el título de
         * campeón.
         *
         * <p>
         * El campeón actual corresponde al ganador del último duelo realizado.
         * Si aún no se ha jugado ningún enfrentamiento, este valor puede ser
         * {@code null}.
         * </p>
         *
         * @return instancia de {@link Mago} que representa al campeón actual, o
         * {@code null} si no existe
         */
        public Mago getCampeonActual() {
            return campeonActual;
        }
    }
}
