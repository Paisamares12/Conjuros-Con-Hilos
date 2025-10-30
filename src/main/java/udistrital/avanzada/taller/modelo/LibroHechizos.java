package udistrital.avanzada.taller.modelo;

import java.util.List;

/**
 * Clase de modelo que agrupa los hechizos disponibles del torneo.
 * <p>
 * No contiene lógica de selección ni aleatoriedad; solo mantiene una lista de
 * objetos {@link Hechizo}. La lógica para seleccionar o usar hechizos
 * corresponde al motor del juego.
 * </p>
 *
 * <p>
 * Originalmente creada por Paula Martínez.<br>
 * Modificada por Juan Sebastián Bravo Rojas
 * </p>
 * 
 * @author Paula Martínez
 * @version 5.0
 * @since 2025-10-26
 */
public class LibroHechizos {

    /**
     * Lista de todos los hechizos disponibles.
     */
    private List<Hechizo> hechizos;

    /**
     * @return lista completa de hechizos
     */
    public List<Hechizo> getHechizos() {
        return List.copyOf(hechizos);
    }


    /**
     * @param hechizos lista de hechizos a asignar
     */
    public void setHechizos(List<Hechizo> hechizos) {
        this.hechizos = hechizos;
    }

}
