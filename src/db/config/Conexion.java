package db.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:oracle:thin:@0.tcp.sa.ngrok.io:19407:xe";
    private static final String USER = "sys as sysdba";
    private static final String PASSWORD = "123";

    private static Connection conexion;

    public static Connection conectar() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                Class.forName("oracle.jdbc.OracleDriver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión exitosa a la base de datos.");
            } catch (ClassNotFoundException e) {
                System.err.println("Error al cargar el driver JDBC: " + e.getMessage());
                throw new SQLException("No se encontró el driver JDBC.");
            }
        }
        return conexion;
    }

    public static void cerrar() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
            System.out.println("Conexión cerrada.");
        }
    }

    // Método para obtener una nueva conexión
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public static void setConexion(Connection conexion) {
        Conexion.conexion = conexion;
    }
    
    
}
