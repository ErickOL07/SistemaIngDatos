package db.repositorio;

import db.config.Conexion;
import db.modelo.Detalle_Venta;
import tda.ListaEnlazada;

import java.sql.*;

public class DetalleVentaRep {

    private int obtenerConcatenadaId(String codigo) throws SQLException {
        String query = "SELECT concatenada_id FROM concatenada WHERE descripcion = ?";
        try (Connection conexion = Conexion.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("concatenada_id");
            } else {
                throw new SQLException("No se encontró un registro en la tabla concatenada con el código: " + codigo);
            }
        }
    }

    public void insertarOActualizarDetalleVenta(Detalle_Venta detalleVenta) throws SQLException {
        String verificarSql = "SELECT cantidad FROM detalle_venta WHERE venta_id = ? AND producto_id = ?";
        String actualizarSql = "UPDATE detalle_venta SET cantidad = cantidad + ? WHERE venta_id = ? AND producto_id = ?";
        String insertarSql = "INSERT INTO detalle_venta (cantidad, concatenada_id, venta_id, producto_id) VALUES (?, ?, ?, ?)";

        try (Connection conexion = Conexion.getConexion()) {
            try (PreparedStatement verificarStmt = conexion.prepareStatement(verificarSql)) {
                verificarStmt.setInt(1, detalleVenta.getVentaId());
                verificarStmt.setInt(2, detalleVenta.getProductoId());
                ResultSet rs = verificarStmt.executeQuery();

                if (rs.next()) {
                    try (PreparedStatement actualizarStmt = conexion.prepareStatement(actualizarSql)) {
                        actualizarStmt.setInt(1, detalleVenta.getCantidad());
                        actualizarStmt.setInt(2, detalleVenta.getVentaId());
                        actualizarStmt.setInt(3, detalleVenta.getProductoId());
                        actualizarStmt.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertarStmt = conexion.prepareStatement(insertarSql)) {
                        insertarStmt.setInt(1, detalleVenta.getCantidad());
                        insertarStmt.setInt(2, obtenerConcatenadaId(detalleVenta.getConcatenadaDesc()));
                        insertarStmt.setInt(3, detalleVenta.getVentaId());
                        insertarStmt.setInt(4, detalleVenta.getProductoId());
                        insertarStmt.executeUpdate();
                    }
                }
            }
        }
    }


}
