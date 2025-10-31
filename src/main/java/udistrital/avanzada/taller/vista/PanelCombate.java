/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.vista;

import javax.swing.*;
import java.awt.*;
import udistrital.avanzada.taller.modelo.*;

/**
 * Panel que muestra visualmente el desarrollo de un duelo mágico en tiempo
 * real.
 * <p>
 * Este panel presenta información dinámica sobre ambos magos participantes,
 * incluyendo puntos acumulados, hechizos lanzados, estado de aturdimiento, y
 * barras de progreso visuales.
 *
 * Creada por Juan Ariza modificada por Paula Martínez
 * </p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 6.0
 * @since 2025-10-29
 */

//TODO: Sacar todo el modelo de aca, la Vista no se puede comunicar directamente con el modelo
//TODO: revisar que cumpla con el MVC y los SOLID 
public class PanelCombate extends PanelBase {

    // Componentes del Mago 1
    private JLabel lblNombreMago1;
    private JLabel lblCasaMago1;
    private JLabel lblPuntosMago1;
    private JLabel lblHechizoMago1;
    private JLabel lblEstadoMago1;
    private JProgressBar progressMago1;

    // Componentes del Mago 2
    private JLabel lblNombreMago2;
    private JLabel lblCasaMago2;
    private JLabel lblPuntosMago2;
    private JLabel lblHechizoMago2;
    private JLabel lblEstadoMago2;
    private JProgressBar progressMago2;

    // Componentes centrales
    private JLabel lblVersus;
    private JTextArea txtLog;
    private JScrollPane scrollLog;
    private JButton btnVolver;

    private static final int META_PUNTOS = 250;
    private static final Color COLOR_GRYFFINDOR = new Color(116, 0, 1);
    private static final Color COLOR_SLYTHERIN = new Color(26, 71, 42);
    private static final Color COLOR_HUFFLEPUFF = new Color(236, 179, 25);
    private static final Color COLOR_RAVENCLAW = new Color(14, 26, 80);

    /**
     * Constructor que inicializa el panel de combate.
     */
    public PanelCombate() {
        initComponents();
    }

    /**
     * Inicializa todos los componentes gráficos del panel.
     */
    private void initComponents() {
        setLayout(null);

        // ========== PANEL MAGO 1 (IZQUIERDA) ==========
        JPanel panelMago1 = crearPanelMago();
        panelMago1.setBounds(50, 100, 400, 400);
        add(panelMago1);

        lblNombreMago1 = new JLabel("Mago 1", SwingConstants.CENTER);
        lblNombreMago1.setFont(new Font("Monospaced", Font.BOLD, 28));
        lblNombreMago1.setForeground(Color.WHITE);
        lblNombreMago1.setBounds(0, 20, 400, 40);
        panelMago1.add(lblNombreMago1);

        lblCasaMago1 = new JLabel("Casa", SwingConstants.CENTER);
        lblCasaMago1.setFont(new Font("Monospaced", Font.PLAIN, 18));
        lblCasaMago1.setForeground(Color.YELLOW);
        lblCasaMago1.setBounds(0, 60, 400, 30);
        panelMago1.add(lblCasaMago1);

        lblPuntosMago1 = new JLabel("0 / 250 puntos", SwingConstants.CENTER);
        lblPuntosMago1.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblPuntosMago1.setForeground(Color.WHITE);
        lblPuntosMago1.setBounds(0, 120, 400, 40);
        panelMago1.add(lblPuntosMago1);

        progressMago1 = new JProgressBar(0, META_PUNTOS);
        progressMago1.setBounds(50, 170, 300, 30);
        progressMago1.setStringPainted(true);
        progressMago1.setForeground(new Color(0, 200, 0));
        panelMago1.add(progressMago1);

        lblHechizoMago1 = new JLabel("Esperando...", SwingConstants.CENTER);
        lblHechizoMago1.setFont(new Font("Monospaced", Font.ITALIC, 16));
        lblHechizoMago1.setForeground(Color.CYAN);
        lblHechizoMago1.setBounds(0, 220, 400, 30);
        panelMago1.add(lblHechizoMago1);

        lblEstadoMago1 = new JLabel("Normal", SwingConstants.CENTER);
        lblEstadoMago1.setFont(new Font("Monospaced", Font.BOLD, 18));
        lblEstadoMago1.setForeground(Color.GREEN);
        lblEstadoMago1.setBounds(0, 260, 400, 30);
        panelMago1.add(lblEstadoMago1);

        // ========== PANEL MAGO 2 (DERECHA) ==========
        JPanel panelMago2 = crearPanelMago();
        panelMago2.setBounds(750, 100, 400, 400);
        add(panelMago2);

        lblNombreMago2 = new JLabel("Mago 2", SwingConstants.CENTER);
        lblNombreMago2.setFont(new Font("Monospaced", Font.BOLD, 28));
        lblNombreMago2.setForeground(Color.WHITE);
        lblNombreMago2.setBounds(0, 20, 400, 40);
        panelMago2.add(lblNombreMago2);

        lblCasaMago2 = new JLabel("Casa", SwingConstants.CENTER);
        lblCasaMago2.setFont(new Font("Monospaced", Font.PLAIN, 18));
        lblCasaMago2.setForeground(Color.YELLOW);
        lblCasaMago2.setBounds(0, 60, 400, 30);
        panelMago2.add(lblCasaMago2);

        lblPuntosMago2 = new JLabel("0 / 250 puntos", SwingConstants.CENTER);
        lblPuntosMago2.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblPuntosMago2.setForeground(Color.WHITE);
        lblPuntosMago2.setBounds(0, 120, 400, 40);
        panelMago2.add(lblPuntosMago2);

        progressMago2 = new JProgressBar(0, META_PUNTOS);
        progressMago2.setBounds(50, 170, 300, 30);
        progressMago2.setStringPainted(true);
        progressMago2.setForeground(new Color(0, 200, 0));
        panelMago2.add(progressMago2);

        lblHechizoMago2 = new JLabel("Esperando...", SwingConstants.CENTER);
        lblHechizoMago2.setFont(new Font("Monospaced", Font.ITALIC, 16));
        lblHechizoMago2.setForeground(Color.CYAN);
        lblHechizoMago2.setBounds(0, 220, 400, 30);
        panelMago2.add(lblHechizoMago2);

        lblEstadoMago2 = new JLabel("Normal", SwingConstants.CENTER);
        lblEstadoMago2.setFont(new Font("Monospaced", Font.BOLD, 18));
        lblEstadoMago2.setForeground(Color.GREEN);
        lblEstadoMago2.setBounds(0, 260, 400, 30);
        panelMago2.add(lblEstadoMago2);

        // ========== COMPONENTES CENTRALES ==========
        lblVersus = new JLabel("VS", SwingConstants.CENTER);
        lblVersus.setFont(new Font("Monospaced", Font.BOLD, 48));
        lblVersus.setForeground(Color.RED);
        lblVersus.setBounds(500, 250, 200, 60);
        add(lblVersus);

        // Log de eventos
        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtLog.setBackground(new Color(30, 30, 30));
        txtLog.setForeground(Color.WHITE);
        scrollLog = new JScrollPane(txtLog);
        scrollLog.setBounds(50, 530, 1100, 180);
        add(scrollLog);

        // Botón volver
        btnVolver = new JButton("VOLVER AL MENÚ");
        btnVolver.setFont(new Font("Monospaced", Font.BOLD, 18));
        btnVolver.setBackground(new Color(132, 72, 62));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBounds(450, 730, 300, 50);
        btnVolver.setEnabled(false);
        add(btnVolver);
    }

    /**
     * Crea un panel semi-transparente para contener la información de un mago.
     */
    private JPanel crearPanelMago() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // limpia
                // (si el padre es no-opaco, asegúrate de que el padre también hace super.paintComponent)
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.6f)); // 60% opacidad
                g2.setColor(Color.BLACK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        panel.setOpaque(false); // el panel se pinta a sí mismo con alpha
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        return panel;
    }

    /**
     * Inicializa el panel con la información de los magos participantes.
     *
     * @param mago1
     * @param mago2
     */
    public void inicializarDuelo(Mago mago1, Mago mago2) {
        // Mago 1
        lblNombreMago1.setText(mago1.getNombre());
        lblCasaMago1.setText(mago1.getCasa());
        lblCasaMago1.setForeground(obtenerColorCasa(mago1.getCasa()));
        lblPuntosMago1.setText("0 / 250 puntos");
        lblHechizoMago1.setText("Esperando turno...");
        lblEstadoMago1.setText("✓ Normal");
        lblEstadoMago1.setForeground(Color.GREEN);
        progressMago1.setValue(0);

        // Mago 2
        lblNombreMago2.setText(mago2.getNombre());
        lblCasaMago2.setText(mago2.getCasa());
        lblCasaMago2.setForeground(obtenerColorCasa(mago2.getCasa()));
        lblPuntosMago2.setText("0 / 250 puntos");
        lblHechizoMago2.setText("Esperando turno...");
        lblEstadoMago2.setText("✓ Normal");
        lblEstadoMago2.setForeground(Color.GREEN);
        progressMago2.setValue(0);

        // Log
        txtLog.setText("");
        agregarLog("=== INICIO DEL DUELO ===");
        agregarLog(mago1.getNombre() + " (" + mago1.getCasa() + ") vs "
                + mago2.getNombre() + " (" + mago2.getCasa() + ")");
        agregarLog("¡Que comience el duelo!\n");

        btnVolver.setEnabled(false);
    }

    /**
     * Actualiza la información de un mago después de lanzar un hechizo.
     *
     * @param mago
     * @param hechizo
     * @param puntosActuales
     */
    public void actualizarMago(Mago mago, Hechizo hechizo, int puntosActuales) {
        SwingUtilities.invokeLater(() -> {
            if (esNombreIgual(mago.getNombre(), lblNombreMago1.getText())) {
                lblPuntosMago1.setText(puntosActuales + " / 250 puntos");
                lblHechizoMago1.setText("⚡ " + hechizo.getNombre() + " (+" + hechizo.getPuntos() + ")");
                progressMago1.setValue(puntosActuales);
                agregarLog(mago.getNombre() + " lanzó " + hechizo.getNombre() + " → " + puntosActuales + " pts");
            } else {
                lblPuntosMago2.setText(puntosActuales + " / 250 puntos");
                lblHechizoMago2.setText("⚡ " + hechizo.getNombre() + " (+" + hechizo.getPuntos() + ")");
                progressMago2.setValue(puntosActuales);
                agregarLog(mago.getNombre() + " lanzó " + hechizo.getNombre() + " → " + puntosActuales + " pts");
            }
        });
    }

    /**
     * Marca a un mago como aturdido.
     *
     * @param mago
     */
    public void marcarAturdido(Mago mago) {
        SwingUtilities.invokeLater(() -> {
            if (esNombreIgual(mago.getNombre(), lblNombreMago1.getText())) {
                lblEstadoMago1.setText("⚠ ATURDIDO");
                lblEstadoMago1.setForeground(Color.RED);
                agregarLog("¡" + mago.getNombre() + " ha sido ATURDIDO!");
            } else {
                lblEstadoMago2.setText("⚠ ATURDIDO");
                lblEstadoMago2.setForeground(Color.RED);
                agregarLog("¡" + mago.getNombre() + " ha sido ATURDIDO!");
            }
        });
    }

    /**
     * Marca a un mago como recuperado.
     *
     * @param mago
     */
    public void marcarRecuperado(Mago mago) {
        SwingUtilities.invokeLater(() -> {
            if (esNombreIgual(mago.getNombre(), lblNombreMago1.getText())) {
                lblEstadoMago1.setText("✓ Normal");
                lblEstadoMago1.setForeground(Color.GREEN);
                agregarLog(mago.getNombre() + " se ha recuperado del aturdimiento");
            } else {
                lblEstadoMago2.setText("✓ Normal");
                lblEstadoMago2.setForeground(Color.GREEN);
                agregarLog(mago.getNombre() + " se ha recuperado del aturdimiento");
            }
        });
    }

    /**
     * Muestra el resultado final del duelo.
     *
     * @param resultado
     */
    public void mostrarResultado(ResultadoDuelo resultado) {
        SwingUtilities.invokeLater(() -> {
            agregarLog("\n=== FIN DEL DUELO ===");
            agregarLog("🏆 GANADOR: " + resultado.getGanador().getNombre());
            agregarLog("Puntos finales: " + resultado.getPuntosGanador() + " - " + resultado.getPuntosPerdedor());
            agregarLog("Hechizos lanzados por el ganador: " + resultado.getHechizosLanzadosGanador());

            btnVolver.setEnabled(true);
        });
    }

    /**
     * Agrega una línea al log de eventos.
     */
    private void agregarLog(String mensaje) {
        txtLog.append(mensaje + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    /**
     * Obtiene el color asociado a una casa de Hogwarts.
     */
    private Color obtenerColorCasa(String casa) {
        if (casa == null) {
            return Color.WHITE;
        }

        return switch (casa.toLowerCase()) {
            case "gryffindor" ->
                COLOR_GRYFFINDOR;
            case "slytherin" ->
                COLOR_SLYTHERIN;
            case "hufflepuff" ->
                COLOR_HUFFLEPUFF;
            case "ravenclaw" ->
                COLOR_RAVENCLAW;
            default ->
                Color.WHITE;
        };
    }

    /**
     * Compara nombres ignorando diferencias de mayúsculas/minúsculas.
     */
    private boolean esNombreIgual(String nombre1, String nombre2) {
        if (nombre1 == null || nombre2 == null) {
            return false;
        }
        return nombre1.trim().equalsIgnoreCase(nombre2.trim());
    }

    /**
     * Obtiene el botón de volver.
     *
     * @return
     */
    public JButton getBotonVolver() {
        return btnVolver;
    }

    /**
     * Limpia el panel para un nuevo duelo.
     */
    public void limpiar() {
        lblNombreMago1.setText("Mago 1");
        lblNombreMago2.setText("Mago 2");
        lblPuntosMago1.setText("0 / 250 puntos");
        lblPuntosMago2.setText("0 / 250 puntos");
        progressMago1.setValue(0);
        progressMago2.setValue(0);
        txtLog.setText("");
        btnVolver.setEnabled(false);
    }
}
