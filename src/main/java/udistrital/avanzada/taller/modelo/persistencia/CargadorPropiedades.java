/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.modelo.persistencia;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import udistrital.avanzada.taller.modelo.Hechizo;
import udistrital.avanzada.taller.modelo.LibroHechizos;
import udistrital.avanzada.taller.modelo.ListadoMagos;
import udistrital.avanzada.taller.modelo.Mago;

/**
 *
 * @author paisa
 */
public class CargadorPropiedades {

    /**
     * Ruta relativa al archivo de propiedades de hechizos
     */
    private static final String RUTA_ARCHIVO_HECHIZOS = "src/data/hechizos.properties";
    /**
     * Ruta relativa al archivo de propiedades de magos
     */
    private static final String RUTA_ARCHIVO_MAGOS = "src/data/magos.properties";

    /**
     * Claves comunes.
     */
    private static final String KEY_COUNT = "count";

    /**
     * Carga los magos desde un archivo en disco o desde el classpath.
     * <p>
     * Si {@code rutaSistemaArchivos} es {@code null} o no existe, intentará
     * cargar {@link #RECURSO_MAGOS} desde el classpath.
     * </p>
     *
     * @param rutaSistemaArchivos ruta en el sistema de archivos (puede ser
     * null)
     * @return un {@link ListadoMagos} con los magos cargados (puede venir
     * vacío)
     * @throws IOException si no se encuentra el archivo ni como ruta ni como
     * recurso, o si hay errores de lectura/parsing de propiedades críticas
     */
    public ListadoMagos cargarMagos(String rutaSistemaArchivos) throws IOException {
        Properties props = cargarDesdeRutaOClasspath(rutaSistemaArchivos, RUTA_ARCHIVO_MAGOS);

        int cantidad = leerEnteroObligatorio(props, KEY_COUNT, "magos.properties: count");
        List<Mago> magos = new ArrayList<>(cantidad);

        for (int i = 1; i <= cantidad; i++) {
            String base = "mago." + i + ".";
            String nombre = leerTextoObligatorio(props, base + "nombre", "mago." + i + ".nombre");
            String casa = leerTextoObligatorio(props, base + "casa", "mago." + i + ".casa");

            Mago mago = new Mago();
            mago.setNombre(nombre);
            mago.setCasa(casa);

            magos.add(mago);
        }

        ListadoMagos listado = new ListadoMagos();
        listado.setMagos(magos);
        return listado;
    }

    /**
     * Carga los hechizos desde un archivo en disco o desde el classpath.
     * <p>
     * Si {@code rutaSistemaArchivos} es {@code null} o no existe, intentará
     * cargar {@link #RECURSO_HECHIZOS} desde el classpath.
     * </p>
     *
     * @param rutaSistemaArchivos ruta en el sistema de archivos (puede ser
     * null)
     * @return un {@link LibroHechizos} con los hechizos cargados (puede venir
     * vacío)
     * @throws IOException si no se encuentra el archivo ni como ruta ni como
     * recurso, o si hay errores de lectura/parsing de propiedades críticas
     */
    public LibroHechizos cargarHechizos(String rutaSistemaArchivos) throws IOException {
        Properties props = cargarDesdeRutaOClasspath(rutaSistemaArchivos, RUTA_ARCHIVO_HECHIZOS);

        int cantidad = leerEnteroObligatorio(props, KEY_COUNT, "hechizos.properties: count");
        List<Hechizo> hechizos = new ArrayList<>(cantidad);

        for (int i = 1; i <= cantidad; i++) {
            String base = "hechizo." + i + ".";
            String nombre = leerTextoObligatorio(props, base + "nombre", "hechizo." + i + ".nombre");
            int puntos = leerEnteroObligatorio(props, base + "puntos", "hechizo." + i + ".puntos");

            // Validación básica de rango (persistencia mínima, no lógica de negocio compleja)
            if (puntos < 1) {
                throw new IOException("Valor inválido en " + base + "puntos: " + puntos + ". Debe ser >= 1.");
            }

            Hechizo h = new Hechizo(nombre, puntos);
            hechizos.add(h);
        }

        LibroHechizos libro = new LibroHechizos();
        libro.setHechizos(hechizos);
        return libro;
    }

    // =========================
    //   Utilidades de lectura
    // =========================
    /**
     * Intenta cargar un {@link Properties} desde una ruta de sistema de
     * archivos. Si falla o la ruta es {@code null}, intenta cargarlo desde el
     * classpath usando {@code recursoClasspath}.
     *
     * @param rutaSistemaArchivos ruta de archivo (puede ser null)
     * @param recursoClasspath recurso en classpath (por ejemplo
     * "/propiedades/magos.properties")
     * @return instancia de {@link Properties} cargada
     * @throws IOException si no se encuentra el archivo en ninguna ubicación
     */
    private Properties cargarDesdeRutaOClasspath(String rutaSistemaArchivos, String recursoClasspath) throws IOException {
        Properties props = new Properties();

        // 1) Intentar como archivo en disco
        if (rutaSistemaArchivos != null && !rutaSistemaArchivos.isBlank()) {
            try (FileInputStream fis = new FileInputStream(rutaSistemaArchivos)) {
                props.load(fis);
                return props;
            } catch (IOException ignore) {
                // Continuar con classpath
            }
        }

        // 2) Intentar como recurso en classpath
        try (InputStream is = getClass().getResourceAsStream(recursoClasspath)) {
            if (is != null) {
                props.load(is);
                return props;
            }
        }

        throw new IOException("No se encontró el archivo de propiedades. Ruta: "
                + rutaSistemaArchivos + "  |  Recurso: " + recursoClasspath);
    }

    /**
     * Lee una propiedad textual obligatoria.
     *
     * @param props properties
     * @param key clave a leer
     * @param etiqueta nombre legible para mensajes de error
     * @return valor de la propiedad (no vacío)
     * @throws IOException si la clave no existe o está vacía
     */
    private String leerTextoObligatorio(Properties props, String key, String etiqueta) throws IOException {
        String v = props.getProperty(key);
        if (v == null || v.isBlank()) {
            throw new IOException("Falta o vacío el valor de la propiedad obligatoria: " + etiqueta);
        }
        return v.trim();
    }

    /**
     * Lee una propiedad entera obligatoria.
     *
     * @param props properties
     * @param key clave a leer
     * @param etiqueta nombre legible para mensajes de error
     * @return valor entero
     * @throws IOException si la clave no existe, está vacía o no es un número
     */
    private int leerEnteroObligatorio(Properties props, String key, String etiqueta) throws IOException {
        String v = props.getProperty(key);
        if (v == null || v.isBlank()) {
            throw new IOException("Falta el valor de la propiedad obligatoria: " + etiqueta);
        }
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            throw new IOException("Formato inválido para " + etiqueta + ": " + v, e);
        }
    }

    // =========================
    //   Validaciones simples
    // =========================
    /**
     * Valida si un mago tiene datos mínimos completos.
     *
     * @param mago mago a verificar
     * @return {@code true} si faltan datos, {@code false} si está completo
     */
    public boolean tieneDatosIncompletos(Mago mago) {
        return mago == null
                || mago.getNombre() == null || mago.getNombre().isBlank()
                || mago.getCasa() == null || mago.getCasa().isBlank();
    }

    /**
     * Valida si un hechizo tiene datos mínimos completos.
     *
     * @param hechizo hechizo a verificar
     * @return {@code true} si faltan datos, {@code false} si está completo
     */
    public boolean tieneDatosIncompletos(Hechizo hechizo) {
        return hechizo == null
                || hechizo.getNombre() == null || hechizo.getNombre().isBlank()
                || hechizo.getPuntos() <= 0;
    }

}
