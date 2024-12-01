package db.repositorio;

import db.config.Conexion;
import db.modelo.Detalle_Venta;
import tda.ListaEnlazada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DetalleVentaRep {

    // Método para insertar un detalle de venta
    public void insertarDetalleVenta(Detalle_Venta detalleVenta) throws SQLException {
        String sql = "INSERT INTO detalle_venta (venta_id, producto_id, cantidad) VALUES ("
                + detalleVenta.getVenta_Id() + ", "
                + detalleVenta.getProducto_Id() + ", "
                + detalleVenta.getCantidad() + ")";
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Detalle de venta insertado: " + detalleVenta);
        }
    }

    // Método para listar todos los detalles de venta
    public ListaEnlazada<Detalle_Venta> listarDetallesVenta() throws SQLException {
        String sql = "SELECT * FROM detalle_venta";
        ListaEnlazada<Detalle_Venta> detalles = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Detalle_Venta detalle = new Detalle_Venta(
                        rs.getInt("venta_id"),
                        rs.getInt("producto_id"),
                        rs.getInt("cantidad")
                );
                detalles.insertar(detalle);
            }
        }
        return detalles;
    }

    // Método para buscar un detalle de venta por Venta_Id
    public ListaEnlazada<Detalle_Venta> buscarDetallePorVentaId(int ventaId) throws SQLException {
        String sql = "SELECT * FROM detalle_venta WHERE venta_id = " + ventaId;
        ListaEnlazada<Detalle_Venta> detalles = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Detalle_Venta detalle = new Detalle_Venta(
                        rs.getInt("venta_id"),
                        rs.getInt("producto_id"),
                        rs.getInt("cantidad")
                );
                detalles.insertar(detalle);
            }
        }
        return detalles;
    }

    // Método para actualizar un detalle de venta
    public void actualizarDetalleVenta(Detalle_Venta detalleVenta) throws SQLException {
        String sql = "UPDATE detalle_venta SET cantidad = " 
                + detalleVenta.getCantidad() + " WHERE venta_id = " 
                + detalleVenta.getVenta_Id() + " AND producto_id = " 
                + detalleVenta.getProducto_Id();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Detalle de venta actualizado: " + detalleVenta);
        }
    }

    // Método para eliminar un detalle de venta
    public void eliminarDetalleVenta(int ventaId, int productoId) throws SQLException {
        String sql = "DELETE FROM detalle_venta WHERE venta_id = " 
                + ventaId + " AND producto_id = " + productoId;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Detalle de venta eliminado con Venta_Id: " + ventaId + " y Producto_Id: " + productoId);
        }
    }
}
