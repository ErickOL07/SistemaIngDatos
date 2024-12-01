package db.repositorio;

import db.config.Conexion;
import db.modelo.Empleado;
import tda.ListaEnlazada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmpleadoRep {

    // Método para insertar un empleado
    public void insertarEmpleado(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO empleado (empleado_id, nombre, correo_electronico, contrasena, rol_id, numero_documento) VALUES ("
                + empleado.getEmpleado_Id() + ", '"
                + empleado.getNombre() + "', '"
                + empleado.getCorreo_Electronico() + "', '"
                + empleado.getContrasena() + "', "
                + empleado.getRol_Id() + ", '"
                + empleado.getNumero_Documento() + "')";
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Empleado insertado: " + empleado);
        }
    }

    // Método para listar todos los empleados
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
        }
        return empleados;
    }

    // Método para buscar un empleado por su ID
    public Empleado buscarEmpleadoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE empleado_id = " + id;
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
        }
        return null; // Si no se encuentra
    }

    public Empleado buscarEmpleadoPorCorreo(String correo) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE correo_electronico = '" + correo + "'"; // Concatenación directa
        try (Connection conexion = Conexion.conectar(); // Usa una nueva conexión
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) { // Ejecuta el SQL

            if (rs.next()) { // Si encuentra resultados
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
        return null; // Si no se encuentra ningún empleado
    }



    // Método para actualizar un empleado
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

    // Método para eliminar un empleado
    public void eliminarEmpleado(int id) throws SQLException {
        String sql = "DELETE FROM empleado WHERE empleado_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Empleado eliminado con ID: " + id);
        }
    }
}
