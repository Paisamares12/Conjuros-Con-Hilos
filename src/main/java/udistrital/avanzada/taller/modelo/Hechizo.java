package udistrital.avanzada.taller.modelo;

/**
 * Representa un hechizo mágico que puede ser lanzado por un mago durante un
 * duelo. Cada hechizo tiene un nombre y una cantidad de puntos que aporta al
 * mago que lo utiliza.
 *
 * <p>
 * Originalmente creada por Paula Martínez.<br>
 * Modificada por Juan Sebastián Bravo Rojas
 * </p>
 * 
 * @author Paula
 * @version 2.0
 * @since 2025-10-26
 */
public class Hechizo {

    /**
     * Nombre del hechizo.
     */
    private final String nombre;

    /**
     * Puntos que aporta el hechizo (entre 5 y 25).
     */
    private final int puntos;

    /**
     * Crea un nuevo hechizo con el nombre y puntos indicados.
     *
     * @param nombre nombre del hechizo
     * @param puntos cantidad de puntos que otorga
     * @throws IllegalArgumentException si los puntos están fuera del rango permitido
     */
    public Hechizo(String nombre, int puntos) {
        this.nombre = nombre;
        this.puntos = puntos;
    }

    /**
     * Obtiene el nombre del hechizo.
     *
     * @return nombre del hechizo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve los puntos que otorga este hechizo.
     *
     * @return cantidad de puntos del hechizo
     */
    public int getPuntos() {
        return puntos;
    }

    /**
     * Devuelve una representación en texto del hechizo.
     * <p>
     * El formato es: {@code "Nombre (X pts)"}.
     * </p>
     *
     * @return descripción legible del hechizo
     */
    @Override
    public String toString() {
        return nombre + " (" + puntos + " pts)";
    }
}
