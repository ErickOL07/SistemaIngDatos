package db.repositorio;

import db.config.Conexion;
import db.modelo.Producto;
import tda.ListaEnlazada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public class ProductoRep {

    public void actualizarProducto(Producto producto) throws SQLException {
        String sql = "{CALL actualizar_producto(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conexion = Conexion.getConexion();
             CallableStatement stmt = conexion.prepareCall(sql)) {

            stmt.setInt(1, producto.getProducto_Id());
            stmt.setString(2, producto.getDescripcion());
            stmt.setFloat(3, producto.getPrecio_Unitario());
            stmt.setInt(4, producto.getStock());
            stmt.setString(5, producto.getImagen_Referencial());
            stmt.setString(6, producto.getSubcategoria());
            stmt.setString(7, producto.getCategoria());

            stmt.execute();
            System.out.println("Producto actualizado correctamente.");
        } catch (SQLException ex) {
            System.err.println("Error al actualizar el producto: " + ex.getMessage());
            throw ex;
        }
    }

    public ListaEnlazada<Producto> obtenerDetallesProductos() throws SQLException {
        String sql = "{CALL obtener_detalles_producto}";
        ListaEnlazada<Producto> productos = new ListaEnlazada<>();

        try (Connection conexion = Conexion.getConexion();
             CallableStatement stmt = conexion.prepareCall(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getString("descripcion_concatenada"),
                        rs.getString("categoria"),
                        rs.getString("subcategoria"),
                        rs.getString("descripcion_producto"),
                        rs.getFloat("precio_unitario_producto"),
                        rs.getInt("stock_producto")
                );
                productos.insertar(producto);
            }
        }
        return productos;
    }

    public ListaEnlazada<String> obtenerSubcategoriasPorCategoria(String categoria) throws SQLException {
        String sql = "SELECT s.descripcion FROM subcategoria s " +
                "JOIN categoria c ON s.categoria_id = c.categoria_id " +
                "WHERE c.descripcion = ?";
        ListaEnlazada<String> subcategorias = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subcategorias.insertar(rs.getString("descripcion"));
            }
        }
        return subcategorias;
    }

    public void eliminarProducto(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE producto_id = ?";
        try (Connection conexion = Conexion.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Producto eliminado con ID: " + id);
        }
    }

    public Integer buscarProductoIdPorConcatenada(String descripcionConcatenada) throws SQLException {
        String sql = "SELECT dbo.buscar_producto_id_por_concatenada(?) AS producto_id";
        Integer productoId = null;

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, descripcionConcatenada);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    productoId = rs.getInt("producto_id");
                    if (rs.wasNull()) {
                        productoId = null;
                    }
                }
            }
        }
        return productoId;
    }

    public Producto buscarProductoPorId(Integer productoId) throws SQLException {
        String sql = "SELECT * FROM buscar_producto_por_id(?)";
        Producto producto = null;

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                producto = new Producto(
                        rs.getInt("producto_id"),
                        rs.getString("descripcion"),
                        rs.getFloat("precio_unitario"),
                        rs.getInt("stock"),
                        rs.getString("imagen_referencial"),
                        rs.getInt("subcategoria_id")
                );
                producto.setSubcategoria(rs.getString("subcategoria"));
                producto.setCategoria(rs.getString("categoria"));
            }
        }
        return producto;
    }

    public Producto buscarProductoPorConcatenada2(String codigo) throws SQLException {
        String sqlFunction = "SELECT dbo.buscar_producto_id_por_concatenada(?) AS producto_id";
        int productoId;

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(sqlFunction)) {

            stmt.setString(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    productoId = rs.getInt("producto_id");
                    if (rs.wasNull()) {
                        throw new SQLException("No se encontró un registro en la tabla concatenada con la descripción: " + codigo);
                    }
                } else {
                    throw new SQLException("No se encontró un registro en la tabla concatenada con la descripción: " + codigo);
                }
            }
        }

        String sqlDetails = "SELECT * FROM producto p " +
                "JOIN concatenada c ON p.producto_id = c.producto_id " +
                "WHERE p.producto_id = ?";

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(sqlDetails)) {

            stmt.setInt(1, productoId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Producto producto = new Producto(
                            rs.getInt("producto_id"),
                            rs.getString("descripcion"),
                            rs.getFloat("precio_unitario"),
                            rs.getInt("stock"),
                            rs.getString("imagen_referencial"),
                            rs.getInt("subcategoria_id")
                    );
                    producto.setConcatenadaDesc(rs.getString("descripcion"));
                    return producto;
                } else {
                    throw new SQLException("Producto no encontrado con producto_id: " + productoId);
                }
            }
        }
    }

}
