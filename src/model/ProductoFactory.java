package model;

/**
 * Fábrica de productos que implementa el patrón Factory Method.
 * Encapsula la lógica de instanciación de productos según la categoría seleccionada.
 */
public class ProductoFactory {

    /**
     * Crea y retorna una instancia concreta de Producto según la categoría especificada.
     *
     * @param categoria Nombre de la categoría (ej. "Bebida", "Abarrote", o categorías nuevas)
     * @param id Identificador único del producto
     * @param nombre Nombre descriptivo del producto
     * @param precio Precio unitario en Soles (S/)
     * @param stock Cantidad disponible en inventario
     * @return Instancia concreta de Producto (ProductoBebida, ProductoAbarrote o ProductoGenerico)
     */
    public static Producto crearProducto(String categoria, int id, String nombre, double precio, int stock) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar una categoría válida.");
        }

        String catLimpia = categoria.trim();
        String catLower = catLimpia.toLowerCase();

        if (catLower.contains("bebida")) {
            return new ProductoBebida(id, nombre, precio, stock, false);
        } else if (catLower.contains("abarrote")) {
            return new ProductoAbarrote(id, nombre, precio, stock, "Sin fecha");
        } else {
            // Para categorías agregadas dinámicamente mediante "+ Categoría"
            return new ProductoGenerico(id, nombre, precio, stock, catLimpia);
        }
    }

    /**
     * Versión sobrecargada que permite asignar atributos específicos según la categoría.
     *
     * @param categoria Nombre de la categoría
     * @param id Identificador único
     * @param nombre Nombre del producto
     * @param precio Precio en S/
     * @param stock Cantidad en stock
     * @param detalleExtra Atributo extra (ej: Boolean para retornable, String para fecha de caducidad)
     * @return Instancia de Producto
     */
    public static Producto crearProducto(String categoria, int id, String nombre, double precio, int stock, Object detalleExtra) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar una categoría válida.");
        }

        String catLimpia = categoria.trim();
        String catLower = catLimpia.toLowerCase();

        if (catLower.contains("bebida")) {
            boolean esRetornable = false;
            if (detalleExtra instanceof Boolean) {
                esRetornable = (Boolean) detalleExtra;
            } else if (detalleExtra != null) {
                esRetornable = Boolean.parseBoolean(detalleExtra.toString());
            }
            return new ProductoBebida(id, nombre, precio, stock, esRetornable);
        } else if (catLower.contains("abarrote")) {
            String fechaCaducidad = (detalleExtra != null) ? detalleExtra.toString() : "Sin fecha";
            return new ProductoAbarrote(id, nombre, precio, stock, fechaCaducidad);
        } else {
            return new ProductoGenerico(id, nombre, precio, stock, catLimpia);
        }
    }
}
