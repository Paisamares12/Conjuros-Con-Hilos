package udistrital.avanzada.taller.modelo;

/**
 * Representa a un mago participante del torneo.
 * <p>
 * Cada mago pertenece a una casa, acumula puntos durante los duelos y puede
 * quedar aturdido temporalmente según las reglas de combate.
 * </p>
 *
 * <p>
 * Originalmente creada por Paula Martínez.<br>
 * Modificada por Juan Sebastián Bravo Rojas
 * </p>
 * 
 * @author Paula Martínez
 * @version 6.0
 * @since 2025-10-26
 */
public class Mago {

    /**
     * Nombre del mago.
     */
    private String nombre;

    /**
     * Casa a la que pertenece el mago.
     */
    private String casa;

    /**
     * Puntos acumulados durante el duelo actual.
     */
    private int puntosAcumulados;

    /**
     * Cantidad de hechizos lanzados.
     */
    private int hechizosLanzados;

    /**
     * Indica si el mago está aturdido.
     */
    private boolean aturdido;

    /**
     * Referencia a su contrincante actual.
     */
    private Mago rival;

    /**
     * Constructor vacio para mayor dinamismo
     */
    public Mago() {
    }    
    
    /**
     * Crea un mago con nombre y casa.
     *
     * @param nombre nombre del mago
     * @param casa casa a la que pertenece
     */
    public Mago(String nombre, String casa) {
        this.nombre = nombre;
        this.casa = casa;
        this.puntosAcumulados = 0;
        this.hechizosLanzados = 0;
        this.aturdido = false;
    }

    /**
     * Incrementa los puntos y el contador de hechizos lanzados al ejecutar un
     * hechizo.
     *
     * @param hechizo el hechizo lanzado por el mago
     */
    public void lanzarHechizo(Hechizo hechizo) {
        if (hechizo != null && !aturdido) {
            this.puntosAcumulados += hechizo.getPuntos();
            this.hechizosLanzados++;
        }
    }

    /**
     * Marca al mago como aturdido.
     */
    public void aturdir() {
        this.aturdido = true;
    }

    /**
     * Elimina el estado de aturdimiento.
     */
    public void recuperar() {
        this.aturdido = false;
    }

    /**
     * Reinicia los puntos y hechizos lanzados para un nuevo duelo.
     */
    public void reiniciarPuntaje() {
        this.puntosAcumulados = 0;
        this.hechizosLanzados = 0;
        this.aturdido = false;
    }

    /**
     * Indica si el mago está actualmente aturdido.
     *
     * @return {@code true} si el mago está aturdido, {@code false} en caso
     * contrario
     */
    public boolean estaAturdido() {
        return aturdido;
    }

    /**
     * Obtiene los puntos acumulados por el mago.
     *
     * @return puntos acumulados
     */
    public int getPuntosAcumulados() {
        return puntosAcumulados;
    }

    /**
     * Establece los puntos acumulados del mago.
     *
     * @param puntosAcumulados nuevos puntos del mago
     */
    public void setPuntosAcumulados(int puntosAcumulados) {
        this.puntosAcumulados = puntosAcumulados;
    }


    /**
     * Devuelve la cantidad total de hechizos lanzados por el mago.
     *
     * @return número de hechizos lanzados
     */
    public int getHechizosLanzados() {
        return hechizosLanzados;
    }

    /**
     * Establece manualmente la cantidad de hechizos lanzados.
     *
     * @param hechizosLanzados nueva cantidad de hechizos lanzados
     */
    public void setHechizosLanzados(int hechizosLanzados) {
        this.hechizosLanzados = hechizosLanzados;
    }

    /**
     * Obtiene el nombre del mago.
     *
     * @return nombre del mago
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del mago.
     *
     * @param nombre nuevo nombre del mago
     */
    public void setNombre(String nombre) {
        this.nombre = nombre.trim();
    }


    /**
     * Devuelve la casa a la que pertenece el mago.
     *
     * @return nombre de la casa
     */
    public String getCasa() {
        return casa;
    }

    /**
     * Establece la casa del mago.
     *
     * @param casa nueva casa del mago
     */
    public void setCasa(String casa) {
        this.casa = casa.trim();
    }

    /**
     * Devuelve la referencia al rival actual del mago.
     *
     * @return rival del mago
     */
    public Mago getRival() {
        return rival;
    }

    /**
     * Asigna un nuevo rival al mago.
     *
     * @param rival mago rival
     */
    public void setRival(Mago rival) {
        this.rival = rival;
    }


}
