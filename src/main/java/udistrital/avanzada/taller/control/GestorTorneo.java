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
 * Gestiona un torneo de eliminación simple entre múltiples magos.
 * <p>
 * El ganador de cada duelo avanza a la siguiente ronda para enfrentar
 * al siguiente mago en la cola. El torneo continúa hasta que solo
 * queda un ganador o no hay más oponentes disponibles.
 * </p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 3.0
 * @since 2025-10-29
 */

//TODO: Hace falta modificar para que se cumpla el ordén de los jugadores del torneo e implementar un JOptionPane para mostar ganadores y botón de siguiente combate
public class GestorTorneo {

    private final Queue<Mago> colaMagos;
    private final List<ResultadoDuelo> historialDuelos;
    private final LibroHechizos libro;
    private Mago campeonActual;
    private int numeroRonda;

    /**
     * Crea un nuevo gestor de torneo con una lista de magos y un libro de hechizos.
     *
     * @param listadoMagos lista de magos participantes
     * @param libro libro de hechizos disponible para los duelos
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
     * Verifica si hay suficientes magos para iniciar un duelo.
     *
     * @return true si hay al menos 2 magos disponibles
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
     * Obtiene el siguiente duelo disponible.
     * <p>
     * Si no hay campeón actual, toma los dos primeros magos de la cola.
     * Si ya hay un campeón, lo enfrenta contra el siguiente mago disponible.
     * </p>
     *
     * @return array con los dos magos que participarán en el duelo
     * @throws IllegalStateException si no hay suficientes magos
     */
    public Mago[] obtenerSiguienteDuelo() {
        if (!hayDueloDisponible()) {
            throw new IllegalStateException("No hay suficientes magos para un duelo");
        }

        Mago mago1;
        Mago mago2;

        if (campeonActual == null) {
            // Primer duelo del torneo
            mago1 = colaMagos.poll();
            mago2 = colaMagos.poll();
            numeroRonda = 1;
        } else {
            // El campeón defiende su título
            mago1 = campeonActual;
            mago2 = colaMagos.poll();
            numeroRonda++;
        }

        return new Mago[]{mago1, mago2};
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
     * Registra el resultado de un duelo y actualiza el campeón actual.
     *
     * @param resultado resultado del duelo a registrar
     */
    private void registrarResultado(ResultadoDuelo resultado) {
        historialDuelos.add(resultado);
        campeonActual = resultado.getGanador();
    }

    /**
     * Obtiene el campeón actual del torneo.
     *
     * @return el mago que ha ganado el último duelo, o null si no ha habido duelos
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

    /**
     * Clase interna para encapsular estadísticas del torneo.
     */
    public static class EstadisticasTorneo {
        private final int rondaActual;
        private final int duelosRealizados;
        private final int magosRestantes;
        private final Mago campeonActual;

        public EstadisticasTorneo(int rondaActual, int duelosRealizados, 
                                  int magosRestantes, Mago campeonActual) {
            this.rondaActual = rondaActual;
            this.duelosRealizados = duelosRealizados;
            this.magosRestantes = magosRestantes;
            this.campeonActual = campeonActual;
        }

        public int getRondaActual() {
            return rondaActual;
        }

        public int getDuelosRealizados() {
            return duelosRealizados;
        }

        public int getMagosRestantes() {
            return magosRestantes;
        }

        public Mago getCampeonActual() {
            return campeonActual;
        }

        //TODO: La misma, no se si se permita usar el toString
        @Override
        public String toString() {
            return String.format(
                "Torneo - Ronda %d | Duelos: %d | Magos restantes: %d | Campeón: %s",
                rondaActual, duelosRealizados, magosRestantes,
                campeonActual != null ? campeonActual.getNombre() : "Ninguno"
            );
        }
    }
}
