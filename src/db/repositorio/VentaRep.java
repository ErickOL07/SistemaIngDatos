package db.repositorio;

import db.config.Conexion;
import db.modelo.Venta;
import tda.ListaEnlazada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VentaRep {

    // Método para insertar una venta
    public void insertarVenta(Venta venta) throws SQLException {
        String sql = "INSERT INTO venta (venta_id, monto_total, fecha_venta, cliente_id, empleado_id, metodo_pago_id) VALUES ("
                + venta.getVenta_Id() + ", "
                + venta.getMonto_Total() + ", '"
                + venta.getFecha_Venta() + "', "
                + venta.getCliente_Id() + ", "
                + venta.getEmpleado_Id() + ", "
                + venta.getMetodo_Pago_Id() + ")";
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Venta insertada: " + venta);
        }
    }

    // Método para listar todas las ventas
    public ListaEnlazada<Venta> listarVentas() throws SQLException {
        String sql = "SELECT * FROM venta";
        ListaEnlazada<Venta> ventas = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Venta venta = new Venta(
                        rs.getInt("venta_id"),
                        rs.getFloat("monto_total"),
                        rs.getString("fecha_venta"),
                        rs.getInt("cliente_id"),
                        rs.getInt("empleado_id"),
                        rs.getInt("metodo_pago_id")
                );
                ventas.insertar(venta);
            }
        }
        return ventas;
    }

    // Método para buscar una venta por su ID
    public Venta buscarVentaPorId(int id) throws SQLException {
        String sql = "SELECT * FROM venta WHERE venta_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Venta(
                        rs.getInt("venta_id"),
                        rs.getFloat("monto_total"),
                        rs.getString("fecha_venta"),
                        rs.getInt("cliente_id"),
                        rs.getInt("empleado_id"),
                        rs.getInt("metodo_pago_id")
                );
            }
        }
        return null; // Si no se encuentra
    }

    // Método para actualizar una venta
    public void actualizarVenta(Venta venta) throws SQLException {
        String sql = "UPDATE venta SET monto_total = "
                + venta.getMonto_Total() + ", fecha_venta = '"
                + venta.getFecha_Venta() + "', cliente_id = "
                + venta.getCliente_Id() + ", empleado_id = "
                + venta.getEmpleado_Id() + ", metodo_pago_id = "
                + venta.getMetodo_Pago_Id() + " WHERE venta_id = "
                + venta.getVenta_Id();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Venta actualizada: " + venta);
        }
    }

    // Método para eliminar una venta
    public void eliminarVenta(int id) throws SQLException {
        String sql = "DELETE FROM venta WHERE venta_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Venta eliminada con ID: " + id);
        }
    }
}