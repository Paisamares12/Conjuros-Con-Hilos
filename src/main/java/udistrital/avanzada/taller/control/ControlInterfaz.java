package udistrital.avanzada.taller.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import udistrital.avanzada.taller.modelo.*;
import udistrital.avanzada.taller.vista.VentanaPrincipal;

/**
 * Controlador de la capa de interfaz del proyecto <b>ConjurosConHilos</b>.
 *
 * <p>
 * Gestiona la comunicación entre la vista y la capa lógica sin acceder
 * directamente al modelo. Se limita a manejar eventos de usuario y delegar las
 * operaciones lógicas a {@link ControlLogica}.
 * </p>
 *
 * <p>
 * Cumple el patrón MVC y los principios SOLID (SRP y DIP).
 * </p>
 *
 * <p>
 * Creada por Paula Martínez. Refactorizada por Juan Ariza.
 * </p>
 *
 * @author Paula Martínez
 * @version 6.0
 * @since 2025-10-29
 */
public class ControlInterfaz implements ActionListener {

    /**
     * Controlador de la capa lógica al que se delegan las operaciones.
     */
    private final ControlLogica cLogica;

    /**
     * Ventana principal de la aplicación (punto de entrada a los paneles).
     */
    private VentanaPrincipal vPrincipal;

    /**
     * Indicador de que el archivo de magos fue cargado exitosamente.
     */
    private boolean magosReady = false;

    /**
     * Indicador de que el archivo de hechizos fue cargado exitosamente.
     */
    private boolean hechizosReady = false;

    /**
     * Crea el controlador de interfaz y lanza la ventana principal.
     *
     * @param cLogica controlador lógico asociado a esta interfaz
     */
    public ControlInterfaz(ControlLogica cLogica) {
        this.cLogica = cLogica;
        iniciarPrograma();
    }

    /**
     * Inicializa la ventana principal, la hace visible y conecta los listeners.
     */
    private void iniciarPrograma() {
        this.vPrincipal = new VentanaPrincipal(this);
        vPrincipal.setVisible(true);
        vPrincipal.setLocationRelativeTo(null);
        conectarEventos();
    }

    /**
     * Conecta todos los componentes interactivos de la vista a este
     * controlador.
     *
     * <p>
     * Registra este objeto como {@link ActionListener} de los botones de los
     * paneles Inicio, Cargar y Combate.</p>
     */
    private void conectarEventos() {
        // Inicio
        vPrincipal.getPanelMain().getPanelInicio().getBotonJugar().addActionListener(this);
        vPrincipal.getPanelMain().getPanelInicio().getBotonSalir().addActionListener(this);
        // Cargar
        vPrincipal.getPanelMain().getPanelCargar().getBotonSalir().addActionListener(this);
        vPrincipal.getPanelMain().getPanelCargar().getBotonJugar().addActionListener(this);
        vPrincipal.getPanelMain().getPanelCargar().getBotonCargarMagos().addActionListener(this);
        vPrincipal.getPanelMain().getPanelCargar().getBotonCargarHechizos().addActionListener(this);
        // Combate
        vPrincipal.getPanelMain().getPanelCombate().getBotonVolver().addActionListener(this);
    }

    /**
     * Maneja todos los eventos de clic en botones registrados.
     *
     * <p>
     * Resuelve navegación entre paneles, carga de archivos de propiedades,
     * arranque del torneo y avance entre duelos.
     * </p>
     *
     * @param e evento de acción generado por la vista
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // Salir
        if (src == vPrincipal.getPanelMain().getPanelInicio().getBotonSalir()
                || src == vPrincipal.getPanelMain().getPanelCargar().getBotonSalir()) {
            vPrincipal.dispose();
            System.exit(0);
            return;
        }

        // Ir a cargar
        if (src == vPrincipal.getPanelMain().getPanelInicio().getBotonJugar()) {
            vPrincipal.getPanelMain().mostrarPanelCargar();
            return;
        }

        // Cargar magos
        if (src == vPrincipal.getPanelMain().getPanelCargar().getBotonCargarMagos()) {
            String ruta = vPrincipal.getPanelMain().getPanelCargar().cargarProperties("Magos");
            if (ruta != null && cLogica.cargarMagos(ruta)) {
                magosReady = true;
                JOptionPane.showMessageDialog(vPrincipal, "Magos cargados exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                verificarDatosCompletos();
            }
            return;
        }

        // Cargar hechizos
        if (src == vPrincipal.getPanelMain().getPanelCargar().getBotonCargarHechizos()) {
            String ruta = vPrincipal.getPanelMain().getPanelCargar().cargarProperties("Hechizos");
            if (ruta != null && cLogica.cargarHechizos(ruta)) {
                hechizosReady = true;
                JOptionPane.showMessageDialog(vPrincipal, "Hechizos cargados exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                verificarDatosCompletos();
            }
            return;
        }

        // Jugar torneo
        if (src == vPrincipal.getPanelMain().getPanelCargar().getBotonJugar()) {
            iniciarTorneo();
            return;
        }

        // Volver al inicio (o avanzar al siguiente duelo, según el flujo)
        if (src == vPrincipal.getPanelMain().getPanelCombate().getBotonVolver()) {
            mostrarOpcionesDespuesDuelo();
        }
    }

    /**
     * Muestra un aviso cuando magos y hechizos han sido cargados correctamente,
     * habilitando el inicio del torneo.
     */
    private void verificarDatosCompletos() {
        if (magosReady && hechizosReady) {
            JOptionPane.showMessageDialog(vPrincipal,
                    "¡Todos los datos están listos! Puedes iniciar el torneo.",
                    "Listo para jugar", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Valida que existan datos y condiciones mínimas para iniciar el torneo.
     *
     * <p>
     * Si las condiciones se cumplen, delega la ejecución del primer duelo.</p>
     */
    private void iniciarTorneo() {
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
     * Lanza el siguiente duelo del torneo y conecta los eventos del combate a
     * la vista mediante un observador.
     *
     * <p>
     * Cambia al panel de combate e instala un
     * {@link CampoDeDuelo.ObservadorDuelo} que actualiza la interfaz en tiempo
     * real. La ejecución del duelo es delegada a {@link ControlLogica}.
     * </p>
     */
    private void ejecutarSiguienteDuelo() {
        vPrincipal.getPanelMain().mostrarPanelCombate();

        CampoDeDuelo.ObservadorDuelo obs = new CampoDeDuelo.ObservadorDuelo() {
            @Override
            public void onInicioDuelo(Mago m1, Mago m2) {
                vPrincipal.getPanelMain().getPanelCombate().inicializarDuelo(m1, m2);
            }

            @Override
            public void onHechizoLanzado(Mago m, Hechizo h, int puntos) {
                vPrincipal.getPanelMain().getPanelCombate().actualizarMago(m, h, puntos);
            }

            @Override
            public void onMagoAturdido(Mago m) {
                vPrincipal.getPanelMain().getPanelCombate().marcarAturdido(m);
            }

            @Override
            public void onMagoRecupera(Mago m) {
                vPrincipal.getPanelMain().getPanelCombate().marcarRecuperado(m);
            }

            @Override
            public void onFinDuelo(ResultadoDuelo r) {
                // Encolar la actualización en la EDT para mantener la UI segura.
                SwingUtilities.invokeLater(() -> mostrarResultadoDuelo(r));
            }
        };

        // Delegar completamente la ejecución del duelo al controlador lógico.
        cLogica.ejecutarSiguienteDueloTorneoConObservador(obs);
    }

    /**
     * Presenta el resultado del duelo en el panel de combate y decide, según el
     * estado del torneo, si continuar con el siguiente duelo o finalizar
     * mostrando el campeón.
     *
     * @param r resultado del duelo finalizado
     */
    private void mostrarResultadoDuelo(ResultadoDuelo r) {
        vPrincipal.getPanelMain().getPanelCombate().mostrarResultado(r);

        if (cLogica.puedeIniciarDuelo()) {
            int opcion = JOptionPane.showConfirmDialog(vPrincipal,
                    "¿Deseas continuar con el siguiente duelo?",
                    "Siguiente Ronda", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                ejecutarSiguienteDuelo();
            } else {
                vPrincipal.getPanelMain().mostrarPanelInicio();
            }
        } else {
            Mago campeon = cLogica.getCampeonActual();
            String msg = "🏆 ¡TORNEO FINALIZADO! 🏆\n\nCampeón: "
                    + campeon.getNombre() + "\nCasa: " + campeon.getCasa()
                    + "\nDuelos realizados: " + cLogica.getDuelosRealizados();
            JOptionPane.showMessageDialog(vPrincipal, msg,
                    "Fin del Torneo", JOptionPane.INFORMATION_MESSAGE);
            vPrincipal.getPanelMain().mostrarPanelInicio();
        }
    }

    /**
     * Ofrece opciones al usuario al terminar un duelo cuando se presiona el
     * botón del panel de combate: continuar con el siguiente o regresar al menú
     * de inicio.
     */
    private void mostrarOpcionesDespuesDuelo() {
        if (cLogica.puedeIniciarDuelo()) {
            int op = JOptionPane.showConfirmDialog(vPrincipal,
                    "¿Deseas continuar con el siguiente duelo?",
                    "Siguiente Ronda", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                ejecutarSiguienteDuelo();
            } else {
                vPrincipal.getPanelMain().mostrarPanelInicio();
            }
        } else {
            Mago campeon = cLogica.getCampeonActual();
            String msg = "🏆 ¡TORNEO FINALIZADO! 🏆\n\nCampeón: " + campeon.getNombre()
                    + "\nCasa: " + campeon.getCasa();
            JOptionPane.showMessageDialog(vPrincipal, msg,
                    "¡Tenemos un Campeón!", JOptionPane.INFORMATION_MESSAGE);
            vPrincipal.getPanelMain().mostrarPanelInicio();
        }
    }

    /**
     * Muestra un resumen de resultado de duelo (modo individual) mediante un
     * diálogo.
     *
     * @param r resultado del duelo; si es {@code null}, no se muestra
     * información
     */
    public void mostrarResultado(ResultadoDuelo r) {
        if (r == null) {
            return;
        }
        String msg = "🏆 GANADOR: " + r.getGanador().getNombre()
                + "\nCasa: " + r.getGanador().getCasa()
                + "\n\nPerdedor: " + r.getPerdedor().getNombre()
                + "\nPuntos: " + r.getPuntosGanador() + " - " + r.getPuntosPerdedor();
        JOptionPane.showMessageDialog(vPrincipal, msg, "Resultado del Duelo",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
