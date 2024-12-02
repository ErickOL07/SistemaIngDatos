package db.repositorio;

import db.config.Conexion;
import db.modelo.Producto;
import tda.ListaEnlazada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;

public class ProductoRep {

    // Método para ejecutar el procedimiento almacenado actualizar_concatenada
    public void ejecutarActualizarConcatenada() throws SQLException {
        String sql = "{CALL actualizar_concatenada()}";
        try (Connection conexion = Conexion.getConexion();
             CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.execute();
            System.out.println("Procedimiento 'actualizar_concatenada' ejecutado correctamente.");
        }
    }

    // Método para insertar un producto
    public void insertarProducto(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto (producto_id, descripcion, precio_unitario, stock, imagen_referencial, subcategoria_id) VALUES ("
                + producto.getProducto_Id() + ", '"
                + producto.getDescripcion() + "', "
                + producto.getPrecio_Unitario() + ", "
                + producto.getStock() + ", '"
                + producto.getImagen_Referencial() + "', "
                + producto.getSubcategoria_Id() + ")";
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Producto insertado: " + producto);
        }
    }

    // Método para actualizar un producto
    public void actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET descripcion = ?, precio_unitario = ?, stock = ?, imagen_referencial = ?, subcategoria_id = ? WHERE producto_id = ?";
        try (Connection conexion = Conexion.getConexion();
             java.sql.PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, producto.getDescripcion());
            stmt.setFloat(2, producto.getPrecio_Unitario());
            stmt.setInt(3, producto.getStock());
            stmt.setString(4, producto.getImagen_Referencial());
            stmt.setInt(5, producto.getSubcategoria_Id());
            stmt.setInt(6, producto.getProducto_Id());

            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Producto actualizado correctamente: " + producto);
            } else {
                System.out.println("No se encontró un producto con el ID especificado.");
            }
        }
    }

    // Método para listar productos con sus categorías y subcategorías
    public ListaEnlazada<Producto> listarProductosConCategoriaYSubcategoria() throws SQLException {
        String sql = """
        SELECT 
            c.descripcion AS categoria,
            s.descripcion AS subcategoria,
            con.descripcion AS concatenada_desc, -- Descripción desde concatenada
            p.producto_id,
            p.descripcion,
            p.precio_unitario,
            p.stock,
            p.imagen_referencial,
            s.subcategoria_id
        FROM producto p
        JOIN subcategoria s ON p.subcategoria_id = s.subcategoria_id
        JOIN categoria c ON s.categoria_id = c.categoria_id
        JOIN concatenada con ON p.producto_id = con.producto_id -- Join con concatenada
        """;

        ListaEnlazada<Producto> productos = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("producto_id"),                  // ID del producto
                        rs.getString("descripcion"),              // Descripción del producto
                        rs.getFloat("precio_unitario"),           // Precio unitario
                        rs.getInt("stock"),                       // Stock disponible
                        rs.getString("imagen_referencial"),       // Imagen referencial
                        rs.getInt("subcategoria_id")              // Subcategoría ID
                );
                producto.setCategoria(rs.getString("categoria"));   // Categoría
                producto.setSubcategoria(rs.getString("subcategoria")); // Subcategoría
                producto.setConcatenadaDesc(rs.getString("concatenada_desc")); // Nueva columna
                productos.insertar(producto);
            }
        }
        return productos;
    }

    public ListaEnlazada<String> listarDescripcionesConcatenada() throws SQLException {
        String sql = "SELECT descripcion FROM concatenada";
        ListaEnlazada<String> descripciones = new ListaEnlazada<>();

        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String descripcion = rs.getString("descripcion");
                descripciones.insertar(descripcion);
            }
        }

        System.out.println("Lista de descripciones de concatenada obtenida correctamente.");
        return descripciones;
    }

    public ListaEnlazada<Producto> obtenerDetallesProductos() throws SQLException {
        String sql = "BEGIN obtener_detalles_producto(:cursor_out); END;";
        ListaEnlazada<Producto> productos = new ListaEnlazada<>();

        try (Connection conexion = Conexion.getConexion();
             CallableStatement stmt = conexion.prepareCall(sql)) {

            // Configurar el parámetro de salida como SYS_REFCURSOR
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

            // Ejecutar el procedimiento almacenado
            stmt.execute();

            // Obtener el cursor
            ResultSet rs = (ResultSet) stmt.getObject(1);

            // Procesar los resultados
            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getString("descripcion_concatenada"), // Descripción concatenada
                        rs.getString("categoria"),              // Categoría
                        rs.getString("subcategoria"),           // Subcategoría
                        rs.getString("descripcion_producto"),   // Descripción del producto
                        rs.getFloat("precio_unitario_producto"),// Precio unitario
                        rs.getInt("stock_producto")             // Stock
                );
                productos.insertar(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return productos;
    }



    public Producto buscarProductoPorDescripcion(String descripcion, String categoria, String subcategoria) throws SQLException {
        // Construir la llamada al procedimiento con las variables embebidas
        String sql = """
        BEGIN 
            buscar_producto_por_descripcion('%s', '%s', '%s', :cursor_out); 
        END;
        """.formatted(descripcion, categoria, subcategoria);

        try (Connection conexion = Conexion.getConexion();
             CallableStatement stmt = conexion.prepareCall(sql)) {

            // Configurar el parámetro de salida como SYS_REFCURSOR
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

            // Ejecutar el procedimiento almacenado
            stmt.execute();

            // Obtener el cursor
            ResultSet rs = (ResultSet) stmt.getObject(1);

            // Procesar los resultados
            if (rs.next()) {
                return new Producto(
                        rs.getInt("producto_id"),                  // ID del producto
                        rs.getString("descripcion"),              // Descripción del producto
                        rs.getFloat("precio_unitario"),           // Precio unitario
                        rs.getInt("stock"),                       // Stock disponible
                        rs.getString("imagen_referencial"),       // Imagen referencial
                        rs.getInt("subcategoria_id")              // Subcategoría ID
                );
            }
        }
        return null; // Si no se encuentra el producto
    }


    public ListaEnlazada<String> obtenerSubcategoriasPorCategoria(String categoria) throws SQLException {
        String sql = """
        SELECT s.descripcion
        FROM subcategoria s
        JOIN categoria c ON s.categoria_id = c.categoria_id
        WHERE c.descripcion = '%s'
    """.formatted(categoria);

        ListaEnlazada<String> subcategorias = new ListaEnlazada<>();
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                subcategorias.insertar(rs.getString("descripcion"));
            }
        }

        System.out.println("Subcategorías obtenidas para la categoría: " + categoria);
        return subcategorias;
    }


    public ListaEnlazada<String> obtenerCategorias() throws SQLException {
        String sql = "SELECT descripcion FROM categoria";
        ListaEnlazada<String> categorias = new ListaEnlazada<>();

        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categorias.insertar(rs.getString("descripcion"));
            }

            System.out.println("Categorías obtenidas correctamente.");
        }
        return categorias;
    }


    public void eliminarProducto(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE producto_id = " + id;
        try (Connection conexion = Conexion.getConexion();
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Producto eliminado con ID: " + id);
        }
    }

    public Integer buscarProductoIdPorConcatenada(String descripcionConcatenada) throws SQLException {
        String sql = "{CALL buscar_producto_por_concatenada(?, ?)}";
        Integer productoId = null;

        try (Connection conexion = Conexion.getConexion();
             CallableStatement stmt = conexion.prepareCall(sql)) {

            // Configurar los parámetros del procedimiento
            stmt.setString(1, descripcionConcatenada); // Entrada
            stmt.registerOutParameter(2, java.sql.Types.INTEGER); // Salida

            // Ejecutar el procedimiento
            stmt.execute();

            // Obtener el resultado
            productoId = stmt.getInt(2);
            if (stmt.wasNull()) {
                productoId = null;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto por concatenada: " + e.getMessage());
            throw e;
        }

        return productoId;
    }

    public Producto buscarProductoPorId(Integer productoId) throws SQLException {
        String sql = "{? = CALL buscar_producto_por_id(?)}";
        Producto producto = null;

        try (Connection conexion = Conexion.getConexion();
             CallableStatement stmt = conexion.prepareCall(sql)) {

            // Configurar los parámetros de la función
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR); // Resultado como SYS_REFCURSOR
            stmt.setInt(2, productoId); // Parámetro de entrada

            // Ejecutar la función
            stmt.execute();

            // Obtener el cursor
            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                if (rs.next()) {
                    producto = new Producto(
                            rs.getInt("producto_id"),
                            rs.getString("descripcion"),
                            rs.getFloat("precio_unitario"),
                            rs.getInt("stock"),
                            rs.getString("imagen_referencial"),
                            rs.getInt("subcategoria_id")
                    );
                    producto.setSubcategoria(rs.getString("subcategoria"));
                    producto.setCategoria(rs.getString("categoria"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto por ID: " + e.getMessage());
            throw e;
        }

        return producto;
    }



}