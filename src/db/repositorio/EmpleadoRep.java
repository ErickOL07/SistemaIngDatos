package db.repositorio;

import db.config.Conexion;
import db.modelo.Empleado;
import tda.ListaEnlazada;

import java.sql.*;

public class EmpleadoRep {

    public int insertarEmpleado(Empleado empleado) throws SQLException {
        String sql = "{CALL sp_insertar_empleado(?, ?, ?, ?, ?, ?)}";
        try (Connection conexion = Conexion.getConexion();
             CallableStatement stmt = conexion.prepareCall(sql)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getCorreo_Electronico());
            stmt.setString(3, empleado.getContrasena());
            stmt.setInt(4, empleado.getRol_Id());
            stmt.setString(5, empleado.getNumero_Documento());

            stmt.registerOutParameter(6, java.sql.Types.INTEGER);

            stmt.execute();

            int empleadoId = stmt.getInt(6);
            if (empleadoId == -1) {
                throw new SQLException("Error al insertar empleado.");
            }

            System.out.println("Empleado insertado con ID: " + empleadoId);
            return empleadoId;
        }
    }

    public ListaEnlazada<Empleado> listarEmpleados() throws SQLException {
        String sql = "SELECT * FROM empleado";
        ListaEnlazada<Empleado> empleados = new ListaEnlazada<>();

        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Empleado empleado = new Empleado(
                        rs.getInt("empleado_id"),
                        rs.getString("nombre"),
                        rs.getString("correo_electronico"),
                        rs.getString("contrasena"),
                        rs.getInt("rol_id"),
                        rs.getString("numero_documento")
                );
                empleados.insertar(empleado);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar empleados: " + e.getMessage());
            throw e;
        }

        return empleados;
    }

    public Empleado buscarEmpleadoPorCorreo(String correo) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE correo_electronico = '" + correo + "'";
        try (Connection conexion = Conexion.conectar();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Empleado(
                        rs.getInt("empleado_id"),
                        rs.getString("nombre"),
                        rs.getString("correo_electronico"),
                        rs.getString("contrasena"),
                        rs.getInt("rol_id"),
                        rs.getString("numero_documento")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar empleado por correo: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void actualizarEmpleado(Empleado empleado) throws SQLException {
        String sql = "UPDATE empleado SET nombre = '"
                + empleado.getNombre() + "', correo_electronico = '"
                + empleado.getCorreo_Electronico() + "', contrasena = '"
                + empleado.getContrasena() + "', rol_id = "
                + empleado.getRol_Id() + ", numero_documento = '"
                + empleado.getNumero_Documento() + "' WHERE empleado_id = "
                + empleado.getEmpleado_Id();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Empleado actualizado: " + empleado);
        }
    }

    public void eliminarEmpleado(int id) throws SQLException {
        String sql = "DELETE FROM empleado WHERE empleado_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Empleado eliminado con ID: " + id);
        }
    }
}
