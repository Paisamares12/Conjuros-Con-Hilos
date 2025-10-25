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
 * @author Paula
 * @version 1.1
 * @since 2025-10-25
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
        return hechizos;
    }

    /**
     * @param hechizos lista de hechizos a asignar
     */
    public void setHechizos(List<Hechizo> hechizos) {
        this.hechizos = hechizos;
    }

    /**
     * @return texto descriptivo del libro de hechizos
     */
    @Override
    public String toString() {
        return "LibroHechizos{" + "hechizos=" + hechizos + '}';
    }
}
