    package db.repositorio;

    import db.config.Conexion;
    import db.modelo.Cliente;
    import tda.ListaEnlazada;

    import java.sql.*;

    public class ClienteRep {

        public int insertarCliente(Cliente cliente) throws SQLException {
            String sql = "{CALL sp_insertar_cliente(?, ?, ?, ?, ?)}";
            try (Connection conexion = Conexion.getConexion();
                 CallableStatement stmt = conexion.prepareCall(sql)) {

                stmt.setString(1, cliente.getNombre_Razon_Social());
                stmt.setFloat(2, cliente.getPuntos());
                stmt.setInt(3, cliente.getTipo_Documento_Id());
                stmt.setString(4, cliente.getNumero_Documento());

                stmt.registerOutParameter(5, java.sql.Types.INTEGER);

                stmt.execute();

                int clienteId = stmt.getInt(5);
                if (clienteId == -1) {
                    throw new SQLException("Error al insertar cliente.");
                }

                System.out.println("Cliente insertado con ID: " + clienteId);
                return clienteId;
            }
        }

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

        public Cliente buscarClientePorId(int id) throws SQLException {
            String sql = "SELECT * FROM cliente WHERE cliente_id = " + id;
            try (Connection conexion = Conexion.getConexion();
                 Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("cliente_id"),
                            rs.getString("nombre_razon_social"),
                            rs.getInt("puntos"),
                            rs.getInt("tipo_documento_id"),
                            rs.getString("numero_documento")
                    );
                }
            }
            return null;
        }


        public void actualizarPuntosCliente(int clienteId, float puntos) throws SQLException {
            String sql = "{CALL sp_actualizar_puntos_cliente(?, ?)}";
            try (Connection conexion = Conexion.getConexion();
                 CallableStatement stmt = conexion.prepareCall(sql)) {

                stmt.setInt(1, clienteId);
                stmt.setFloat(2, puntos);
                stmt.execute();
            }
        }


    }
