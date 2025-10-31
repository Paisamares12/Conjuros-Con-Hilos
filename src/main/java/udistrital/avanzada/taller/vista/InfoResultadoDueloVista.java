/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.vista;

/**
 * Interfaz que representa la información del resultado de un duelo para la vista.
 * Desacopla la vista del modelo siguiendo el patrón MVC.
 * 
 * @author Refactorización MVC
 * @version 6.0
 * @since 2025-10-31
 */
public interface InfoResultadoDueloVista {
    /**
     * Obtiene la información del mago ganador.
     * @return información del ganador
     */
    InfoMagoVista getGanador();
    
    /**
     * Obtiene la información del mago perdedor.
     * @return información del perdedor
     */
    InfoMagoVista getPerdedor();
    
    /**
     * Obtiene los puntos del ganador.
     * @return puntos del ganador
     */
    int getPuntosGanador();
    
    /**
     * Obtiene los puntos del perdedor.
     * @return puntos del perdedor
     */
    int getPuntosPerdedor();
    
    /**
     * Obtiene la cantidad de hechizos lanzados por el ganador.
     * @return hechizos lanzados
     */
    int getHechizosLanzadosGanador();
}
