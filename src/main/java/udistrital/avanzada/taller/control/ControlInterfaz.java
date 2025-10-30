package udistrital.avanzada.taller.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import udistrital.avanzada.taller.modelo.*;
import udistrital.avanzada.taller.vista.VentanaPrincipal;

/**
 * Controlador de la capa de interfaz del proyecto <b>ConjurosConHilos</b>.
 * <p>
 * Se encarga de recibir los eventos generados por la vista, comunicarlos a la
 * capa lógica y actualizar la interfaz según los resultados del sistema.
 *
 * Creada por Paula Martínez Modificada por Juan Ariza
 * </p>
 *
 * @author Paula Martínez
 * @version 5.0
 * @since 2025-10-29
 */
//TODO: Sacar todo el modelo de aca, el ControlInterfaz no se comunica directamente con el modelo
//TODO: revisar que cumpla con el MVC y los SOLID 
public class ControlInterfaz implements ActionListener {

    private final ControlLogica cLogica;
    private VentanaPrincipal vPrincipal;
    /*
    * Control para la validación de existencia de magos y hechizos
     */
    private boolean magosReady = false;
    private boolean hechizosReady = false;

    /**
     * Constructor que inicializa la interfaz gráfica principal.
     *
     * @param cLogica controlador lógico asociado
     */
    public ControlInterfaz(ControlLogica cLogica) {
        this.cLogica = cLogica;
        iniciarPrograma();
    }

    /**
     * Inicializa la interfaz principal y conecta los eventos.
     */
    private void iniciarPrograma() {
        this.vPrincipal = new VentanaPrincipal(this);
        this.vPrincipal.setVisible(true);
        this.vPrincipal.setLocationRelativeTo(null);
        conectarEventos();
    }

    /**
     * Conecta los botones de la interfaz con sus listeners.
     */
    private void conectarEventos() {
        // Panel Inicio
        this.vPrincipal.getPanelMain().getPanelInicio().getBotonJugar()
                .addActionListener(this);
        this.vPrincipal.getPanelMain().getPanelInicio().getBotonSalir()
                .addActionListener(this);

        // Panel Cargar
        this.vPrincipal.getPanelMain().getPanelCargar().getBotonSalir()
                .addActionListener(this);
        this.vPrincipal.getPanelMain().getPanelCargar().getBotonJugar()
                .addActionListener(this);
        this.vPrincipal.getPanelMain().getPanelCargar().getBotonCargarMagos()
                .addActionListener(this);
        this.vPrincipal.getPanelMain().getPanelCargar().getBotonCargarHechizos()
                .addActionListener(this);

        // Panel Combate
        this.vPrincipal.getPanelMain().getPanelCombate().getBotonVolver()
                .addActionListener(this);
    }

    /**
     * Maneja los eventos generados por los botones de la aplicación.
     *
     * @param e evento de acción recibido
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // BOTONES DE SALIDA
        if (e.getSource() == vPrincipal.getPanelMain().getPanelInicio().getBotonSalir()
                || e.getSource() == vPrincipal.getPanelMain().getPanelCargar().getBotonSalir()) {
            vPrincipal.dispose();
            System.exit(0);
            return;
        }

        // BOTÓN "JUGAR" EN PANEL INICIO
        if (e.getSource() == vPrincipal.getPanelMain().getPanelInicio().getBotonJugar()) {
            vPrincipal.getPanelMain().mostrarPanelCargar();
            return;
        }

        // BOTONES "CARGAR" EN PANEL CARGAR
        if (e.getSource() == vPrincipal.getPanelMain().getPanelCargar().getBotonCargarMagos()) {
            String ruta = vPrincipal.getPanelMain().getPanelCargar().cargarProperties("Magos");
            if (ruta != null) {
                boolean exito = cLogica.cargarMagos(ruta);
                if (exito) {
                    magosReady = true;
                    JOptionPane.showMessageDialog(vPrincipal,
                            "Magos cargados exitosamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    verificarDatosCompletos();
                } else {
                    JOptionPane.showMessageDialog(vPrincipal,
                            "Error al cargar el archivo de magos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }

        if (e.getSource() == vPrincipal.getPanelMain().getPanelCargar().getBotonCargarHechizos()) {
            String ruta = vPrincipal.getPanelMain().getPanelCargar().cargarProperties("Hechizos");
            if (ruta != null) {
                boolean exito = cLogica.cargarHechizos(ruta);
                if (exito) {
                    hechizosReady = true;
                    JOptionPane.showMessageDialog(vPrincipal,
                            "Hechizos cargados exitosamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    verificarDatosCompletos();
                } else {
                    JOptionPane.showMessageDialog(vPrincipal,
                            "Error al cargar el archivo de hechizos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }

        // BOTÓN "JUGAR" EN PANEL CARGAR
        if (e.getSource() == vPrincipal.getPanelMain().getPanelCargar().getBotonJugar()) {
            ejecutarTorneo();
            return;
        }

        // BOTÓN "VOLVER" EN PANEL COMBATE
        if (e.getSource() == vPrincipal.getPanelMain().getPanelCombate().getBotonVolver()) {
            mostrarOpcionesDespuesDuelo();
        }
    }

    /**
     * Verifica si ya se han cargado ambos archivos de propiedades.
     */
    private void verificarDatosCompletos() {
        if (magosReady && hechizosReady) {
            JOptionPane.showMessageDialog(vPrincipal,
                    "¡Todos los datos están listos! Puedes iniciar el torneo.",
                    "Listo para jugar", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Inicia el torneo mostrando el primer duelo.
     */
    private void ejecutarTorneo() {
        if (!cLogica.datosListos()) {
            JOptionPane.showMessageDialog(vPrincipal,
                    "Debes cargar primero los archivos de magos y hechizos.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!cLogica.puedeIniciarDuelo()) {
            JOptionPane.showMessageDialog(vPrincipal,
                    "No hay suficientes magos para iniciar un duelo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ejecutarSiguienteDuelo();
    }

    /**
     * Ejecuta el siguiente duelo del torneo con actualización visual.
     */
    private void ejecutarSiguienteDuelo() {
        if (!cLogica.puedeIniciarDuelo()) {
            GestorTorneo.EstadisticasTorneo stats = cLogica.getGestorTorneo().obtenerEstadisticas();
            Mago campeon = stats.getCampeonActual();

            String mensaje = "🏆 ¡TORNEO FINALIZADO! 🏆\n\n"
                    + "Campeón: " + campeon.getNombre() + "\n"
                    + "Casa: " + campeon.getCasa() + "\n"
                    + "Duelos realizados: " + stats.getDuelosRealizados();

            JOptionPane.showMessageDialog(vPrincipal, mensaje,
                    "Fin del Torneo", JOptionPane.INFORMATION_MESSAGE);

            vPrincipal.getPanelMain().mostrarPanelInicio();
            return;
        }

        vPrincipal.getPanelMain().mostrarPanelCombate();

        CampoDeDuelo.ObservadorDuelo observador = new CampoDeDuelo.ObservadorDuelo() {
            @Override
            public void onInicioDuelo(Mago mago1, Mago mago2) {
                // ✅ Inicializa la UI AQUÍ con los contendientes reales del duelo
                vPrincipal.getPanelMain().getPanelCombate().inicializarDuelo(mago1, mago2);
            }

            @Override
            public void onHechizoLanzado(Mago mago, Hechizo hechizo, int puntosActuales) {
                vPrincipal.getPanelMain().getPanelCombate()
                        .actualizarMago(mago, hechizo, puntosActuales);
            }

            @Override
            public void onMagoAturdido(Mago mago) {
                vPrincipal.getPanelMain().getPanelCombate().marcarAturdido(mago);
            }

            @Override
            public void onMagoRecupera(Mago mago) {
                vPrincipal.getPanelMain().getPanelCombate().marcarRecuperado(mago);
            }

            @Override
            public void onFinDuelo(ResultadoDuelo resultado) {
                SwingUtilities.invokeLater(() -> {
                    // Muestra el resultado en el panel
                    vPrincipal.getPanelMain().getPanelCombate().mostrarResultado(resultado);

                    // ¿Quedan más duelos?
                    if (cLogica.puedeIniciarDuelo()) {
                        int opcion = JOptionPane.showConfirmDialog(
                                vPrincipal,
                                "¿Deseas continuar con el siguiente duelo?",
                                "Siguiente Ronda",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        if (opcion == JOptionPane.YES_OPTION) {
                            ejecutarSiguienteDuelo();               // ⬅️ lanza el siguiente
                        } else {
                            vPrincipal.getPanelMain().mostrarPanelInicio();
                        }
                    } else {
                        // Fin del torneo
                        GestorTorneo.EstadisticasTorneo stats = cLogica.getGestorTorneo().obtenerEstadisticas();
                        Mago campeon = stats.getCampeonActual();
                        String mensaje = "🏆 ¡TORNEO FINALIZADO! 🏆\n\n"
                                + "Campeón: " + campeon.getNombre() + "\n"
                                + "Casa: " + campeon.getCasa() + "\n"
                                + "Duelos realizados: " + stats.getDuelosRealizados();
                        JOptionPane.showMessageDialog(vPrincipal, mensaje, "Fin del Torneo",
                                JOptionPane.INFORMATION_MESSAGE);
                        vPrincipal.getPanelMain().mostrarPanelInicio();
                    }
                });
            }
        };

        // Ejecuta sin bloquear la UI
        SwingWorker<ResultadoDuelo, Void> worker = new SwingWorker<>() {
            @Override
            protected ResultadoDuelo doInBackground() {
                return cLogica.iniciarSiguienteDueloTorneoConObservador(observador);
            }

            @Override
            protected void done() {
                /* no-op */ }
        };

        worker.execute();
    }

    //TODO: no sé si se puede usar el JOptionPane para esto
    /**
     * Muestra opciones después de finalizar un duelo.
     */
    private void mostrarOpcionesDespuesDuelo() {
        if (!cLogica.puedeIniciarDuelo()) {
            // No hay más duelos, mostrar campeón final
            GestorTorneo.EstadisticasTorneo stats = cLogica.getGestorTorneo().obtenerEstadisticas();
            Mago campeon = stats.getCampeonActual();

            String mensaje = "🏆 ¡TORNEO FINALIZADO! 🏆\n\n"
                    + "Campeón: " + campeon.getNombre() + "\n"
                    + "Casa: " + campeon.getCasa() + "\n"
                    + "Duelos ganados: " + stats.getRondaActual();

            JOptionPane.showMessageDialog(vPrincipal, mensaje,
                    "¡Tenemos un Campeón!", JOptionPane.INFORMATION_MESSAGE);

            vPrincipal.getPanelMain().mostrarPanelInicio();
        } else {
            // Hay más duelos disponibles
            int opcion = JOptionPane.showConfirmDialog(vPrincipal,
                    "¿Deseas continuar con el siguiente duelo?",
                    "Siguiente Ronda",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                ejecutarSiguienteDuelo();
            } else {
                vPrincipal.getPanelMain().mostrarPanelInicio();
            }
        }
    }

    //TODO: no sabría si quitar este método pq no se uso
    /**
     * Muestra el resultado final de un duelo (usado para modo individual).
     *
     * @param resultado objeto con los datos del combate
     */
    public void mostrarResultado(ResultadoDuelo resultado) {
        if (resultado == null || resultado.getGanador() == null) {
            JOptionPane.showMessageDialog(vPrincipal,
                    "El duelo no generó un resultado válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String mensaje = "🏆 GANADOR: " + resultado.getGanador().getNombre() + "\n"
                + "Casa: " + resultado.getGanador().getCasa() + "\n\n"
                + "Perdedor: " + resultado.getPerdedor().getNombre() + "\n"
                + "Casa: " + resultado.getPerdedor().getCasa() + "\n\n"
                + "Puntos: " + resultado.getPuntosGanador() + " - "
                + resultado.getPuntosPerdedor() + "\n"
                + "Hechizos lanzados (ganador): " + resultado.getHechizosLanzadosGanador();

        JOptionPane.showMessageDialog(vPrincipal, mensaje,
                "Resultado del Duelo", JOptionPane.INFORMATION_MESSAGE);
    }
}
