package db.repositorio;

import db.config.Conexion;
import db.modelo.Categoria;
import tda.ListaEnlazada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CategoriaRep {

    // Método para insertar una categoría
    public void insertarCategoria(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (categoria_id, descripcion) VALUES (" 
                + categoria.getCategoria_Id() + ", '" 
                + categoria.getDescripcion() + "')";
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Categoría insertada: " + categoria);
        }
    }

    // Método para listar todas las categorías
    public ListaEnlazada<Categoria> listarCategorias() throws SQLException {
        String sql = "SELECT * FROM categoria";
        ListaEnlazada<Categoria> categorias = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("categoria_id"),
                        rs.getString("descripcion")
                );
                categorias.insertar(categoria);
            }
        }
        return categorias;
    }

    // Método para buscar una categoría por su ID
    public Categoria buscarCategoriaPorId(int id) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE categoria_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Categoria(
                        rs.getInt("categoria_id"),
                        rs.getString("descripcion")
                );
            }
        }
        return null; // Si no se encuentra
    }

    // Método para actualizar una categoría
    public void actualizarCategoria(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET descripcion = '" 
                + categoria.getDescripcion() + "' WHERE categoria_id = " 
                + categoria.getCategoria_Id();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Categoría actualizada: " + categoria);
        }
    }

    // Método para eliminar una categoría
    public void eliminarCategoria(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE categoria_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Categoría eliminada con ID: " + id);
        }
    }
}
