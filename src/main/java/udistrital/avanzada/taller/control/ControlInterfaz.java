package udistrital.avanzada.taller.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import udistrital.avanzada.taller.modelo.*;
import udistrital.avanzada.taller.vista.*;

/**
 * Controlador de la capa de interfaz del proyecto <b>ConjurosConHilos</b>.
 * <p>
 * Actúa como adaptador entre el modelo y la vista,
 * convirtiendo objetos del modelo en interfaces que la vista puede usar
 * sin depender directamente del modelo (respetando MVC completamente).
 * </p>
 *
 * Cumple el patrón MVC y los principios SRP y DIP.
 *
 * @author Paula Martínez
 * @version 6.0 - Refactorizado MVC
 * @since 2025-10-31
 */
public class ControlInterfaz implements ActionListener {

    private final ControlLogica cLogica;
    private VentanaPrincipal vPrincipal;

    private boolean magosReady = false;
    private boolean hechizosReady = false;

    public ControlInterfaz(ControlLogica cLogica) {
        this.cLogica = cLogica;
        iniciarPrograma();
    }

    private void iniciarPrograma() {
        this.vPrincipal = new VentanaPrincipal(this);
        vPrincipal.setVisible(true);
        vPrincipal.setLocationRelativeTo(null);
        conectarEventos();
    }

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

        // Volver al inicio
        if (src == vPrincipal.getPanelMain().getPanelCombate().getBotonVolver()) {
            mostrarOpcionesDespuesDuelo();
        }
    }

    private void verificarDatosCompletos() {
        if (magosReady && hechizosReady) {
            JOptionPane.showMessageDialog(vPrincipal,
                    "¡Todos los datos están listos! Puedes iniciar el torneo.",
                    "Listo para jugar", JOptionPane.INFORMATION_MESSAGE);
        }
    }

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

    private void ejecutarSiguienteDuelo() {
        vPrincipal.getPanelMain().mostrarPanelCombate();

        // Crear observador que adapta del modelo a la vista
        CampoDeDuelo.ObservadorDuelo obs = new CampoDeDuelo.ObservadorDuelo() {
            @Override
            public void onInicioDuelo(Mago m1, Mago m2) {
                // Convertir Mago del modelo a InfoMagoVista
                InfoMagoVista info1 = adaptarMago(m1);
                InfoMagoVista info2 = adaptarMago(m2);
                vPrincipal.getPanelMain().getPanelCombate().inicializarDuelo(info1, info2);
            }

            @Override
            public void onHechizoLanzado(Mago m, Hechizo h, int puntos) {
                // Convertir Hechizo del modelo a InfoHechizoVista
                InfoHechizoVista infoHechizo = adaptarHechizo(h);
                vPrincipal.getPanelMain().getPanelCombate().actualizarMago(
                    m.getNombre(), infoHechizo, puntos
                );
            }

            @Override
            public void onMagoAturdido(Mago m) {
                vPrincipal.getPanelMain().getPanelCombate().marcarAturdido(m.getNombre());
            }

            @Override
            public void onMagoRecupera(Mago m) {
                vPrincipal.getPanelMain().getPanelCombate().marcarRecuperado(m.getNombre());
            }

            @Override
            public void onFinDuelo(ResultadoDuelo r) {
                SwingUtilities.invokeLater(() -> {
                    // Convertir ResultadoDuelo del modelo a InfoResultadoDueloVista
                    InfoResultadoDueloVista infoResultado = adaptarResultado(r);
                    mostrarResultadoDuelo(infoResultado);
                });
            }
        };

        // Ejecutar duelo con el observador
        cLogica.ejecutarSiguienteDueloTorneoConObservador(obs);
    }

    private void mostrarResultadoDuelo(InfoResultadoDueloVista r) {
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

    private void mostrarOpcionesDespuesDuelo() {
        if (cLogica.puedeIniciarDuelo()) {
            int op = JOptionPane.showConfirmDialog(vPrincipal,
                    "¿Deseas continuar con el siguiente duelo?",
                    "Siguiente Ronda", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) ejecutarSiguienteDuelo();
            else vPrincipal.getPanelMain().mostrarPanelInicio();
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
     * Notifica el resultado de un duelo a la vista.
     * Convierte el ResultadoDuelo del modelo a InfoResultadoDueloVista.
     * 
     * @param resultado resultado del duelo desde el modelo
     */
    public void notificarResultadoDuelo(ResultadoDuelo resultado) {
        if (resultado == null) return;
        
        InfoResultadoDueloVista infoResultado = adaptarResultado(resultado);
        
        String msg = "🏆 GANADOR: " + infoResultado.getGanador().getNombre()
                + "\nCasa: " + infoResultado.getGanador().getCasa()
                + "\n\nPerdedor: " + infoResultado.getPerdedor().getNombre()
                + "\nPuntos: " + infoResultado.getPuntosGanador() 
                + " - " + infoResultado.getPuntosPerdedor();
        
        JOptionPane.showMessageDialog(vPrincipal, msg, "Resultado del Duelo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ========== MÉTODOS ADAPTADORES (Modelo -> Vista) ==========
    
    /**
     * Adapta un Mago del modelo a InfoMagoVista para la vista.
     * Este método actúa como puente entre capas.
     */
    private InfoMagoVista adaptarMago(Mago mago) {
        return new InfoMagoVista() {
            @Override
            public String getNombre() {
                return mago.getNombre();
            }

            @Override
            public String getCasa() {
                return mago.getCasa();
            }
        };
    }
    
    /**
     * Adapta un Hechizo del modelo a InfoHechizoVista para la vista.
     */
    private InfoHechizoVista adaptarHechizo(Hechizo hechizo) {
        return new InfoHechizoVista() {
            @Override
            public String getNombre() {
                return hechizo.getNombre();
            }

            @Override
            public int getPuntos() {
                return hechizo.getPuntos();
            }
        };
    }
    
    /**
     * Adapta un ResultadoDuelo del modelo a InfoResultadoDueloVista para la vista.
     */
    private InfoResultadoDueloVista adaptarResultado(ResultadoDuelo resultado) {
        return new InfoResultadoDueloVista() {
            @Override
            public InfoMagoVista getGanador() {
                return adaptarMago(resultado.getGanador());
            }

            @Override
            public InfoMagoVista getPerdedor() {
                return adaptarMago(resultado.getPerdedor());
            }

            @Override
            public int getPuntosGanador() {
                return resultado.getPuntosGanador();
            }

            @Override
            public int getPuntosPerdedor() {
                return resultado.getPuntosPerdedor();
            }

            @Override
            public int getHechizosLanzadosGanador() {
                return resultado.getHechizosLanzadosGanador();
            }
        };
    }
}