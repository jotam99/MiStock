package model;

/**
 * Subclase que representa productos de la categoría Abarrote.
 * Añade la propiedad 'fechaCaducidad' fundamental para la gestión de productos no perecibles/perecibles.
 */
public class ProductoAbarrote extends Producto {
    private String fechaCaducidad;

    public ProductoAbarrote(int id, String nombre, double precio, int stock, String fechaCaducidad) {
        super(id, nombre, precio, stock);
        this.fechaCaducidad = (fechaCaducidad == null || fechaCaducidad.trim().isEmpty()) ? "Sin fecha" : fechaCaducidad;
    }

    public ProductoAbarrote(int id, String nombre, double precio, int stock) {
        this(id, nombre, precio, stock, "Sin fecha");
    }

    @Override
    public String getCategoria() {
        return "Abarrote";
    }

    @Override
    public String getDetalleEspecifico() {
        return "Vence: " + fechaCaducidad;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }
}
