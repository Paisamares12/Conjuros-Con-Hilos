package udistrital.avanzada.taller.modelo;

import java.util.ArrayList;
import java.util.Collections;
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
 * @author Paula Martínez
 * @version 5.0
 * @since 2025-10-26
 */
public class ListadoMagos {

    /** Lista de magos participantes. */
    private List<Mago> magos = new ArrayList<>(); //inicializa vacía por defecto

    /** @return lista de magos registrados (nunca null) */
    public List<Mago> getMagos() {
        // si aún no se asignó o es null, devuelve lista vacía
        return magos == null ? Collections.emptyList() : List.copyOf(magos);
    }


    /** @param magos nueva lista de magos */
    public void setMagos(List<Mago> magos) {
        this.magos = magos;
    }

}
