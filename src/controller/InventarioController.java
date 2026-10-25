package controller;

import dao.ProductoDAO;
import dao.ProductoDAOImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Producto;
import model.ProductoFactory;
import view.BodegaInventarioView;

/**
 * Controlador que articula la vista Swing con la persistencia DAO y la Factory.
 */
public class InventarioController implements ActionListener {

    private final BodegaInventarioView view;
    private final ProductoDAO productoDAO;

    public InventarioController(BodegaInventarioView view) {
        this.view = view;
        this.productoDAO = new ProductoDAOImpl();
    }

    public void iniciar() {
        // Enlazar los botones de la vista con este controlador
        this.view.btnGuardar.addActionListener(this);
        this.view.btnModificar.addActionListener(this);
        this.view.btnEliminar.addActionListener(this);
        this.view.btnLimpiar.addActionListener(this);

        // Cargar registros desde SQL Server al abrir la ventana
        cargarTablaDesdeBD();

        this.view.setLocationRelativeTo(null);
        this.view.setVisible(true);
    }

    private void cargarTablaDesdeBD() {
        DefaultTableModel modelo = (DefaultTableModel) view.tblProductos.getModel();
        modelo.setRowCount(0); // Limpiar filas previas

        // Consultar productos a SQL Server mediante el DAO
        List<Producto> lista = productoDAO.listarTodos();

        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock(),
                p.getCategoria()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnGuardar) {
            guardarProducto();
        } else if (e.getSource() == view.btnLimpiar) {
            limpiarCampos();
        }
    }

    private void guardarProducto() {
        try {
            int id = Integer.parseInt(view.txtId.getText().trim());
            String nombre = view.txtNombre.getText().trim();
            double precio = Double.parseDouble(view.txtPrecio.getText().trim());
            int stock = Integer.parseInt(view.txtStock.getText().trim());
            String categoria = (String) view.cmbCategoria.getSelectedItem();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(view, "El nombre no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Instanciación polimórfica mediante el Factory Method
            Producto nuevo = ProductoFactory.crearProducto(categoria, id, nombre, precio, stock);

            // Persistencia en SQL Server mediante el DAO
            boolean exito = productoDAO.insertar(nuevo);

            if (exito) {
                JOptionPane.showMessageDialog(view, "¡Producto guardado exitosamente en SQL Server!");
                cargarTablaDesdeBD();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(view, "No se pudo guardar. Verifique si el ID ya existe en la base de datos.", "Error de clave duplicada", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Los campos ID, Precio y Stock deben contener únicamente números.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Ocurrió un error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        view.txtId.setText("");
        view.txtNombre.setText("");
        view.txtPrecio.setText("");
        view.txtStock.setText("");
        view.txtId.requestFocus();
    }
}