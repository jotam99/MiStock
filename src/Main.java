import controller.InventarioController;
import database.ConexionDB;
import java.sql.Connection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.BodegaInventarioView;

/**
 * Punto de entrada principal para el sistema MiStock.
 */
public class Main {

    public static void main(String[] args) {
        // --- 1. PRUEBA DE ENLACE CON SQL SERVER ---
        System.out.println("Probando enlace con SQL Server...");
        Connection cn = ConexionDB.getInstancia().getConexion();

        if (cn != null) {
            System.out.println("¡ENLACE EXITOSO! Java y SQL Server están conectados.");
        } else {
            System.out.println("-> [Aviso] No se pudo conectar a la base de datos.");
        }
        // ------------------------------------------

        // 2. Establecer apariencia visual
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // 3. Iniciar la interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            BodegaInventarioView view = new BodegaInventarioView();
            InventarioController controller = new InventarioController(view);
            controller.iniciar();
        });
    }
}