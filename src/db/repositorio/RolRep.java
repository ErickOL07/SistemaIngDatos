package db.repositorio;

import db.config.Conexion;
import db.modelo.Rol;
import tda.ListaEnlazada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RolRep {

    // Método para insertar un rol
    public void insertarRol(Rol rol) throws SQLException {
        String sql = "INSERT INTO rol (rol_id, descripcion) VALUES ("
                + rol.getRol_Id() + ", '"
                + rol.getDescripcion() + "')";
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Rol insertado: " + rol);
        }
    }

    // Método para listar todos los roles
    public ListaEnlazada<Rol> listarRoles() throws SQLException {
        String sql = "SELECT * FROM rol";
        ListaEnlazada<Rol> roles = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Rol rol = new Rol(
                        rs.getInt("rol_id"),
                        rs.getString("descripcion")
                );
                roles.insertar(rol);
            }
        }
        return roles;
    }

    // Método para buscar un rol por su ID
    public Rol buscarRolPorId(int id) throws SQLException {
        String sql = "SELECT * FROM rol WHERE rol_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Rol(
                        rs.getInt("rol_id"),
                        rs.getString("descripcion")
                );
            }
        }
        return null; // Si no se encuentra
    }

    // Método para actualizar un rol
    public void actualizarRol(Rol rol) throws SQLException {
        String sql = "UPDATE rol SET descripcion = '"
                + rol.getDescripcion() + "' WHERE rol_id = "
                + rol.getRol_Id();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Rol actualizado: " + rol);
        }
    }

    // Método para eliminar un rol
    public void eliminarRol(int id) throws SQLException {
        String sql = "DELETE FROM rol WHERE rol_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Rol eliminado con ID: " + id);
        }
    }
}
