package db.repositorio;

import db.config.Conexion;
import db.modelo.Metodo_Pago;
import tda.ListaEnlazada;

import java.sql.*;

public class MetodoPagoRep {

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

    public int obtenerMetodoPagoIdPorDescripcion(String descripcion) throws SQLException {
        String sql = "SELECT metodo_pago_id FROM metodo_pago WHERE descripcion = ?";
        try (Connection conexion = Conexion.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, descripcion);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("metodo_pago_id");
            } else {
                throw new SQLException("No se encontró el método de pago: " + descripcion);
            }
        }
    }


}
