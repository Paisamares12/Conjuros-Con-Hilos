package udistrital.avanzada.taller.vista;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel base para la interfaz de <b>ConjurosConHilos</b>.
 * <p>
 * Pinta automáticamente una imagen de fondo para cualquier panel que herede
 * de esta clase. La imagen debe estar en el classpath en
 * <code>/Imagenes/Background.png</code>.
 * </p>
 *
 * @author Paula Martinez
 * @version 1.0
 * @since 2025-10-25
 */
public class PanelBase extends JPanel {

    private static final long serialVersionUID = 1L;

    // Ruta absoluta dentro del classpath (src/main/resources/Imagenes/Background.png)
    private static final String RUTA_FONDO = "/Imagenes/Background.png";

    // Cache del recurso para evitar búsquedas repetidas
    private final URL fondoUrl = getClass().getResource(RUTA_FONDO);
    private final Image fondoImg = (fondoUrl != null) ? new ImageIcon(fondoUrl).getImage() : null;

    /**
     * Crea el panel base. Se mantiene compatible con el GUI Builder de NetBeans.
     */
    public PanelBase() {
        // Asegura que el panel pinta su propio fondo
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Si no se encontró la imagen, pinta el color de fondo por defecto
        if (fondoImg == null) {
            return;
        }

        // Dibuja la imagen escalada al tamaño actual del panel
        g.drawImage(fondoImg, 0, 0, getWidth(), getHeight(), this);
    }
}
