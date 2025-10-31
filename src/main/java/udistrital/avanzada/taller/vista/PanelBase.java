package udistrital.avanzada.taller.vista;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Clase base para todos los paneles de la interfaz del proyecto <b>ConjurosConHilos</b>.
 * <p>
 * Esta clase extiende {@link javax.swing.JPanel} e implementa un comportamiento común:
 * el pintado automático de una imagen de fondo en todos los paneles que hereden de ella.
 * <br>
 * El propósito de esta clase es centralizar la lógica del fondo para evitar duplicación
 * de código en cada panel específico de la vista (por ejemplo, paneles de inicio, carga
 * o combate). De esta forma se cumple con el principio de responsabilidad única (SRP)
 * y se favorece la reutilización del código.
 * </p>
 *
 * <p>
 * La imagen de fondo se busca dentro del classpath del proyecto, en la ruta:
 * <code>/Imagenes/Background.png</code>. Esta imagen se escala automáticamente para
 * ajustarse al tamaño actual del panel cada vez que este se repinta.
 * </p>
 *
 * <p><b>Ejemplo de uso:</b></p>
 * <pre>
 * public class PanelInicio extends PanelBase {
 *     public PanelInicio() {
 *         // Este panel ya tendrá el fondo configurado por PanelBase
 *         initComponents();
 *     }
 * }
 * </pre>
 *
 * @author Paula Martínez
 * @version 6.0
 * @since 2025-10-25
 */
public class PanelBase extends JPanel {

    /** Identificador de versión serial del panel (recomendado para compatibilidad con Swing). */
    private static final long serialVersionUID = 1L;

    /**
     * Ruta del recurso gráfico que se usará como fondo.
     * <p>
     * Debe ubicarse dentro del classpath en:
     * <code>src/main/resources/Imagenes/Background.png</code>
     * o dentro de un paquete equivalente en el proyecto.
     * </p>
     */
    private static final String RUTA_FONDO = "/Imagenes/Background.png";

    /** URL del recurso de imagen cargado desde el classpath. */
    private final URL fondoUrl = getClass().getResource(RUTA_FONDO);

    /** Imagen del fondo ya cargada y almacenada en memoria para reutilización. */
    private final Image fondoImg = (fondoUrl != null) ? new ImageIcon(fondoUrl).getImage() : null;

    /**
     * Constructor por defecto del panel base.
     * <p>
     * Configura el panel para ser opaco, lo que garantiza que Swing
     * llame correctamente a {@link #paintComponent(Graphics)} para pintar el fondo.
     * </p>
     */
    public PanelBase() {
        // Asegura que el panel pinte su propio fondo antes de los hijos.
        setOpaque(true);
    }

    /**
     * Sobrescribe el método de pintura del panel para dibujar una imagen de fondo.
     * <p>
     * Este método se ejecuta automáticamente cada vez que Swing repinta el panel.
     * Primero llama a {@code super.paintComponent(g)} para limpiar el área y luego
     * dibuja la imagen escalada al tamaño actual del panel.
     * </p>
     *
     * @param g el contexto gráfico usado para dibujar el panel
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Si no se encontró la imagen, simplemente deja el color de fondo por defecto.
        if (fondoImg == null) {
            return;
        }

        // Dibuja la imagen escalada al tamaño del panel.
        g.drawImage(fondoImg, 0, 0, getWidth(), getHeight(), this);
    }
}
