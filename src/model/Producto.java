package model;

/**
 * Clase abstracta base que representa un producto en el sistema MiStock.
 */
public abstract class Producto {
    protected int id;
    protected String nombre;
    protected double precio;
    protected int stock;

    public Producto() {
    }

    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // Método abstracto para obtener el nombre de la categoría
    public abstract String getCategoria();

    // Método abstracto para obtener información adicional o específica de la categoría
    public abstract String getDetalleEspecifico();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return String.format("[%s] ID: %d | %s | S/ %.2f | Stock: %d", 
                getCategoria(), id, nombre, precio, stock);
    }
}
