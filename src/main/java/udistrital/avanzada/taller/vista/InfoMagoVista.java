/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.vista;

/**
 * Interfaz que representa la información de un mago para la vista.
 * Desacopla la vista del modelo siguiendo el patrón MVC.
 * 
 * @author Juan Estevan Ariza Ortiz
 * @version 6.0
 * @since 2025-10-31
 */
public interface InfoMagoVista {
    /**
     * Obtiene el nombre del mago.
     * @return nombre del mago
     */
    String getNombre();
    
    /**
     * Obtiene la casa del mago.
     * @return casa del mago
     */
    String getCasa();
}
