import controller.InventarioController;
import view.BodegaInventarioView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada principal para el sistema MiStock.
 */
public class Main {
    public static void main(String[] args) {
        // Establecer apariencia del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // Ejecutar la interfaz en el hilo de despacho de eventos de Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            BodegaInventarioView view = new BodegaInventarioView();
            InventarioController controller = new InventarioController(view);
            controller.iniciar();
        });
    }
}
