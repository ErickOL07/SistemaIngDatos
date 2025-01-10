package db.repositorio;

import db.config.Conexion;
import db.modelo.Venta;
import tda.ListaEnlazada;

import java.sql.*;

public class VentaRep {

    public int insertarVenta(Venta venta) throws SQLException {
        String sql = "{CALL sp_insertar_venta(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conexion = Conexion.getConexion();
             CallableStatement stmt = conexion.prepareCall(sql)) {

            stmt.setFloat(1, venta.getMonto_Total());
            stmt.setFloat(2, venta.getDescuento());
            stmt.setDate(3, Date.valueOf(venta.getFecha_Venta()));
            stmt.setInt(4, venta.getCliente_Id());
            stmt.setInt(5, venta.getEmpleado_Id());
            stmt.setInt(6, venta.getMetodo_Pago_Id());
            stmt.setInt(7, venta.getForma_Pago_Id());

            stmt.registerOutParameter(8, java.sql.Types.INTEGER);

            stmt.execute();

            int ventaId = stmt.getInt(8);
            System.out.println("ID de la venta generado: " + ventaId);
            venta.setVenta_Id(ventaId); // Actualiza el ID en el objeto Venta
            return ventaId;
        }
    }

    public void eliminarVenta(int id) throws SQLException {
        String sql = "DELETE FROM venta WHERE venta_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Venta eliminada con ID: " + id);
        }
    }

    public ResultSet generarReporteVenta(int ventaId) throws SQLException {
        String sql = "{CALL sp_generar_reporte_venta(?)}";
        Connection conexion = Conexion.getConexion();
        CallableStatement stmt = conexion.prepareCall(sql);
        stmt.setInt(1, ventaId);
        return stmt.executeQuery();
    }


    public int obtenerUltimaVentaId() throws SQLException {
        String sql = "SELECT MAX(venta_id) AS ultima_id FROM venta";
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("ultima_id");
            }
        }
        return -1;
    }


}
