package dao;

import java.util.List;
import model.Producto;

/**
 * Contrato de operaciones de persistencia para la entidad Producto.
 * Define las operaciones CRUD desacopladas del motor de base de datos.
 */
public interface ProductoDAO {
    
    // Consulta todos los productos registrados en SQL Server
    List<Producto> listarTodos();
    
    // Inserta un nuevo producto en la tabla dbo.Productos
    boolean insertar(Producto producto);
    
    // Actualiza los datos de un producto existente por su ID
    boolean actualizar(Producto producto);
    
    // Elimina un producto de la base de datos según su ID
    boolean eliminar(int id);
}