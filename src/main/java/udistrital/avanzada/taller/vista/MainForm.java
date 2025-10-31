/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.vista;

import java.awt.CardLayout;

/**
 * Clase {@code MainForm} representa el panel principal de la aplicación.
 * <p>
 * Este panel funciona como un contenedor que administra las diferentes vistas
 * del programa (Inicio, Cargar, Combate) utilizando un {@link CardLayout}.
 * </p>
 *
 * @author Paula Martínez
 * @version 6.0
 * @since 2025-10-31
 */
public class MainForm extends javax.swing.JPanel {

    private PanelInicio inicio;
    private PanelCargar cargar;
    private PanelCombate combate;
    private CardLayout layout;

    /**
     * Constructor de la clase {@code MainForm}.
     * Inicializa el layout principal y todos los subpaneles.
     */
    public MainForm() {
        layout = new CardLayout();
        setLayout(layout);
        initPanels();
        mostrarPanelInicio();
    }

    /**
     * Inicializa los diferentes subpaneles del formulario principal.
     */
    private void initPanels() {
        inicio = new PanelInicio();
        cargar = new PanelCargar();
        combate = new PanelCombate();

        add(inicio, "Inicio");
        add(cargar, "Cargar");
        add(combate, "Combate");
    }

    // ========== MÉTODOS DE NAVEGACIÓN ==========

    /**
     * Muestra el panel de inicio.
     */
    public void mostrarPanelInicio() {
        layout.show(this, "Inicio");
    }

    /**
     * Muestra el panel de carga de propiedades.
     */
    public void mostrarPanelCargar() {
        layout.show(this, "Cargar");
    }

    /**
     * Muestra el panel de combate.
     */
    public void mostrarPanelCombate() {
        layout.show(this, "Combate");
    }

    // ========== GETTERS ==========

    /**
     * Obtiene el panel de inicio.
     *
     * @return instancia de {@link PanelInicio}
     */
    public PanelInicio getPanelInicio() {
        return inicio;
    }

    /**
     * Obtiene el panel de cargar.
     *
     * @return instancia de {@link PanelCargar}
     */
    public PanelCargar getPanelCargar() {
        return cargar;
    }

    /**
     * Obtiene el panel de combate.
     *
     * @return instancia de {@link PanelCombate}
     */
    public PanelCombate getPanelCombate() {
        return combate;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
    }// </editor-fold>
}
