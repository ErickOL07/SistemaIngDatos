package db.repositorio;

import db.config.Conexion;
import db.modelo.Tipo_Documento;
import tda.ListaEnlazada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TipoDocumentoRep {

    // Método para insertar un tipo de documento
    public void insertarTipoDocumento(Tipo_Documento tipoDocumento) throws SQLException {
        String sql = "INSERT INTO tipo_documento (tipo_documento_id, descripcion) VALUES ("
                + tipoDocumento.getTipo_Documento_Id() + ", '"
                + tipoDocumento.getDescripcion() + "')";
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tipo de documento insertado: " + tipoDocumento);
        }
    }

    // Método para listar todos los tipos de documento
    public ListaEnlazada<Tipo_Documento> listarTiposDocumento() throws SQLException {
        String sql = "SELECT * FROM tipo_documento";
        ListaEnlazada<Tipo_Documento> tipos = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tipo_Documento tipo = new Tipo_Documento(
                        rs.getInt("tipo_documento_id"),
                        rs.getString("descripcion")
                );
                tipos.insertar(tipo);
            }
        }
        return tipos;
    }

    // Método para buscar un tipo de documento por su ID
    public Tipo_Documento buscarTipoDocumentoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tipo_documento WHERE tipo_documento_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Tipo_Documento(
                        rs.getInt("tipo_documento_id"),
                        rs.getString("descripcion")
                );
            }
        }
        return null; // Si no se encuentra
    }

    // Método para actualizar un tipo de documento
    public void actualizarTipoDocumento(Tipo_Documento tipoDocumento) throws SQLException {
        String sql = "UPDATE tipo_documento SET descripcion = '"
                + tipoDocumento.getDescripcion() + "' WHERE tipo_documento_id = "
                + tipoDocumento.getTipo_Documento_Id();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tipo de documento actualizado: " + tipoDocumento);
        }
    }

    // Método para eliminar un tipo de documento
    public void eliminarTipoDocumento(int id) throws SQLException {
        String sql = "DELETE FROM tipo_documento WHERE tipo_documento_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tipo de documento eliminado con ID: " + id);
        }
    }
}