package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión a la base de datos SQL Server aplicando el Patrón Singleton.
 * Asegura la existencia de una única conexión compartida para toda la aplicación.
 */
public class ConexionDB {

    // 1. Instancia única de la propia clase (Singleton)
    private static ConexionDB instancia;
    
    // Objeto Connection nativo de JDBC
    private Connection conexion;

    // Parámetros de conexión a SQL Server
    private final String URL = "jdbc:sqlserver://localhost:1433;databaseName=MiStockDB;user=sa;password=Admin123*;encrypt=true;trustServerCertificate=true;";

    // 2. Constructor privado: impide que otras clases creen instancias con 'new ConexionDB()'
    private ConexionDB() {
        try {
            // Cargar explícitamente el driver de Microsoft
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.conexion = DriverManager.getConnection(URL);
            System.out.println("-> [ConexionDB] Conexión establecida con éxito a MiStockDB.");
        } catch (ClassNotFoundException e) {
            System.err.println("-> [Error] No se encontró el driver JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("-> [Error] Fallo al conectar con SQL Server: " + e.getMessage());
        }
    }

    // 3. Método de acceso global estático y sincronizado (Thread-safe)
    public static synchronized ConexionDB getInstancia() {
        try {
            // Si no existe instancia o si la conexión física se cerró, la crea nuevamente
            if (instancia == null || instancia.getConexion().isClosed()) {
                instancia = new ConexionDB();
            }
        } catch (SQLException e) {
            System.err.println("-> [Error] Verificación de estado de conexión: " + e.getMessage());
        }
        return instancia;
    }

    // Retorna la conexión activa para ser usada por los DAO
    public Connection getConexion() {
        return this.conexion;
    }

    // Método seguro para cerrar la conexión al salir de la aplicación
    public void cerrarConexion() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                    System.out.println("-> [ConexionDB] Conexión cerrada de forma segura.");
                }
            } catch (SQLException e) {
                System.err.println("-> [Error] Al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}