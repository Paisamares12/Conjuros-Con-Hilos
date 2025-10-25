package udistrital.avanzada.taller.modelo;

/**
 * Representa un hechizo mágico que puede ser lanzado por un mago durante un
 * duelo. Cada hechizo tiene un nombre y una cantidad de puntos que aporta al
 * mago que lo utiliza.
 *
 * @author Paula
 * @version 1.0
 * @since 2025-10-25
 */
public class Hechizo {

    /**
     * Nombre del hechizo.
     */
    private String nombre;

    /**
     * Puntos que aporta el hechizo (entre 5 y 25).
     */
    private int puntos;

    /**
     * Crea un nuevo hechizo con el nombre y puntos indicados.
     *
     * @param nombre nombre del hechizo
     * @param puntos cantidad de puntos que otorga
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
     * Establece un nuevo nombre para el hechizo.
     *
     * @param nombre nuevo nombre del hechizo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
     * Establece la cantidad de puntos que otorga el hechizo.
     *
     * @param puntos nuevos puntos del hechizo (debe estar entre 5 y 25)
     */
    public void setPuntos(int puntos) {
        this.puntos = puntos;
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
