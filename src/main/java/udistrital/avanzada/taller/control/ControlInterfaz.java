package udistrital.avanzada.taller.control;
//Importar clases desde otros paquetes
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import udistrital.avanzada.taller.vista.VentanaPrincipal;

/**
 *
 * @author paisa
 */
public class ControlInterfaz implements ActionListener {

    /**
     * Controlador de la capa lógica del sistema.
     */
    private ControlLogica cLogica;

    /**
     * Referencia a la ventana principal de la aplicación.
     */
    private VentanaPrincipal vPrincipal;

    public ControlInterfaz(ControlLogica cLogica) {
        this.cLogica = cLogica;
        iniciarPrograma(); // Se lanza la interfaz gráfica al crear el controlador
    }

    /**
     * Inicia la interfaz gráfica del programa <b>ConjurosConHilos</b>.
     * <p>
     * Este método crea y configura la ventana principal del sistema, la asocia
     * con el controlador de interfaz y la muestra en pantalla, centrada en el
     * monitor del usuario.
     * </p>
     *
     * <p>
     * Solo debe ejecutarse una vez, al inicializar el controlador de interfaz.
     * </p>
     *
     */
    private void iniciarPrograma() {
        // Se crea la instancia de la ventana principal y se vincula
        // el controlador de interfaz, para manejar los eventos de la UI.
        this.vPrincipal = new VentanaPrincipal(this);

        // Se hace visible la ventana al usuario.
        this.vPrincipal.setVisible(true);

        // Se centra la ventana en la pantalla, sin depender de la resolución.
        this.vPrincipal.setLocationRelativeTo(null);
        //Añadir los ActionListener a los botones
        conectarEventos();
    }
    
    /**
     * Conecta todos los eventos de botones con sus listeners.
     */
    private void conectarEventos() {
        this.vPrincipal.getPanelMain().getPanelInicio().getBotonJugar().addActionListener(this);
        this.vPrincipal.getPanelMain().getPanelInicio().getBotonSalir().addActionListener(this);
        this.vPrincipal.getPanelMain().getPanelCargar().getBotonSalir().addActionListener(this);
    }
        
    @Override
    public void actionPerformed(ActionEvent e) {
        // ---- BOTÓN SALIR ----
        if (e.getSource() == this.vPrincipal.getPanelMain().getPanelInicio().getBotonSalir()) {
            this.vPrincipal.dispose();
            System.exit(0);
        }
        if (e.getSource() == this.vPrincipal.getPanelMain().getPanelCargar().getBotonSalir()) {
            this.vPrincipal.dispose();
            System.exit(0);
        }
        // ---- BOTÓN JUGAR ----
        if(e.getSource() == this.vPrincipal.getPanelMain().getPanelInicio().getBotonJugar()){
            this.vPrincipal.getPanelMain().mostrarPanelCargar();
        }
        if(e.getSource() == this.vPrincipal.getPanelMain().getPanelCargar().getBotonJugar()){
            
        }
    }
}
