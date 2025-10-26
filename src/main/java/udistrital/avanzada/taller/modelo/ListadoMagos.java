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
 * <p>
 * Originalmente creada por Paula Martínez.<br>
 * Modificada por Juan Sebastián Bravo Rojas
 * </p>
 * 
 * @author Paula
 * @version 2.0
 * @since 2025-10-26
 */
public class ListadoMagos {

    /** Lista de magos participantes. */
    private List<Mago> magos;

    /** @return lista de magos registrados */
    public List<Mago> getMagos() {
        return List.copyOf(magos);
    }


    /** @param magos nueva lista de magos */
    public void setMagos(List<Mago> magos) {
        if (magos == null) {
            throw new IllegalArgumentException("La lista de magos no puede ser nula.");
        }
        this.magos = magos;
    }


    /** @return texto descriptivo del listado de magos */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Listado de Magos:\n");
        magos.forEach(m -> sb.append("- ").append(m).append("\n"));
        return sb.toString();
    }

}
