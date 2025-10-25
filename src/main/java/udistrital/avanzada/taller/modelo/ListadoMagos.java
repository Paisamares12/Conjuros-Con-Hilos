package udistrital.avanzada.taller.modelo;

import java.util.List;

/**
 * Clase de modelo que almacena los magos participantes del torneo.
 * <p>
 * Se limita a mantener la lista de magos, sin incluir reglas de enfrentamiento
 * ni lógica de selección de duelos. Es utilizada por los servicios del torneo
 * para organizar las rondas.
 * </p>
 *
 * @author Paula
 * @version 1.0
 * @since 2025-10-25
 */
public class ListadoMagos {

    /** Lista de magos participantes. */
    private List<Mago> magos;

    /** @return lista de magos registrados */
    public List<Mago> getMagos() {
        return magos;
    }

    /** @param magos nueva lista de magos */
    public void setMagos(List<Mago> magos) {
        this.magos = magos;
    }

    /** @return texto descriptivo del listado de magos */
    @Override
    public String toString() {
        return "ListadoMagos{" + "magos=" + magos + '}';
    }
}
