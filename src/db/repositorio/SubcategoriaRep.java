package db.repositorio;

import db.config.Conexion;
import db.modelo.Subcategoria;
import tda.ListaEnlazada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SubcategoriaRep {

    // Método para insertar una subcategoría
    public void insertarSubcategoria(Subcategoria subcategoria) throws SQLException {
        String sql = "INSERT INTO subcategoria (subcategoria_id, descripcion, categoria_id) VALUES ("
                + subcategoria.getSubcategoria_Id() + ", '"
                + subcategoria.getDescripcion() + "', "
                + subcategoria.getCategoria_Id() + ")";
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Subcategoría insertada: " + subcategoria);
        }
    }

    // Método para listar todas las subcategorías
    public ListaEnlazada<Subcategoria> listarSubcategorias() throws SQLException {
        String sql = "SELECT * FROM subcategoria";
        ListaEnlazada<Subcategoria> subcategorias = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Subcategoria subcategoria = new Subcategoria(
                        rs.getInt("subcategoria_id"),
                        rs.getString("descripcion"),
                        rs.getInt("categoria_id")
                );
                subcategorias.insertar(subcategoria);
            }
        }
        return subcategorias;
    }

    // Método para buscar una subcategoría por su ID
    public Subcategoria buscarSubcategoriaPorId(int id) throws SQLException {
        String sql = "SELECT * FROM subcategoria WHERE subcategoria_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Subcategoria(
                        rs.getInt("subcategoria_id"),
                        rs.getString("descripcion"),
                        rs.getInt("categoria_id")
                );
            }
        }
        return null; // Si no se encuentra
    }

    // Método para actualizar una subcategoría
    public void actualizarSubcategoria(Subcategoria subcategoria) throws SQLException {
        String sql = "UPDATE subcategoria SET descripcion = '"
                + subcategoria.getDescripcion() + "', categoria_id = "
                + subcategoria.getCategoria_Id() + " WHERE subcategoria_id = "
                + subcategoria.getSubcategoria_Id();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Subcategoría actualizada: " + subcategoria);
        }
    }

    // Método para eliminar una subcategoría
    public void eliminarSubcategoria(int id) throws SQLException {
        String sql = "DELETE FROM subcategoria WHERE subcategoria_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Subcategoría eliminada con ID: " + id);
        }
    }
}