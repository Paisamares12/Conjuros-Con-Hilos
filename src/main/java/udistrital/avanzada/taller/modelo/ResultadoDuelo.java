package udistrital.avanzada.taller.modelo;

/**
 * Clase de modelo que almacena el resultado final de un duelo entre dos magos.
 * <p>
 * Esta clase es un contenedor de datos; no contiene la lógica que determina el
 * ganador ni los cálculos de puntuación, los cuales son realizados en la capa
 * de motor o servicios.
 * </p>
 *
 * @author Paula
 * @version 1.0
 * @since 2025-10-25
 */
public class ResultadoDuelo {

    private Mago ganador;
    private Mago perdedor;
    private int puntosGanador;
    private int puntosPerdedor;
    private int hechizosLanzadosGanador;

    /**
     * Constructor vacío por defecto.
     */
    public ResultadoDuelo() {
    }

    /**
     * @return mago ganador del duelo
     */
    public Mago getGanador() {
        return ganador;
    }

    /**
     * @param ganador mago ganador del duelo
     */
    public void setGanador(Mago ganador) {
        this.ganador = ganador;
    }

    /**
     * @return mago perdedor del duelo
     */
    public Mago getPerdedor() {
        return perdedor;
    }

    /**
     * @param perdedor mago perdedor del duelo
     */
    public void setPerdedor(Mago perdedor) {
        this.perdedor = perdedor;
    }

    /**
     * @return puntos obtenidos por el ganador
     */
    public int getPuntosGanador() {
        return puntosGanador;
    }

    /**
     * @param puntosGanador puntos del ganador
     */
    public void setPuntosGanador(int puntosGanador) {
        this.puntosGanador = puntosGanador;
    }

    /**
     * @return puntos obtenidos por el perdedor
     */
    public int getPuntosPerdedor() {
        return puntosPerdedor;
    }

    /**
     * @param puntosPerdedor puntos del perdedor
     */
    public void setPuntosPerdedor(int puntosPerdedor) {
        this.puntosPerdedor = puntosPerdedor;
    }

    /**
     * @return cantidad de hechizos lanzados por el ganador
     */
    public int getHechizosLanzadosGanador() {
        return hechizosLanzadosGanador;
    }

    /**
     * @param hechizosLanzadosGanador cantidad de hechizos lanzados
     */
    public void setHechizosLanzadosGanador(int hechizosLanzadosGanador) {
        this.hechizosLanzadosGanador = hechizosLanzadosGanador;
    }

    /**
     * @return representación textual del resultado del duelo
     */
    @Override
    public String toString() {
        return "ResultadoDuelo{"
                + "ganador=" + ganador
                + ", perdedor=" + perdedor
                + ", puntosGanador=" + puntosGanador
                + ", puntosPerdedor=" + puntosPerdedor
                + ", hechizosLanzadosGanador=" + hechizosLanzadosGanador
                + '}';
    }
}
