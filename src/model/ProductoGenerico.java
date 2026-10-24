package model;

/**
 * Subclase genérica para categorías personalizadas creadas dinámicamente en la bodega.
 */
public class ProductoGenerico extends Producto {
    private String categoriaPersonalizada;

    public ProductoGenerico(int id, String nombre, double precio, int stock, String categoriaPersonalizada) {
        super(id, nombre, precio, stock);
        this.categoriaPersonalizada = (categoriaPersonalizada == null || categoriaPersonalizada.trim().isEmpty()) 
                ? "General" 
                : categoriaPersonalizada.trim();
    }

    @Override
    public String getCategoria() {
        return categoriaPersonalizada;
    }

    @Override
    public String getDetalleEspecifico() {
        return "Categoría general";
    }

    public void setCategoriaPersonalizada(String categoriaPersonalizada) {
        this.categoriaPersonalizada = categoriaPersonalizada;
    }
}
