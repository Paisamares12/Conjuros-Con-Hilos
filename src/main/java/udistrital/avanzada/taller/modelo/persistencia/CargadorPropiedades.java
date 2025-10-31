package udistrital.avanzada.taller.modelo.persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import udistrital.avanzada.taller.modelo.Hechizo;
import udistrital.avanzada.taller.modelo.LibroHechizos;
import udistrital.avanzada.taller.modelo.ListadoMagos;
import udistrital.avanzada.taller.modelo.Mago;

/**
 * Cargador de archivos de propiedades ({@code .properties}) para poblar el
 * modelo de la aplicación con {@link Mago magos} y {@link Hechizo hechizos}.
 *
 * <p>
 * Esta clase concentra la lógica de <em>persistencia de lectura</em> desde
 * archivos externos del sistema, manteniendo el principio SRP (Single
 * Responsibility) del diseño SOLID: leer/validar propiedades y mapearlas a
 * objetos de dominio ({@link ListadoMagos} y {@link LibroHechizos}).</p>
 *
 * <p>
 * No usa rutas “quemadas”: cuando la ruta no es provista, abre un
 * {@link JFileChooser} para que el usuario seleccione el archivo. Los formatos
 * esperados son:
 * <ul>
 * <li><b>magos.properties</b>: con la clave {@code count} y pares
 * {@code mago.&lt;i&gt;.nombre}, {@code mago.&lt;i&gt;.casa} (i=1..count).</li>
 * <li><b>hechizos.properties</b>: con la clave {@code count} y pares
 * {@code hechizo.&lt;i&gt;.nombre}, {@code hechizo.&lt;i&gt;.puntos}
 * (i=1..count).</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Ejemplo de propiedades (magos.properties):</b></p>
 * <pre>
 * count=3
 * mago.1.nombre=Harry Potter
 * mago.1.casa=Gryffindor
 * mago.2.nombre=Draco Malfoy
 * mago.2.casa=Slytherin
 * mago.3.nombre=...
 * mago.3.casa=...
 * </pre>
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
public class CargadorPropiedades {

    /**
     * Clave usada para indicar la cantidad de elementos en el archivo.
     */
    private static final String KEY_COUNT = "count";

    /**
     * Permite al usuario seleccionar un archivo .properties desde el sistema.
     *
     * @param descripcion texto descriptivo del tipo de archivo
     * @return ruta absoluta del archivo seleccionado o {@code null} si se
     * cancela
     */
    private String seleccionarArchivo(String descripcion) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar archivo de " + descripcion);
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos de propiedades (*.properties)", "properties"));

        int resultado = chooser.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            return archivo.getAbsolutePath();
        }
        return null;
    }

    /**
     * Carga los magos desde un archivo de propiedades seleccionado por el
     * usuario o desde una ruta proporcionada.
     *
     * @param rutaSistemaArchivos ruta opcional del archivo de magos (si es
     * {@code null}, se abrirá un JFileChooser)
     * @return listado de magos cargado
     * @throws IOException si ocurre un error durante la lectura
     */
    public ListadoMagos cargarMagos(String rutaSistemaArchivos) throws IOException {
        if (rutaSistemaArchivos == null) {
            rutaSistemaArchivos = seleccionarArchivo("magos");
        }
        if (rutaSistemaArchivos == null) {
            throw new IOException("No se seleccionó ningún archivo de magos.");
        }

        Properties props = cargarDesdeRutaOClasspath(rutaSistemaArchivos);
        int cantidad = leerEnteroObligatorio(props, KEY_COUNT, "magos.properties: count");

        List<Mago> magos = new ArrayList<>(cantidad);
        for (int i = 1; i <= cantidad; i++) {
            String base = "mago." + i + ".";
            String nombre = leerTextoObligatorio(props, base + "nombre", "mago." + i + ".nombre");
            String casa = leerTextoObligatorio(props, base + "casa", "mago." + i + ".casa");

            Mago mago = new Mago(nombre, casa);
            magos.add(mago);
        }

        ListadoMagos listado = new ListadoMagos();
        listado.setMagos(magos);
        return listado;
    }

    /**
     * Carga los hechizos desde un archivo de propiedades seleccionado por el
     * usuario o desde una ruta proporcionada.
     *
     * @param rutaSistemaArchivos ruta opcional del archivo de hechizos (si es
     * {@code null}, se abrirá un JFileChooser)
     * @return libro de hechizos cargado
     * @throws IOException si ocurre un error durante la lectura
     */
    public LibroHechizos cargarHechizos(String rutaSistemaArchivos) throws IOException {
        if (rutaSistemaArchivos == null) {
            rutaSistemaArchivos = seleccionarArchivo("hechizos");
        }
        if (rutaSistemaArchivos == null) {
            throw new IOException("No se seleccionó ningún archivo de hechizos.");
        }

        Properties props = cargarDesdeRutaOClasspath(rutaSistemaArchivos);
        int cantidad = leerEnteroObligatorio(props, KEY_COUNT, "hechizos.properties: count");

        List<Hechizo> hechizos = new ArrayList<>(cantidad);
        for (int i = 1; i <= cantidad; i++) {
            String base = "hechizo." + i + ".";
            String nombre = leerTextoObligatorio(props, base + "nombre", "hechizo." + i + ".nombre");
            int puntos = leerEnteroObligatorio(props, base + "puntos", "hechizo." + i + ".puntos");

            if (puntos < 1) {
                throw new IOException("Valor inválido en " + base + "puntos: " + puntos + ". Debe ser >= 1.");
            }

            hechizos.add(new Hechizo(nombre, puntos));
        }

        LibroHechizos libro = new LibroHechizos();
        libro.setHechizos(hechizos);
        return libro;
    }

    // =========================
    //   Utilidades de lectura
    // =========================
    /**
     * Carga un archivo de propiedades desde una ruta del sistema de archivos.
     *
     * @param rutaSistemaArchivos ruta absoluta del archivo
     * @return objeto {@link Properties} cargado
     * @throws IOException si el archivo no puede abrirse o leerse
     */
    private Properties cargarDesdeRutaOClasspath(String rutaSistemaArchivos) throws IOException {
        Properties props = new Properties();

        try (InputStream is = new FileInputStream(rutaSistemaArchivos)) {
            props.load(is);
            return props;
        } catch (IOException e) {
            throw new IOException("No se pudo cargar el archivo: " + rutaSistemaArchivos, e);
        }
    }

    /**
     * Lee una propiedad de texto obligatoria.
     *
     * @param props archivo de propiedades cargado
     * @param key clave a consultar
     * @param etiqueta nombre legible de la propiedad (para mensajes de error)
     * @return valor no vacío (con {@code trim()}) de la propiedad
     * @throws IOException si la clave no existe o el valor está vacío
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
     * @param props archivo de propiedades cargado
     * @param key clave a consultar
     * @param etiqueta nombre legible de la propiedad (para mensajes de error)
     * @return valor entero parseado
     * @throws IOException si la clave no existe, está vacía o no es numérica
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
     * Verifica si un mago tiene datos incompletos o inválidos.
     *
     * @param mago instancia a validar (puede ser {@code null})
     * @return {@code true} si el mago es {@code null} o tiene nombre/casa
     * vacíos; {@code false} en caso contrario
     */
    public boolean tieneDatosIncompletos(Mago mago) {
        return mago == null
                || mago.getNombre() == null || mago.getNombre().isBlank()
                || mago.getCasa() == null || mago.getCasa().isBlank();
    }

    /**
     * Verifica si un hechizo tiene datos incompletos o inválidos.
     *
     * @param hechizo instancia a validar (puede ser {@code null})
     * @return {@code true} si el hechizo es {@code null}, si su nombre es vacío
     * o si {@code puntos} es menor o igual a cero; {@code false} en caso
     * contrario
     */
    public boolean tieneDatosIncompletos(Hechizo hechizo) {
        return hechizo == null
                || hechizo.getNombre() == null || hechizo.getNombre().isBlank()
                || hechizo.getPuntos() <= 0;
    }
}
