/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.vista;

/**
 * Interfaz que representa la información de un hechizo para la vista.
 * Desacopla la vista del modelo siguiendo el patrón MVC.
 * 
 * @author Refactorización MVC
 * @version 6.0
 * @since 2025-10-31
 */
public interface InfoHechizoVista {
    /**
     * Obtiene el nombre del hechizo.
     * @return nombre del hechizo
     */
    String getNombre();
    
    /**
     * Obtiene los puntos del hechizo.
     * @return puntos del hechizo
     */
    int getPuntos();
}
