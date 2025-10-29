package udistrital.avanzada.taller.modelo;

/**
 * Clase de modelo que almacena el resultado final de un duelo entre dos magos.
 * <p>
 * Esta clase funciona como un contenedor de datos, pero incluye validaciones
 * básicas para garantizar la coherencia del resultado (por ejemplo, valores
 * nulos o negativos). La lógica que determina el ganador pertenece a la capa
 * de servicios o motor del juego.
 * </p>
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
public class ResultadoDuelo {

    /** Mago que ganó el duelo. */
    private Mago ganador;

    /** Mago que perdió el duelo. */
    private Mago perdedor;

    /** Puntos obtenidos por el mago ganador. */
    private int puntosGanador;

    /** Puntos obtenidos por el mago perdedor. */
    private int puntosPerdedor;

    /** Cantidad de hechizos lanzados por el ganador. */
    private int hechizosLanzadosGanador;

    /**
     * Constructor vacío por defecto.
     * <p>
     * Se utiliza para crear el objeto antes de asignar sus valores.
     * </p>
     */
    public ResultadoDuelo() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param ganador mago ganador (no nulo)
     * @param perdedor mago perdedor (no nulo y distinto al ganador)
     * @param puntosGanador puntos del ganador (no negativos)
     * @param puntosPerdedor puntos del perdedor (no negativos)
     * @param hechizosLanzadosGanador cantidad de hechizos lanzados por el ganador (no negativos)
     * @throws IllegalArgumentException si algún argumento es inválido
     */
    public ResultadoDuelo(Mago ganador, Mago perdedor,int puntosGanador, int puntosPerdedor,int hechizosLanzadosGanador) {
        setGanador(ganador);
        setPerdedor(perdedor);
        setPuntosGanador(puntosGanador);
        setPuntosPerdedor(puntosPerdedor);
        setHechizosLanzadosGanador(hechizosLanzadosGanador);
    }

    /** @return mago ganador del duelo */
    public Mago getGanador() {
        return ganador;
    }

    /**
     * Asigna el mago ganador.
     *
     * @param ganador mago ganador (no nulo)
     * @throws IllegalArgumentException si es nulo
     */
    public void setGanador(Mago ganador) {
        this.ganador = ganador;
    }

    /** @return mago perdedor del duelo */
    public Mago getPerdedor() {
        return perdedor;
    }

    /**
     * Asigna el mago perdedor.
     *
     * @param perdedor mago perdedor (no nulo)
     * @throws IllegalArgumentException si es nulo o igual al ganador
     */
    public void setPerdedor(Mago perdedor) {
        this.perdedor = perdedor;
    }

    /** @return puntos obtenidos por el ganador */
    public int getPuntosGanador() {
        return puntosGanador;
    }

    /**
     * Asigna los puntos del ganador.
     *
     * @param puntosGanador puntos del ganador (>= 0)
     * @throws IllegalArgumentException si el valor es negativo
     */
    public void setPuntosGanador(int puntosGanador) {
        this.puntosGanador = puntosGanador;
    }

    /** @return puntos obtenidos por el perdedor */
    public int getPuntosPerdedor() {
        return puntosPerdedor;
    }

    /**
     * Asigna los puntos del perdedor.
     *
     * @param puntosPerdedor puntos del perdedor (>= 0)
     * @throws IllegalArgumentException si el valor es negativo
     */
    public void setPuntosPerdedor(int puntosPerdedor) {
        this.puntosPerdedor = puntosPerdedor;
    }

    /** @return cantidad de hechizos lanzados por el ganador */
    public int getHechizosLanzadosGanador() {
        return hechizosLanzadosGanador;
    }

    /**
     * Asigna la cantidad de hechizos lanzados por el ganador.
     *
     * @param hechizosLanzadosGanador número de hechizos (>= 0)
     * @throws IllegalArgumentException si el valor es negativo
     */
    public void setHechizosLanzadosGanador(int hechizosLanzadosGanador) {
        this.hechizosLanzadosGanador = hechizosLanzadosGanador;
    }

    
    //TODO: No estoy seguro de si se pueda usar el toString
    /**
     * Devuelve una representación textual del resultado del duelo.
     * Uso de toString
     * <p>
     * Ejemplo:
     * <pre>
     * Ganador: Harry (Gryffindor)
     * Perdedor: Draco (Slytherin)
     * Puntos: 80 - 60
     * Hechizos lanzados por el ganador: 4
     * </pre>
     * </p>
     *
     * @return texto legible del resultado
     */
    @Override
    public String toString() {
        String nombreG = (ganador != null)
                ? ganador.getNombre() + " (" + ganador.getCasa() + ")"
                : "N/A";
        String nombreP = (perdedor != null)
                ? perdedor.getNombre() + " (" + perdedor.getCasa() + ")"
                : "N/A";

        return "Ganador: " + nombreG + "\n"
                + "Perdedor: " + nombreP + "\n"
                + "Puntos: " + puntosGanador + " - " + puntosPerdedor + "\n"
                + "Hechizos lanzados por el ganador: " + hechizosLanzadosGanador;
    }
}
