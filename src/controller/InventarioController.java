package controller;

import dao.ProductoDAO;
import dao.ProductoDAOImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Producto;
import model.ProductoFactory;
import view.BodegaInventarioView;

/**
 * Controlador que articula la vista Swing con la persistencia DAO y la Factory.
 * Gestiona el ciclo de vida completo de las operaciones CRUD.
 */
public class InventarioController implements ActionListener {

    private final BodegaInventarioView view;
    private final ProductoDAO productoDAO;

    public InventarioController(BodegaInventarioView view) {
        this.view = view;
        this.productoDAO = new ProductoDAOImpl();
    }

    public void iniciar() {
        // Enlazar botones con el controlador
        this.view.btnGuardar.addActionListener(this);
        this.view.btnModificar.addActionListener(this);
        this.view.btnEliminar.addActionListener(this);
        this.view.btnLimpiar.addActionListener(this);

        // Enlazar evento de selección con el ratón en la tabla
        this.view.tblProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarFilaSeleccionadaEnFormulario();
            }
        });

        // Cargar datos iniciales desde SQL Server
        cargarTablaDesdeBD();

        this.view.setLocationRelativeTo(null);
        this.view.setVisible(true);
    }

    private void cargarTablaDesdeBD() {
        DefaultTableModel modelo = (DefaultTableModel) view.tblProductos.getModel();
        modelo.setRowCount(0);

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

    private void cargarFilaSeleccionadaEnFormulario() {
        int fila = view.tblProductos.getSelectedRow();
        if (fila != -1) {
            view.txtId.setText(view.tblProductos.getValueAt(fila, 0).toString());
            view.txtNombre.setText(view.tblProductos.getValueAt(fila, 1).toString());
            view.txtPrecio.setText(view.tblProductos.getValueAt(fila, 2).toString());
            view.txtStock.setText(view.tblProductos.getValueAt(fila, 3).toString());
            view.cmbCategoria.setSelectedItem(view.tblProductos.getValueAt(fila, 4).toString());

            // Bloquear la edición del ID para proteger la clave primaria durante la actualización
            view.txtId.setEditable(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnGuardar) {
            guardarProducto();
        } else if (e.getSource() == view.btnModificar) {
            modificarProducto();
        } else if (e.getSource() == view.btnEliminar) {
            eliminarProducto();
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

            Producto nuevo = ProductoFactory.crearProducto(categoria, id, nombre, precio, stock);
            boolean exito = productoDAO.insertar(nuevo);

            if (exito) {
                JOptionPane.showMessageDialog(view, "¡Producto guardado exitosamente en SQL Server!");
                cargarTablaDesdeBD();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(view, "No se pudo guardar. Verifique si el ID ya existe en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Los campos ID, Precio y Stock deben contener únicamente números.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Ocurrió un error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarProducto() {
        try {
            if (view.txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Debe seleccionar un producto de la tabla para modificarlo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(view.txtId.getText().trim());
            String nombre = view.txtNombre.getText().trim();
            double precio = Double.parseDouble(view.txtPrecio.getText().trim());
            int stock = Integer.parseInt(view.txtStock.getText().trim());
            String categoria = (String) view.cmbCategoria.getSelectedItem();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(view, "El nombre no puede quedar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Producto modificado = ProductoFactory.crearProducto(categoria, id, nombre, precio, stock);
            boolean exito = productoDAO.actualizar(modificado);

            if (exito) {
                JOptionPane.showMessageDialog(view, "¡Producto actualizado exitosamente en SQL Server!");
                cargarTablaDesdeBD();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(view, "No se pudo actualizar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Los campos de precio y stock deben ser válidos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        try {
            if (view.txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Debe seleccionar un producto de la tabla para eliminarlo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(view.txtId.getText().trim());
            String nombre = view.txtNombre.getText().trim();

            int confirmacion = JOptionPane.showConfirmDialog(
                view,
                "¿Está seguro de eliminar el producto \"" + nombre + "\" (ID: " + id + ") de la base de datos?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean exito = productoDAO.eliminar(id);

                if (exito) {
                    JOptionPane.showMessageDialog(view, "Producto eliminado correctamente de SQL Server.");
                    cargarTablaDesdeBD();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(view, "No se pudo eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "ID no válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        view.txtId.setText("");
        view.txtNombre.setText("");
        view.txtPrecio.setText("");
        view.txtStock.setText("");
        view.txtId.setEditable(true); // Reactivar el campo ID para nuevos registros
        view.tblProductos.clearSelection();
        view.txtId.requestFocus();
    }
}