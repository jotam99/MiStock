package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Vista principal de la aplicación MiStock.
 * Implementa la interfaz gráfica solicitada con Swing siguiendo MVC.
 */
public class BodegaInventarioView extends JFrame {

   // Componentes del Formulario Superior
    public JTextField txtId;
    public JTextField txtNombre;
    public JTextField txtPrecio;
    public JTextField txtStock;
    public JComboBox<String> cmbCategoria;
    public JTextField txtNuevaCategoria;
    public JButton btnAgregarCategoria;

    // Componentes de la Tabla Central
    public JTable tblProductos;
    public DefaultTableModel tableModel;

    // Componentes de la Barra Inferior (Botones de Acción)
    public JButton btnGuardar;
    public JButton btnModificar;
    public JButton btnEliminar;
    public JButton btnLimpiar;
    
    public BodegaInventarioView() {
        super("MiStock - Gestión de Inventario para Bodegas");
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setMinimumSize(new Dimension(750, 500));
        setLocationRelativeTo(null);

        // Panel Principal con margen
        JPanel pnlPrincipal = new JPanel(new BorderLayout(10, 10));
        pnlPrincipal.setBorder(new EmptyBorder(12, 12, 12, 12));

        // 1. Formulario superior
        JPanel pnlSuperior = crearPanelFormulario();
        pnlPrincipal.add(pnlSuperior, BorderLayout.NORTH);

        // 2. Tabla central
        JPanel pnlCentral = crearPanelTabla();
        pnlPrincipal.add(pnlCentral, BorderLayout.CENTER);

        // 3. Botones inferiores
        JPanel pnlInferior = crearPanelBotones();
        pnlPrincipal.add(pnlInferior, BorderLayout.SOUTH);

        setContentPane(pnlPrincipal);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Datos del Producto", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("SansSerif", Font.BOLD, 12)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: ID y Nombre
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.2;
        txtId = new JTextField(8);
        panel.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 3; gbc.gridy = 0; gbc.gridwidth = 3; gbc.weightx = 0.8;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);

        // Fila 1: Precio (S/) y Stock
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(new JLabel("Precio (S/):"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.2;
        txtPrecio = new JTextField(8);
        panel.add(txtPrecio, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Stock:"), gbc);

        gbc.gridx = 3; gbc.gridy = 1; gbc.gridwidth = 3; gbc.weightx = 0.8;
        txtStock = new JTextField(8);
        panel.add(txtStock, gbc);

        // Fila 2: Categoría, Nueva Cat y Botón '+ Categoría'
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(new JLabel("Categoría:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.3;
        cmbCategoria = new JComboBox<>();
        cmbCategoria.addItem("Bebida");
        cmbCategoria.addItem("Abarrote");
        panel.add(cmbCategoria, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Nueva Cat:"), gbc);

        gbc.gridx = 3; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.4;
        txtNuevaCategoria = new JTextField(12);
        panel.add(txtNuevaCategoria, gbc);

        gbc.gridx = 4; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.1;
        btnAgregarCategoria = new JButton("+ Categoría");
        btnAgregarCategoria.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnAgregarCategoria, gbc);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Inventario de Productos", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("SansSerif", Font.BOLD, 12)
        ));

        // Columnas solicitadas: ID, Nombre, Precio, Stock, Categoría
        String[] columnas = {"ID", "Nombre", "Precio", "Stock", "Categoría"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura en la tabla, se edita con el formulario
            }
        };

        tblProductos = new JTable(tableModel);
        tblProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProductos.setRowHeight(24);
        tblProductos.getTableHeader().setReorderingAllowed(false);
        tblProductos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        // Alineación centrada para ID, Precio, Stock y Categoría
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblProductos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblProductos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblProductos.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblProductos.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tblProductos);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 8));

        btnGuardar = new JButton("Guardar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        Dimension btnSize = new Dimension(110, 32);
        JButton[] botones = {btnGuardar, btnModificar, btnEliminar, btnLimpiar};
        for (JButton btn : botones) {
            btn.setPreferredSize(btnSize);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panel.add(btn);
        }

        return panel;
    }

    // Métodos utilitarios de diálogo
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "MiStock - Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "MiStock - Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmar(String mensaje) {
        int res = JOptionPane.showConfirmDialog(this, mensaje, "MiStock - Confirmación", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return res == JOptionPane.YES_OPTION;
    }

    // Getters para el controlador
    public JTextField getTxtId() {
        return txtId;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtPrecio() {
        return txtPrecio;
    }

    public JTextField getTxtStock() {
        return txtStock;
    }

    public JComboBox<String> getCmbCategoria() {
        return cmbCategoria;
    }

    public JTextField getTxtNuevaCategoria() {
        return txtNuevaCategoria;
    }

    public JButton getBtnAgregarCategoria() {
        return btnAgregarCategoria;
    }

    public JTable getTblProductos() {
        return tblProductos;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnModificar() {
        return btnModificar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }
}
