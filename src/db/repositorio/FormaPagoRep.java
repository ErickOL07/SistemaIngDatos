package db.repositorio;

import db.config.Conexion;

import java.sql.*;

public class FormaPagoRep {

    public int obtenerFormaPagoIdPorDescripcion(String descripcion) throws SQLException {
        String sql = "SELECT forma_pago_id FROM forma_pago WHERE descripcion = ?";
        try (Connection conexion = Conexion.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, descripcion);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("forma_pago_id");
            } else {
                throw new SQLException("No se encontró la forma de pago: " + descripcion);
            }
        }
    }
}
