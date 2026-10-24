package controller;

import model.Producto;
import model.ProductoFactory;
import view.BodegaInventarioView;

import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 * Controlador del sistema MiStock.
 * Coordina la interacción entre la vista (BodegaInventarioView) y el modelo (Producto / LinkedList).
 * Emplea ProductoFactory para la creación desacoplada de productos.
 */
public class InventarioController {

    private final BodegaInventarioView view;
    private final LinkedList<Producto> listaProductos;

    public InventarioController(BodegaInventarioView view) {
        this.view = view;
        this.listaProductos = new LinkedList<>();
        inicializarEventos();
        cargarDatosDePrueba();
        actualizarTabla();
    }

    /**
     * Registra los manejadores de eventos (ActionListeners y MouseListeners)
     * para mantener la vista totalmente desacoplada de la lógica de negocio.
     */
    private void inicializarEventos() {
        // Botón Guardar
        view.getBtnGuardar().addActionListener(e -> guardarProducto());

        // Botón Modificar
        view.getBtnModificar().addActionListener(e -> modificarProducto());

        // Botón Eliminar
        view.getBtnEliminar().addActionListener(e -> eliminarProducto());

        // Botón Limpiar
        view.getBtnLimpiar().addActionListener(e -> limpiarCampos());

        // Botón '+ Categoría'
        view.getBtnAgregarCategoria().addActionListener(e -> agregarNuevaCategoria());

        // Selección en la tabla para cargar datos al formulario
        view.getTblProductos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFilaTabla();
            }
        });
    }

    /**
     * Datos iniciales de demostración para bodegas.
     */
    private void cargarDatosDePrueba() {
        // Usamos el Factory Method para instanciar los productos iniciales
        Producto p1 = ProductoFactory.crearProducto("Bebida", 101, "Inca Kola 1.5L", 6.50, 24);
        Producto p2 = ProductoFactory.crearProducto("Abarrote", 102, "Arroz Costeño 1kg", 4.80, 50);

        listaProductos.add(p1);
        listaProductos.add(p2);
    }

    /**
     * Guarda un nuevo producto validando los campos y usando ProductoFactory.
     */
    private void guardarProducto() {
        try {
            String strId = view.getTxtId().getText().trim();
            String nombre = view.getTxtNombre().getText().trim();
            String strPrecio = view.getTxtPrecio().getText().trim();
            String strStock = view.getTxtStock().getText().trim();
            String categoria = (String) view.getCmbCategoria().getSelectedItem();

            if (strId.isEmpty() || nombre.isEmpty() || strPrecio.isEmpty() || strStock.isEmpty() || categoria == null) {
                view.mostrarError("Por favor, complete todos los campos del formulario.");
                return;
            }

            int id = Integer.parseInt(strId);
            if (id <= 0) {
                view.mostrarError("El ID debe ser un número entero positivo.");
                return;
            }

            // Validar que el ID no esté duplicado
            if (buscarProductoPorId(id) != null) {
                view.mostrarError("Ya existe un producto registrado con el ID: " + id);
                return;
            }

            double precio = Double.parseDouble(strPrecio.replace(",", "."));
            if (precio < 0) {
                view.mostrarError("El precio no puede ser negativo.");
                return;
            }

            int stock = Integer.parseInt(strStock);
            if (stock < 0) {
                view.mostrarError("El stock no puede ser negativo.");
                return;
            }

            // Instanciación a través del patrón Factory Method
            Producto nuevoProducto = ProductoFactory.crearProducto(categoria, id, nombre, precio, stock);

            // Almacenar en la lista temporal en memoria
            listaProductos.add(nuevoProducto);

            actualizarTabla();
            limpiarCampos();
            view.mostrarMensaje("Producto registrado exitosamente como [" + nuevoProducto.getCategoria() + "].");

        } catch (NumberFormatException ex) {
            view.mostrarError("Formato numérico inválido en ID, Precio o Stock.");
        } catch (IllegalArgumentException ex) {
            view.mostrarError("Error en la fábrica de productos: " + ex.getMessage());
        }
    }

    /**
     * Modifica el producto correspondiente al ID ingresado o fila seleccionada.
     */
    private void modificarProducto() {
        try {
            String strId = view.getTxtId().getText().trim();
            if (strId.isEmpty()) {
                view.mostrarError("Ingrese o seleccione el ID del producto que desea modificar.");
                return;
            }

            int id = Integer.parseInt(strId);
            int indice = encontrarIndicePorId(id);

            if (indice == -1) {
                view.mostrarError("No se encontró ningún producto con el ID: " + id);
                return;
            }

            String nombre = view.getTxtNombre().getText().trim();
            String strPrecio = view.getTxtPrecio().getText().trim();
            String strStock = view.getTxtStock().getText().trim();
            String categoria = (String) view.getCmbCategoria().getSelectedItem();

            if (nombre.isEmpty() || strPrecio.isEmpty() || strStock.isEmpty() || categoria == null) {
                view.mostrarError("Por favor, no deje campos vacíos al modificar.");
                return;
            }

            double precio = Double.parseDouble(strPrecio.replace(",", "."));
            int stock = Integer.parseInt(strStock);

            if (precio < 0 || stock < 0) {
                view.mostrarError("El precio y el stock deben ser valores positivos.");
                return;
            }

            // Usamos el Factory Method para regenerar la instancia acorde a la categoría elegida
            Producto productoActualizado = ProductoFactory.crearProducto(categoria, id, nombre, precio, stock);
            listaProductos.set(indice, productoActualizado);

            actualizarTabla();
            limpiarCampos();
            view.mostrarMensaje("Producto modificado correctamente.");

        } catch (NumberFormatException ex) {
            view.mostrarError("Valores numéricos inválidos.");
        }
    }

    /**
     * Elimina el producto seleccionado del inventario.
     */
    private void eliminarProducto() {
        int fila = view.getTblProductos().getSelectedRow();
        String strId = view.getTxtId().getText().trim();

        int idEliminar = -1;

        if (fila != -1) {
            idEliminar = Integer.parseInt(view.getTableModel().getValueAt(fila, 0).toString());
        } else if (!strId.isEmpty()) {
            try {
                idEliminar = Integer.parseInt(strId);
            } catch (NumberFormatException ignored) {
            }
        }

        if (idEliminar == -1) {
            view.mostrarError("Seleccione un producto de la tabla o ingrese un ID para eliminar.");
            return;
        }

        int indice = encontrarIndicePorId(idEliminar);
        if (indice == -1) {
            view.mostrarError("No se encontró el producto con ID: " + idEliminar);
            return;
        }

        if (view.confirmar("¿Está seguro de eliminar el producto con ID " + idEliminar + "?")) {
            listaProductos.remove(indice);
            actualizarTabla();
            limpiarCampos();
            view.mostrarMensaje("Producto eliminado del inventario.");
        }
    }

    /**
     * Limpia los campos del formulario y deselecciona la tabla.
     */
    private void limpiarCampos() {
        view.getTxtId().setText("");
        view.getTxtNombre().setText("");
        view.getTxtPrecio().setText("");
        view.getTxtStock().setText("");
        view.getTxtNuevaCategoria().setText("");
        if (view.getCmbCategoria().getItemCount() > 0) {
            view.getCmbCategoria().setSelectedIndex(0);
        }
        view.getTblProductos().clearSelection();
        view.getTxtId().setEditable(true);
        view.getTxtId().requestFocus();
    }

    /**
     * Agrega una nueva categoría al JComboBox desde el campo 'Nueva Cat'.
     */
    private void agregarNuevaCategoria() {
        String nuevaCat = view.getTxtNuevaCategoria().getText().trim();

        if (nuevaCat.isEmpty()) {
            view.mostrarError("Ingrese el nombre de la nueva categoría.");
            return;
        }

        // Verificar si la categoría ya existe en el JComboBox
        for (int i = 0; i < view.getCmbCategoria().getItemCount(); i++) {
            if (view.getCmbCategoria().getItemAt(i).equalsIgnoreCase(nuevaCat)) {
                view.mostrarError("La categoría '" + nuevaCat + "' ya existe en el selector.");
                view.getCmbCategoria().setSelectedIndex(i);
                return;
            }
        }

        // Añadir y seleccionar la nueva categoría
        view.getCmbCategoria().addItem(nuevaCat);
        view.getCmbCategoria().setSelectedItem(nuevaCat);
        view.getTxtNuevaCategoria().setText("");
        view.mostrarMensaje("Categoría '" + nuevaCat + "' agregada con éxito.");
    }

    /**
     * Carga los datos de la fila seleccionada de la tabla al formulario superior.
     */
    private void seleccionarFilaTabla() {
        int fila = view.getTblProductos().getSelectedRow();
        if (fila != -1) {
            DefaultTableModel model = view.getTableModel();
            view.getTxtId().setText(model.getValueAt(fila, 0).toString());
            view.getTxtNombre().setText(model.getValueAt(fila, 1).toString());
            view.getTxtPrecio().setText(model.getValueAt(fila, 2).toString());
            view.getTxtStock().setText(model.getValueAt(fila, 3).toString());

            String cat = model.getValueAt(fila, 4).toString();
            // Asegurar que la categoría exista en el JComboBox
            boolean existe = false;
            for (int i = 0; i < view.getCmbCategoria().getItemCount(); i++) {
                if (view.getCmbCategoria().getItemAt(i).equalsIgnoreCase(cat)) {
                    view.getCmbCategoria().setSelectedIndex(i);
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                view.getCmbCategoria().addItem(cat);
                view.getCmbCategoria().setSelectedItem(cat);
            }
        }
    }

    /**
     * Refresca el JTable con los datos actuales de la LinkedList en memoria.
     */
    public void actualizarTabla() {
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0); // Limpiar filas previas

        for (Producto p : listaProductos) {
            Object[] fila = new Object[]{
                    p.getId(),
                    p.getNombre(),
                    String.format("%.2f", p.getPrecio()).replace(",", "."),
                    p.getStock(),
                    p.getCategoria()
            };
            model.addRow(fila);
        }
    }

    private Producto buscarProductoPorId(int id) {
        for (Producto p : listaProductos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private int encontrarIndicePorId(int id) {
        for (int i = 0; i < listaProductos.size(); i++) {
            if (listaProductos.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Inicia y despliega la vista gráfica.
     */
    public void iniciar() {
        view.setVisible(true);
    }

    public LinkedList<Producto> getListaProductos() {
        return listaProductos;
    }
}
