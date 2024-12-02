package gui.ventanas.administrador.productos;

import db.config.Conexion;
import db.modelo.Producto;
import db.repositorio.ProductoRep;
import gui.ventanas.administrador.PrincipalAdmin;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tda.ListaEnlazada;
import tda.Nodo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GestionProductos extends Application {

    private final ProductoRep productoRep = new ProductoRep();
    private static Producto productoSeleccionado; // Variable para almacenar el producto seleccionado

    public static Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    @Override
    public void start(Stage primaryStage) {
        // Logo principal
        Image logoImage = new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/LogoMundoMascotasMundo%20Mascotas%20-%20Logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(250);
        logoView.setPreserveRatio(true);

        // Título
        Label titulo = new Label("GESTIÓN DE PRODUCTOS");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        HBox tituloLayout = new HBox(titulo);
        tituloLayout.setAlignment(Pos.CENTER);

        // Botón de volver
        Image volverImage = new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/Volver.png");
        ImageView volverView = new ImageView(volverImage);
        volverView.setFitWidth(50);
        volverView.setPreserveRatio(true);
        Button btnVolver = new Button("", volverView);
        btnVolver.setStyle("-fx-background-color: transparent;");
        btnVolver.setOnAction(e -> {
            try {
                new PrincipalAdmin().start(primaryStage); // Regresa a PrincipalAdmin
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Encabezado
        HBox headerLayout = new HBox(20, logoView, tituloLayout, btnVolver);
        HBox.setHgrow(tituloLayout, Priority.ALWAYS);
        headerLayout.setAlignment(Pos.CENTER_LEFT);
        headerLayout.setPadding(new Insets(20));
        headerLayout.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;");

        // Tabla de productos
        TableView<Producto> tablaProductos = new TableView<>();
        tablaProductos.setId("tablaProductos");
        tablaProductos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaProductos.setPrefHeight(400);

        // Columnas de la tabla
        TableColumn<Producto, String> colID = new TableColumn<>("ID");
        colID.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getConcatenadaDesc()));

        TableColumn<Producto, String> colCategoria = new TableColumn<>("CATEGORÍA");
        colCategoria.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getCategoria()));

        TableColumn<Producto, String> colSubcategoria = new TableColumn<>("SUBCATEGORÍA");
        colSubcategoria.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getSubcategoria()));

        TableColumn<Producto, String> colDescripcion = new TableColumn<>("DESCRIPCIÓN");
        colDescripcion.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDescripcion()));

        TableColumn<Producto, Float> colPrecio = new TableColumn<>("P. UNITARIO");
        colPrecio.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPrecio_Unitario()));

        TableColumn<Producto, Integer> colStock = new TableColumn<>("STOCK");
        colStock.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getStock()));

        tablaProductos.getColumns().addAll(colID, colCategoria, colSubcategoria, colDescripcion, colPrecio, colStock);

        // Detectar selección en la tabla
        tablaProductos.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null) {
                    if (newItem.getStock() == 0) {
                        row.setStyle("-fx-background-color: #FF6666;"); // Rojo para stock cero
                    } else if (newItem.getStock() > 0 && newItem.getStock() <= 10) {
                        row.setStyle("-fx-background-color: #FFFF99;"); // Amarillo para stock bajo
                    } else {
                        row.setStyle(""); // Sin color para stock normal
                    }
                } else {
                    row.setStyle(""); // Restablecer estilo
                }
            });

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Producto productoSeleccionado = row.getItem();
                    if (productoSeleccionado != null) {
                        try {
                            // Obtener la descripción concatenada seleccionada
                            String descripcionConcatenada = productoSeleccionado.getConcatenadaDesc();

                            // Buscar el producto_id correspondiente
                            Integer productoId = productoRep.buscarProductoIdPorConcatenada(descripcionConcatenada);

                            if (productoId != null) {
                                // Obtener los detalles del producto por producto_id
                                Producto producto = productoRep.buscarProductoPorId(productoId);

                                if (producto != null) {
                                    GestionProductos.productoSeleccionado = producto;
                                    new EdicionP().start(primaryStage); // Abrir la ventana de edición
                                } else {
                                    Alert alerta = new Alert(Alert.AlertType.WARNING, "No se encontró el producto.");
                                    alerta.showAndWait();
                                }
                            } else {
                                Alert alerta = new Alert(Alert.AlertType.WARNING, "No se encontró el producto para la descripción seleccionada.");
                                alerta.showAndWait();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Alert alerta = new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage());
                            alerta.showAndWait();
                        }
                    }
                }
            });



            return row;
        });


        // Botón de registrar nuevo producto
        Button btnRegistrar = new Button("REGISTRAR NUEVO PRODUCTO");
        btnRegistrar.setStyle("-fx-background-color: #d3d3d3; -fx-border-color: black; -fx-border-radius: 5; -fx-background-radius: 5;");
        btnRegistrar.setPrefWidth(250);
        btnRegistrar.setOnAction(e -> {
            try {
                new CreacionP().start(primaryStage); // Abrir CreacionP
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox botonLayout = new HBox(btnRegistrar);
        botonLayout.setAlignment(Pos.CENTER_RIGHT);
        botonLayout.setPadding(new Insets(10));

        // Contenedor para la tabla y el botón
        VBox tableContainer = new VBox(10, tablaProductos, botonLayout);
        tableContainer.setPadding(new Insets(20));
        tableContainer.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;");

        // Llenar la tabla desde la base de datos
        llenarTabla(tablaProductos);

        // Layout principal
        VBox layout = new VBox(20, headerLayout, tableContainer);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #999999;");

        // Escena
        Scene scene = new Scene(layout, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestión de Productos");
        primaryStage.show();
    }

    // Método para llenar la tabla desde la base de datos
    private void llenarTabla(TableView<Producto> tablaProductos) {
        tablaProductos.getItems().clear(); // Limpiar los datos actuales de la tabla
        try {
            ListaEnlazada<Producto> productos = productoRep.obtenerDetallesProductos();
            if (productos != null) {
                Nodo<Producto> actual = productos.getHead();
                while (actual != null) {
                    tablaProductos.getItems().add(actual.getData());
                    actual = actual.getNext();
                }
            }
        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Error al cargar productos: " + e.getMessage());
            alerta.showAndWait();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}