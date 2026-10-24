package model;

/**
 * Subclase que representa productos de la categoría Bebida.
 * Añade la propiedad 'esRetornable' habitual en bodegas.
 */
public class ProductoBebida extends Producto {
    private boolean esRetornable;

    public ProductoBebida(int id, String nombre, double precio, int stock, boolean esRetornable) {
        super(id, nombre, precio, stock);
        this.esRetornable = esRetornable;
    }

    public ProductoBebida(int id, String nombre, double precio, int stock) {
        this(id, nombre, precio, stock, false);
    }

    @Override
    public String getCategoria() {
        return "Bebida";
    }

    @Override
    public String getDetalleEspecifico() {
        return esRetornable ? "Retornable" : "No retornable";
    }

    public boolean isEsRetornable() {
        return esRetornable;
    }

    public void setEsRetornable(boolean esRetornable) {
        this.esRetornable = esRetornable;
    }
}
