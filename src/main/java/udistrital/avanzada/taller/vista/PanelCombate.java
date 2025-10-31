/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel que muestra visualmente el desarrollo de un duelo mágico en tiempo real.
 * <p>
 * RESPETA MVC: No tiene dependencias con el modelo, solo usa interfaces de vista.
 * Incluye imágenes de magos, animación de lanzamiento de hechizos con efectos visuales
 * y un panel central que muestra la dirección del hechizo lanzado.
 * </p>
 *
 * @author Juan Estevan Ariza Ortiz
 * @version 6.2 - Con panel direccional de hechizos
 * @since 2025-10-31
 */
public class PanelCombate extends PanelBase {

    private Image imagenMago1;
    private Image imagenMago2;
    
    private JLabel lblNombreMago1;
    private JLabel lblCasaMago1;
    private JLabel lblPuntosMago1;
    private JLabel lblHechizoMago1;
    private JLabel lblEstadoMago1;
    private JProgressBar progressMago1;
    private PanelImagenMago panelImagenMago1;

    private JLabel lblNombreMago2;
    private JLabel lblCasaMago2;
    private JLabel lblPuntosMago2;
    private JLabel lblHechizoMago2;
    private JLabel lblEstadoMago2;
    private JProgressBar progressMago2;
    private PanelImagenMago panelImagenMago2;

    private JLabel lblVersus;
    private PanelDireccionHechizo panelDireccion;
    private JTextArea txtLog;
    private JScrollPane scrollLog;
    private JButton btnVolver;

    private Timer timerAnimacionMago1;
    private Timer timerAnimacionMago2;
    private int valorObjetivoMago1 = 0;
    private int valorObjetivoMago2 = 0;

    private static final int META_PUNTOS = 250;
    private static final Color COLOR_GRYFFINDOR = new Color(116, 0, 1);
    private static final Color COLOR_SLYTHERIN = new Color(26, 71, 42);
    private static final Color COLOR_HUFFLEPUFF = new Color(236, 179, 25);
    private static final Color COLOR_RAVENCLAW = new Color(14, 26, 80);

    /**
     * Constructor que inicializa el panel de combate.
     */
    public PanelCombate() {
        cargarImagenesMagos();
        initComponents();
        inicializarTimers();
    }

    /**
     * Carga las imágenes de los magos desde los recursos.
     */
    private void cargarImagenesMagos() {
        try {
            java.net.URL urlMago1 = getClass().getResource("/Imagenes/Mago1.png");
            if (urlMago1 != null) {
                imagenMago1 = new ImageIcon(urlMago1).getImage();
            }
            
            java.net.URL urlMago2 = getClass().getResource("/Imagenes/Mago2.png");
            if (urlMago2 != null) {
                imagenMago2 = new ImageIcon(urlMago2).getImage();
            }
        } catch (Exception e) {
            imagenMago1 = null;
            imagenMago2 = null;
        }
    }

    /**
     * Inicializa los timers para animación suave de las barras de progreso.
     */
    private void inicializarTimers() {
        timerAnimacionMago1 = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int valorActual = progressMago1.getValue();
                if (valorActual < valorObjetivoMago1) {
                    progressMago1.setValue(Math.min(valorActual + 2, valorObjetivoMago1));
                } else {
                    timerAnimacionMago1.stop();
                }
            }
        });

        timerAnimacionMago2 = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int valorActual = progressMago2.getValue();
                if (valorActual < valorObjetivoMago2) {
                    progressMago2.setValue(Math.min(valorActual + 2, valorObjetivoMago2));
                } else {
                    timerAnimacionMago2.stop();
                }
            }
        });
    }

    /**
     * Inicializa todos los componentes gráficos del panel.
     */
    private void initComponents() {
        setLayout(null);

        // ========== PANEL MAGO 1 (IZQUIERDA) ==========
        JPanel panelMago1 = crearPanelMago();
        panelMago1.setBounds(50, 100, 400, 550);
        add(panelMago1);

        panelImagenMago1 = new PanelImagenMago(imagenMago1, "MAGO 1", new Color(100, 100, 200, 150));
        panelImagenMago1.setBounds(125, 20, 150, 200);
        panelMago1.add(panelImagenMago1);

        lblNombreMago1 = new JLabel("Mago 1", SwingConstants.CENTER);
        lblNombreMago1.setFont(new Font("Monospaced", Font.BOLD, 28));
        lblNombreMago1.setForeground(Color.WHITE);
        lblNombreMago1.setBounds(0, 230, 400, 40);
        panelMago1.add(lblNombreMago1);

        lblCasaMago1 = new JLabel("Casa", SwingConstants.CENTER);
        lblCasaMago1.setFont(new Font("Monospaced", Font.PLAIN, 18));
        lblCasaMago1.setForeground(Color.YELLOW);
        lblCasaMago1.setBounds(0, 270, 400, 30);
        panelMago1.add(lblCasaMago1);

        lblPuntosMago1 = new JLabel("0 / 250 puntos", SwingConstants.CENTER);
        lblPuntosMago1.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblPuntosMago1.setForeground(Color.WHITE);
        lblPuntosMago1.setBounds(0, 320, 400, 40);
        panelMago1.add(lblPuntosMago1);

        progressMago1 = new JProgressBar(0, META_PUNTOS);
        progressMago1.setBounds(50, 370, 300, 30);
        progressMago1.setStringPainted(true);
        progressMago1.setForeground(new Color(0, 200, 0));
        panelMago1.add(progressMago1);

        lblHechizoMago1 = new JLabel("Esperando...", SwingConstants.CENTER);
        lblHechizoMago1.setFont(new Font("Monospaced", Font.ITALIC, 16));
        lblHechizoMago1.setForeground(Color.CYAN);
        lblHechizoMago1.setBounds(0, 420, 400, 30);
        panelMago1.add(lblHechizoMago1);

        lblEstadoMago1 = new JLabel("Normal", SwingConstants.CENTER);
        lblEstadoMago1.setFont(new Font("Monospaced", Font.BOLD, 18));
        lblEstadoMago1.setForeground(Color.GREEN);
        lblEstadoMago1.setBounds(0, 460, 400, 30);
        panelMago1.add(lblEstadoMago1);

        // ========== PANEL MAGO 2 (DERECHA) ==========
        JPanel panelMago2 = crearPanelMago();
        panelMago2.setBounds(750, 100, 400, 550);
        add(panelMago2);

        panelImagenMago2 = new PanelImagenMago(imagenMago2, "MAGO 2", new Color(200, 100, 100, 150));
        panelImagenMago2.setBounds(125, 20, 150, 200);
        panelMago2.add(panelImagenMago2);

        lblNombreMago2 = new JLabel("Mago 2", SwingConstants.CENTER);
        lblNombreMago2.setFont(new Font("Monospaced", Font.BOLD, 28));
        lblNombreMago2.setForeground(Color.WHITE);
        lblNombreMago2.setBounds(0, 230, 400, 40);
        panelMago2.add(lblNombreMago2);

        lblCasaMago2 = new JLabel("Casa", SwingConstants.CENTER);
        lblCasaMago2.setFont(new Font("Monospaced", Font.PLAIN, 18));
        lblCasaMago2.setForeground(Color.YELLOW);
        lblCasaMago2.setBounds(0, 270, 400, 30);
        panelMago2.add(lblCasaMago2);

        lblPuntosMago2 = new JLabel("0 / 250 puntos", SwingConstants.CENTER);
        lblPuntosMago2.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblPuntosMago2.setForeground(Color.WHITE);
        lblPuntosMago2.setBounds(0, 320, 400, 40);
        panelMago2.add(lblPuntosMago2);

        progressMago2 = new JProgressBar(0, META_PUNTOS);
        progressMago2.setBounds(50, 370, 300, 30);
        progressMago2.setStringPainted(true);
        progressMago2.setForeground(new Color(0, 200, 0));
        panelMago2.add(progressMago2);

        lblHechizoMago2 = new JLabel("Esperando...", SwingConstants.CENTER);
        lblHechizoMago2.setFont(new Font("Monospaced", Font.ITALIC, 16));
        lblHechizoMago2.setForeground(Color.CYAN);
        lblHechizoMago2.setBounds(0, 420, 400, 30);
        panelMago2.add(lblHechizoMago2);

        lblEstadoMago2 = new JLabel("Normal", SwingConstants.CENTER);
        lblEstadoMago2.setFont(new Font("Monospaced", Font.BOLD, 18));
        lblEstadoMago2.setForeground(Color.GREEN);
        lblEstadoMago2.setBounds(0, 460, 400, 30);
        panelMago2.add(lblEstadoMago2);

        // ========== PANEL CENTRAL ==========
        // Panel de dirección de hechizos
        panelDireccion = new PanelDireccionHechizo();
        panelDireccion.setBounds(465, 250, 270, 150);
        add(panelDireccion);

        // VS label en el centro
        lblVersus = new JLabel("VS", SwingConstants.CENTER);
        lblVersus.setFont(new Font("Monospaced", Font.BOLD, 48));
        lblVersus.setForeground(Color.RED);
        lblVersus.setBounds(500, 180, 200, 60);
        add(lblVersus);

        // ========== LOG Y BOTONES ==========
        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtLog.setBackground(new Color(30, 30, 30));
        txtLog.setForeground(Color.WHITE);
        scrollLog = new JScrollPane(txtLog);
        scrollLog.setBounds(50, 670, 1100, 100);
        add(scrollLog);

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
     *
     * @return panel configurado para mostrar información de un mago
     */
    private JPanel crearPanelMago() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
                g2.setColor(Color.BLACK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        return panel;
    }

    /**
     * Inicializa el panel con la información de los magos participantes.
     *
     * @param mago1 información del primer mago
     * @param mago2 información del segundo mago
     */
    public void inicializarDuelo(InfoMagoVista mago1, InfoMagoVista mago2) {
        lblNombreMago1.setText(mago1.getNombre());
        lblCasaMago1.setText(mago1.getCasa());
        lblCasaMago1.setForeground(obtenerColorCasa(mago1.getCasa()));
        lblPuntosMago1.setText("0 / 250 puntos");
        lblHechizoMago1.setText("Esperando turno...");
        lblEstadoMago1.setText("✓ Normal");
        lblEstadoMago1.setForeground(Color.GREEN);
        progressMago1.setValue(0);
        valorObjetivoMago1 = 0;
        panelImagenMago1.detenerAnimacion();

        lblNombreMago2.setText(mago2.getNombre());
        lblCasaMago2.setText(mago2.getCasa());
        lblCasaMago2.setForeground(obtenerColorCasa(mago2.getCasa()));
        lblPuntosMago2.setText("0 / 250 puntos");
        lblHechizoMago2.setText("Esperando turno...");
        lblEstadoMago2.setText("✓ Normal");
        lblEstadoMago2.setForeground(Color.GREEN);
        progressMago2.setValue(0);
        valorObjetivoMago2 = 0;
        panelImagenMago2.detenerAnimacion();

        panelDireccion.detenerAnimacion();

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
     * @param nombreMago nombre del mago que lanzó el hechizo
     * @param hechizo información del hechizo lanzado
     * @param puntosActuales puntos actuales del mago después de lanzar el hechizo
     */
    public void actualizarMago(String nombreMago, InfoHechizoVista hechizo, int puntosActuales) {
        SwingUtilities.invokeLater(() -> {
            if (esNombreIgual(nombreMago, lblNombreMago1.getText())) {
                lblPuntosMago1.setText(puntosActuales + " / 250 puntos");
                lblHechizoMago1.setText("⚡ " + hechizo.getNombre() + " (+" + hechizo.getPuntos() + ")");
                
                valorObjetivoMago1 = puntosActuales;
                if (!timerAnimacionMago1.isRunning()) {
                    timerAnimacionMago1.start();
                }
                
                panelImagenMago1.animarLanzamientoHechizo();
                panelDireccion.animarHechizo(true, hechizo.getNombre());
                agregarLog(nombreMago + " lanzó " + hechizo.getNombre() + " → " + puntosActuales + " pts");
            } else {
                lblPuntosMago2.setText(puntosActuales + " / 250 puntos");
                lblHechizoMago2.setText("⚡ " + hechizo.getNombre() + " (+" + hechizo.getPuntos() + ")");
                
                valorObjetivoMago2 = puntosActuales;
                if (!timerAnimacionMago2.isRunning()) {
                    timerAnimacionMago2.start();
                }
                
                panelImagenMago2.animarLanzamientoHechizo();
                panelDireccion.animarHechizo(false, hechizo.getNombre());
                agregarLog(nombreMago + " lanzó " + hechizo.getNombre() + " → " + puntosActuales + " pts");
            }
        });
    }

    /**
     * Marca a un mago como aturdido visualmente.
     *
     * @param nombreMago nombre del mago aturdido
     */
    public void marcarAturdido(String nombreMago) {
        SwingUtilities.invokeLater(() -> {
            if (esNombreIgual(nombreMago, lblNombreMago1.getText())) {
                lblEstadoMago1.setText("⚠ ATURDIDO");
                lblEstadoMago1.setForeground(Color.RED);
                agregarLog("¡" + nombreMago + " ha sido ATURDIDO!");
            } else {
                lblEstadoMago2.setText("⚠ ATURDIDO");
                lblEstadoMago2.setForeground(Color.RED);
                agregarLog("¡" + nombreMago + " ha sido ATURDIDO!");
            }
        });
    }

    /**
     * Marca a un mago como recuperado del aturdimiento.
     *
     * @param nombreMago nombre del mago recuperado
     */
    public void marcarRecuperado(String nombreMago) {
        SwingUtilities.invokeLater(() -> {
            if (esNombreIgual(nombreMago, lblNombreMago1.getText())) {
                lblEstadoMago1.setText("✓ Normal");
                lblEstadoMago1.setForeground(Color.GREEN);
                agregarLog(nombreMago + " se ha recuperado del aturdimiento");
            } else {
                lblEstadoMago2.setText("✓ Normal");
                lblEstadoMago2.setForeground(Color.GREEN);
                agregarLog(nombreMago + " se ha recuperado del aturdimiento");
            }
        });
    }

    /**
     * Muestra el resultado final del duelo.
     *
     * @param resultado información del resultado del duelo
     */
    public void mostrarResultado(InfoResultadoDueloVista resultado) {
        SwingUtilities.invokeLater(() -> {
            agregarLog("\n=== FIN DEL DUELO ===");
            agregarLog("🏆 GANADOR: " + resultado.getGanador().getNombre());
            agregarLog("Puntos finales: " + resultado.getPuntosGanador() + " - " + resultado.getPuntosPerdedor());
            agregarLog("Hechizos lanzados por el ganador: " + resultado.getHechizosLanzadosGanador());

            btnVolver.setEnabled(true);
            
            timerAnimacionMago1.stop();
            timerAnimacionMago2.stop();
            panelImagenMago1.detenerAnimacion();
            panelImagenMago2.detenerAnimacion();
            panelDireccion.detenerAnimacion();
        });
    }

    /**
     * Agrega una línea al log de eventos del duelo.
     *
     * @param mensaje mensaje a agregar al log
     */
    private void agregarLog(String mensaje) {
        txtLog.append(mensaje + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    /**
     * Obtiene el color asociado a una casa de Hogwarts.
     *
     * @param casa nombre de la casa
     * @return color representativo de la casa
     */
    private Color obtenerColorCasa(String casa) {
        if (casa == null) return Color.WHITE;
        return switch (casa.toLowerCase()) {
            case "gryffindor" -> COLOR_GRYFFINDOR;
            case "slytherin" -> COLOR_SLYTHERIN;
            case "hufflepuff" -> COLOR_HUFFLEPUFF;
            case "ravenclaw" -> COLOR_RAVENCLAW;
            default -> Color.WHITE;
        };
    }

    /**
     * Compara dos nombres ignorando diferencias de mayúsculas/minúsculas.
     *
     * @param nombre1 primer nombre
     * @param nombre2 segundo nombre
     * @return true si los nombres son iguales
     */
    private boolean esNombreIgual(String nombre1, String nombre2) {
        if (nombre1 == null || nombre2 == null) return false;
        return nombre1.trim().equalsIgnoreCase(nombre2.trim());
    }

    /**
     * Obtiene el botón de volver al menú.
     *
     * @return botón de volver
     */
    public JButton getBotonVolver() {
        return btnVolver;
    }

    /**
     * Limpia el panel para prepararlo para un nuevo duelo.
     */
    public void limpiar() {
        lblNombreMago1.setText("Mago 1");
        lblNombreMago2.setText("Mago 2");
        lblPuntosMago1.setText("0 / 250 puntos");
        lblPuntosMago2.setText("0 / 250 puntos");
        progressMago1.setValue(0);
        progressMago2.setValue(0);
        valorObjetivoMago1 = 0;
        valorObjetivoMago2 = 0;
        txtLog.setText("");
        btnVolver.setEnabled(false);
        timerAnimacionMago1.stop();
        timerAnimacionMago2.stop();
        panelImagenMago1.detenerAnimacion();
        panelImagenMago2.detenerAnimacion();
        panelDireccion.detenerAnimacion();
    }

    /**
     * Panel que visualiza la dirección del hechizo lanzado entre los magos.
     * <p>
     * Muestra flechas animadas que indican la dirección del hechizo:
     * - De izquierda a derecha cuando ataca el Mago 1
     * - De derecha a izquierda cuando ataca el Mago 2
     * Incluye el nombre del hechizo y efectos visuales de movimiento.
     * </p>
     */
        /**
     * Panel que visualiza la dirección del hechizo lanzado entre los magos.
     * <p>
     * Muestra flechas animadas que indican la dirección del hechizo:
     * - De izquierda a derecha cuando ataca el Mago 1
     * - De derecha a izquierda cuando ataca el Mago 2
     * Incluye el nombre del hechizo y efectos visuales de movimiento y energía.
     * </p>
     */
    private class PanelDireccionHechizo extends JPanel {
        private Timer animacionTimer;
        private int frameAnimacion = 0;
        private boolean animando = false;
        private boolean direccionIzquierda = true;
        private String nombreHechizo = "";
        private int offset = 0;

        public PanelDireccionHechizo() {
            setOpaque(false);
            animacionTimer = new Timer(40, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (animando) {
                        frameAnimacion++;
                        offset = (frameAnimacion * 8) % 60;
                        repaint();
                        if (frameAnimacion >= 50) detenerAnimacion();
                    }
                }
            });
        }

        /**
         * Inicia la animación del hechizo en una dirección específica.
         *
         * @param desdeMago1 true si el hechizo va del Mago 1 al Mago 2, false si es al revés
         * @param nombreHechizo nombre del hechizo lanzado
         */
        public void animarHechizo(boolean desdeMago1, String nombreHechizo) {
            this.direccionIzquierda = desdeMago1;
            this.nombreHechizo = nombreHechizo;
            this.animando = true;
            this.frameAnimacion = 0;
            this.offset = 0;
            if (!animacionTimer.isRunning()) animacionTimer.start();
        }

        /** Detiene la animación del hechizo. */
        public void detenerAnimacion() {
            animando = false;
            frameAnimacion = 0;
            offset = 0;
            nombreHechizo = "";
            animacionTimer.stop();
            repaint();
        }

        /** Dibuja una flecha direccional. */
        private void dibujarFlecha(Graphics2D g2d, int x, int y, int largo, Color color, boolean derecha) {
            int anchoCabeza = 12;
            int altoCabeza = 8;
            g2d.setColor(color);
            if (derecha) {
                g2d.fillRect(x, y - 2, largo, 4);
                int[] xs = {x + largo, x + largo + anchoCabeza, x + largo};
                int[] ys = {y - altoCabeza, y, y + altoCabeza};
                g2d.fillPolygon(xs, ys, 3);
            } else {
                g2d.fillRect(x, y - 2, largo, 4);
                int[] xs = {x, x - anchoCabeza, x};
                int[] ys = {y - altoCabeza, y, y + altoCabeza};
                g2d.fillPolygon(xs, ys, 3);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!animando) return;
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int centerY = getHeight() / 2;
            int width = getWidth();
            float alpha = (float) (0.5 + 0.5 * Math.sin(frameAnimacion * 0.2));
            Color colorHechizo = new Color(255, 230, 100, (int)(alpha * 255));

            if (direccionIzquierda) {
                dibujarFlecha(g2d, 10 + offset, centerY - 20, 150, colorHechizo, true);
                dibujarFlecha(g2d, 40 + offset, centerY, 150, colorHechizo, true);
                dibujarFlecha(g2d, 70 + offset, centerY + 20, 150, colorHechizo, true);
            } else {
                dibujarFlecha(g2d, width - 160 - offset, centerY - 20, 150, colorHechizo, false);
                dibujarFlecha(g2d, width - 190 - offset, centerY, 150, colorHechizo, false);
                dibujarFlecha(g2d, width - 220 - offset, centerY + 20, 150, colorHechizo, false);
            }

            g2d.setFont(new Font("Monospaced", Font.BOLD, 16));
            g2d.setColor(new Color(255, 255, 255, (int)(alpha * 255)));
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (width - fm.stringWidth(nombreHechizo)) / 2;
            int textY = centerY + 45;
            g2d.drawString(nombreHechizo, textX, textY);
            g2d.dispose();
        }
    }

    /**
     * Panel que muestra la imagen de un mago con animaciones visuales.
     * Incluye un efecto de brillo al lanzar hechizos.
     */
    private class PanelImagenMago extends JPanel {
        private Image imagen;
        private String textoPlaceholder;
        private Color colorPlaceholder;
        private Timer animacionTimer;
        private boolean animando = false;
        private float intensidadBrillo = 0f;

        public PanelImagenMago(Image imagen, String textoPlaceholder, Color colorPlaceholder) {
            this.imagen = imagen;
            this.textoPlaceholder = textoPlaceholder;
            this.colorPlaceholder = colorPlaceholder;
            setOpaque(false);

            animacionTimer = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (animando) {
                        intensidadBrillo = (float) Math.abs(Math.sin(System.currentTimeMillis() * 0.005));
                        repaint();
                    }
                }
            });
        }

        /** Inicia la animación de lanzamiento del hechizo. */
        public void animarLanzamientoHechizo() {
            animando = true;
            if (!animacionTimer.isRunning()) animacionTimer.start();
            new Timer(2000, e -> detenerAnimacion()).start();
        }

        /** Detiene cualquier animación activa. */
        public void detenerAnimacion() {
            animando = false;
            intensidadBrillo = 0f;
            animacionTimer.stop();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (imagen != null)
                g2d.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            else {
                g2d.setColor(colorPlaceholder);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Monospaced", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(textoPlaceholder)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2d.drawString(textoPlaceholder, x, y);
            }

            if (animando && intensidadBrillo > 0) {
                Color brillo = new Color(255, 255, 100, (int)(intensidadBrillo * 120));
                g2d.setColor(brillo);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }

            g2d.dispose();
        }
    }
}
