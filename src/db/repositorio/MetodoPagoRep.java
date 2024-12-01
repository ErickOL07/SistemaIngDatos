package db.repositorio;

import db.config.Conexion;
import db.modelo.Metodo_Pago;
import tda.ListaEnlazada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MetodoPagoRep {

    // Método para insertar un método de pago
    public void insertarMetodoPago(Metodo_Pago metodoPago) throws SQLException {
        String sql = "INSERT INTO metodo_pago (metodo_pago_id, descripcion) VALUES ("
                + metodoPago.getMetodo_Pago_Id() + ", '"
                + metodoPago.getDescripcion() + "')";
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Método de pago insertado: " + metodoPago);
        }
    }

    // Método para listar todos los métodos de pago
    public ListaEnlazada<Metodo_Pago> listarMetodosPago() throws SQLException {
        String sql = "SELECT * FROM metodo_pago";
        ListaEnlazada<Metodo_Pago> metodos = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Metodo_Pago metodo = new Metodo_Pago(
                        rs.getInt("metodo_pago_id"),
                        rs.getString("descripcion")
                );
                metodos.insertar(metodo);
            }
        }
        return metodos;
    }

    // Método para buscar un método de pago por su ID
    public Metodo_Pago buscarMetodoPagoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM metodo_pago WHERE metodo_pago_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Metodo_Pago(
                        rs.getInt("metodo_pago_id"),
                        rs.getString("descripcion")
                );
            }
        }
        return null; // Si no se encuentra
    }

    // Método para actualizar un método de pago
    public void actualizarMetodoPago(Metodo_Pago metodoPago) throws SQLException {
        String sql = "UPDATE metodo_pago SET descripcion = '"
                + metodoPago.getDescripcion() + "' WHERE metodo_pago_id = "
                + metodoPago.getMetodo_Pago_Id();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Método de pago actualizado: " + metodoPago);
        }
    }

    // Método para eliminar un método de pago
    public void eliminarMetodoPago(int id) throws SQLException {
        String sql = "DELETE FROM metodo_pago WHERE metodo_pago_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Método de pago eliminado con ID: " + id);
        }
    }
}
