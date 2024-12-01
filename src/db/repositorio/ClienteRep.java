package db.repositorio;

import db.config.Conexion;
import db.modelo.Cliente;
import tda.ListaEnlazada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClienteRep {

    // Método para insertar un cliente
    public void insertarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (cliente_id, nombre_razon_social, puntos, tipo_documento_id, numero_documento) VALUES (" 
                + cliente.getCliente_Id() + ", '" 
                + cliente.getNombre_Razon_Social() + "', " 
                + cliente.getPuntos() + ", " 
                + cliente.getTipo_Documento_Id() + ", '" 
                + cliente.getNumero_Documento() + "')";
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Cliente insertado: " + cliente);
        }
    }

    // Método para listar todos los clientes
    public ListaEnlazada<Cliente> listarClientes() throws SQLException {
        String sql = "SELECT * FROM cliente";
        ListaEnlazada<Cliente> clientes = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("cliente_id"),
                        rs.getString("nombre_razon_social"),
                        rs.getFloat("puntos"),
                        rs.getInt("tipo_documento_id"),
                        rs.getString("numero_documento")
                );
                clientes.insertar(cliente);
            }
        }
        return clientes;
    }

    // Método para buscar un cliente por su ID
    public Cliente buscarClientePorId(int id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE cliente_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("cliente_id"),
                        rs.getString("nombre_razon_social"),
                        rs.getFloat("puntos"),
                        rs.getInt("tipo_documento_id"),
                        rs.getString("numero_documento")
                );
            }
        }
        return null; // Si no se encuentra
    }

    // Método para actualizar un cliente
    public void actualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombre_razon_social = '" 
                + cliente.getNombre_Razon_Social() + "', puntos = " 
                + cliente.getPuntos() + ", tipo_documento_id = " 
                + cliente.getTipo_Documento_Id() + ", numero_documento = '" 
                + cliente.getNumero_Documento() + "' WHERE cliente_id = " 
                + cliente.getCliente_Id();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Cliente actualizado: " + cliente);
        }
    }

    // Método para eliminar un cliente
    public void eliminarCliente(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE cliente_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Cliente eliminado con ID: " + id);
        }
    }
}
