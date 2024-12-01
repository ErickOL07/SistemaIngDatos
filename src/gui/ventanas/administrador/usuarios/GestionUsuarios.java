package gui.ventanas.administrador.usuarios;

import db.modelo.Empleado;
import db.repositorio.EmpleadoRep;
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

import java.sql.SQLException;

public class GestionUsuarios extends Application {

    private final EmpleadoRep empleadoRep = new EmpleadoRep();
    private static Empleado empleadoSeleccionado; // Variable para almacenar el empleado seleccionado

    public static Empleado getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    @Override
    public void start(Stage primaryStage) {
        // Logo principal
        Image logoImage = new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/LogoMundoMascotasMundo%20Mascotas%20-%20Logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(250); // Tamaño aumentado
        logoView.setPreserveRatio(true);

        // Título
        Label titulo = new Label("GESTIÓN DE USUARIOS");
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

        // Tabla de usuarios
        TableView<Empleado> tablaUsuarios = new TableView<>();
        tablaUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Ajustar columnas automáticamente
        tablaUsuarios.setPrefHeight(400);

        // Columnas de la tabla
        TableColumn<Empleado, String> colID = new TableColumn<>("ID");
        colID.setCellValueFactory(data -> {
            String prefix = data.getValue().getRol_Id() == 1 ? "UA" : "UC";
            String idFormatted = String.format("%04d", data.getValue().getEmpleado_Id());
            return new javafx.beans.property.SimpleObjectProperty<>(prefix + idFormatted);
        });

        TableColumn<Empleado, String> colTipo = new TableColumn<>("TIPO");
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getRol_Id() == 1 ? "Administrador" : "Cajero"));

        TableColumn<Empleado, String> colNombre = new TableColumn<>("NOMBRE");
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getNombre()));

        TableColumn<Empleado, String> colCorreo = new TableColumn<>("CORREO");
        colCorreo.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getCorreo_Electronico()));

        tablaUsuarios.getColumns().addAll(colID, colTipo, colNombre, colCorreo);

        // Detectar selección en la tabla
        tablaUsuarios.setRowFactory(tv -> {
            TableRow<Empleado> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    empleadoSeleccionado = row.getItem();
                    try {
                        new EdicionU().start(primaryStage); // Abrir EdicionU con el empleado seleccionado
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            return row;
        });

        // Botón de registrar nuevo usuario
        Button btnRegistrar = new Button("REGISTRAR NUEVO USUARIO");
        btnRegistrar.setStyle("-fx-background-color: #d3d3d3; -fx-border-color: black; -fx-border-radius: 5; -fx-background-radius: 5;");
        btnRegistrar.setPrefWidth(250);
        btnRegistrar.setOnAction(e -> {
            try {
                new CreacionU().start(primaryStage); // Abrir CreacionU
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox botonLayout = new HBox(btnRegistrar);
        botonLayout.setAlignment(Pos.CENTER_RIGHT);
        botonLayout.setPadding(new Insets(10));

        // Contenedor para la tabla y el botón
        VBox tableContainer = new VBox(10, tablaUsuarios, botonLayout);
        tableContainer.setPadding(new Insets(20));
        tableContainer.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;");

        // Llenar la tabla desde la base de datos
        llenarTabla(tablaUsuarios);

        // Layout principal
        VBox layout = new VBox(20, headerLayout, tableContainer);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #999999;");

        // Escena
        Scene scene = new Scene(layout, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestión de Usuarios");
        primaryStage.show();
    }

    // Método para llenar la tabla desde la base de datos
    private void llenarTabla(TableView<Empleado> tablaUsuarios) {
        try {
            ListaEnlazada<Empleado> empleados = empleadoRep.listarEmpleados();
            if (empleados != null) {
                Nodo<Empleado> actual = empleados.getHead();
                while (actual != null) {
                    tablaUsuarios.getItems().add(actual.getData());
                    actual = actual.getNext();
                }
            }
        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Error al cargar empleados: " + e.getMessage());
            alerta.showAndWait();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
