package db.repositorio;

import db.config.Conexion;
import db.modelo.Producto;
import tda.ListaEnlazada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductoRep {

    // Método para insertar un producto
    public void insertarProducto(Producto producto) throws SQLException {
        String sql = "SET DEFINE OFF; INSERT INTO producto (producto_id, descripcion, precio_unitario, stock, imagen_referencial, subcategoria_id) VALUES ("
                + producto.getProducto_Id() + ", '"
                + producto.getDescripcion() + "', "
                + producto.getPrecio_Unitario() + ", "
                + producto.getStock() + ", '"
                + producto.getImagen_Referencial() + "', "
                + producto.getSubcategoria_Id() + "); SET DEFINE ON;";
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Producto insertado: " + producto);
        }
    }

    // Método para listar todos los productos
    public ListaEnlazada<Producto> listarProductos() throws SQLException {
        String sql = "SELECT * FROM producto";
        ListaEnlazada<Producto> productos = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("producto_id"),
                        rs.getString("descripcion"),
                        rs.getFloat("precio_unitario"),
                        rs.getInt("stock"),
                        rs.getString("imagen_referencial"),
                        rs.getInt("subcategoria_id")
                );
                productos.insertar(producto);
            }
        }
        return productos;
    }

    // Método para buscar un producto por su ID
    public Producto buscarProductoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM producto WHERE producto_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Producto(
                        rs.getInt("producto_id"),
                        rs.getString("descripcion"),
                        rs.getFloat("precio_unitario"),
                        rs.getInt("stock"),
                        rs.getString("imagen_referencial"),
                        rs.getInt("subcategoria_id")
                );
            }
        }
        return null; // Si no se encuentra
    }

    // Método para actualizar un producto
    public void actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET descripcion = '"
                + producto.getDescripcion() + "', precio_unitario = "
                + producto.getPrecio_Unitario() + ", stock = "
                + producto.getStock() + ", imagen_referencial = '"
                + producto.getImagen_Referencial() + "', subcategoria_id = "
                + producto.getSubcategoria_Id() + " WHERE producto_id = "
                + producto.getProducto_Id();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Producto actualizado: " + producto);
        }
    }

    // Método para eliminar un producto
    public void eliminarProducto(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE producto_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Producto eliminado con ID: " + id);
        }
    }
}
