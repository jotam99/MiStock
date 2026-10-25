package dao;

import database.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Producto;
import model.ProductoFactory;

/**
 * Implementación concreta del acceso a datos para SQL Server.
 * Ejecuta sentencias T-SQL y transforma los registros mediante ProductoFactory.
 */
public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, precio, stock, categoria FROM dbo.Productos";

        try {
            // Obtenemos la conexión única del Singleton
            Connection cn = ConexionDB.getInstancia().getConexion();
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Recorremos cada fila devuelta por SQL Server
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock");
                String categoria = rs.getString("categoria");

                // Usamos la Factory para crear la instancia adecuada (Bebida, Abarrote o Genérico)
                Producto p = ProductoFactory.crearProducto(categoria, id, nombre, precio, stock);
                lista.add(p);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("-> [Error DAO] Al listar productos: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO dbo.Productos (id, nombre, precio, stock, categoria) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection cn = ConexionDB.getInstancia().getConexion();
            PreparedStatement ps = cn.prepareStatement(sql);

            // Asignación segura de parámetros (evita inyecciones SQL)
            ps.setInt(1, producto.getId());
            ps.setString(2, producto.getNombre());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getCategoria());

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("-> [Error DAO] Al insertar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE dbo.Productos SET nombre = ?, precio = ?, stock = ?, categoria = ? WHERE id = ?";

        try {
            Connection cn = ConexionDB.getInstancia().getConexion();
            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());
            ps.setString(4, producto.getCategoria());
            ps.setInt(5, producto.getId());

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("-> [Error DAO] Al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM dbo.Productos WHERE id = ?";

        try {
            Connection cn = ConexionDB.getInstancia().getConexion();
            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("-> [Error DAO] Al eliminar producto: " + e.getMessage());
            return false;
        }
    }
}