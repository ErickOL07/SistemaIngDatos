package gui.ventanas.administrador.usuarios;

import db.modelo.Empleado;
import db.repositorio.EmpleadoRep;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreacionU {
    private final EmpleadoRep empleadoRep = new EmpleadoRep();

    public void start(Stage parentStage) {
        // Crear un nuevo Stage para la ventana modal
        Stage modalStage = new Stage();
        modalStage.initOwner(parentStage);
        modalStage.initModality(Modality.APPLICATION_MODAL); // Hace que sea modal
        modalStage.setTitle("Creación de Usuario");

        // Título
        Label titulo = new Label("Creación de usuario");
        titulo.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        HBox tituloLayout = new HBox(titulo);
        tituloLayout.setAlignment(Pos.CENTER);

        // Campos de entrada
        Label lblNombre = new Label("Nombre");
        lblNombre.setStyle("-fx-font-weight: bold;");
        TextField txtNombre = new TextField();

        Label lblDni = new Label("DNI");
        lblDni.setStyle("-fx-font-weight: bold;");
        TextField txtDni = new TextField();

        Label lblCorreo = new Label("Correo");
        lblCorreo.setStyle("-fx-font-weight: bold;");
        TextField txtCorreo = new TextField();

        Label lblContrasena = new Label("Contraseña");
        lblContrasena.setStyle("-fx-font-weight: bold;");
        PasswordField txtContrasena = new PasswordField();

        Label lblRepetirContrasena = new Label("Repetir contraseña");
        lblRepetirContrasena.setStyle("-fx-font-weight: bold;");
        PasswordField txtRepetirContrasena = new PasswordField();

        Label lblTipo = new Label("Tipo");
        lblTipo.setStyle("-fx-font-weight: bold;");
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton rbAdministrador = new RadioButton("Administrador");
        rbAdministrador.setToggleGroup(toggleGroup);
        RadioButton rbCajero = new RadioButton("Cajero");
        rbCajero.setToggleGroup(toggleGroup);
        rbAdministrador.setSelected(true); // Por defecto, administrador seleccionado
        HBox tipoLayout = new HBox(10, rbAdministrador, rbCajero);
        tipoLayout.setAlignment(Pos.CENTER_LEFT);

        // Botones de acción
        Button btnRegistrar = new Button("REGISTRAR");
        btnRegistrar.setStyle("-fx-background-color: #D3D3D3; -fx-font-weight: bold;");
        btnRegistrar.setOnAction(e -> {
            if (!txtContrasena.getText().equals(txtRepetirContrasena.getText())) {
                Alert alerta = new Alert(Alert.AlertType.ERROR, "Las contraseñas no coinciden.");
                alerta.showAndWait();
                return;
            }
            try {
                Empleado nuevoEmpleado = new Empleado(
                        0, // ID generado automáticamente por la base de datos
                        txtNombre.getText(),
                        txtCorreo.getText(),
                        txtContrasena.getText(),
                        rbAdministrador.isSelected() ? 1 : 2,
                        txtDni.getText()
                );
                empleadoRep.insertarEmpleado(nuevoEmpleado);

                Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Usuario creado correctamente.");
                alerta.showAndWait();

                // Actualizar la tabla en la interfaz principal
                if (parentStage.getScene().getRoot() instanceof VBox layoutPrincipal) {
                    TableView<Empleado> tablaUsuarios = (TableView<Empleado>) layoutPrincipal.lookup("#tablaUsuarios");
                    if (tablaUsuarios != null) {
                        new GestionUsuarios().llenarTabla(tablaUsuarios);
                    }
                }

                modalStage.close(); // Cerrar la ventana modal
            } catch (Exception ex) {
                Alert alerta = new Alert(Alert.AlertType.ERROR, "Error al crear usuario: " + ex.getMessage());
                alerta.showAndWait();
                ex.printStackTrace();
            }
        });


        Button btnCancelar = new Button("CANCELAR");
        btnCancelar.setStyle("-fx-background-color: #D3D3D3; -fx-font-weight: bold;");
        btnCancelar.setOnAction(e -> {
            modalStage.close(); // Cerrar la ventana modal
        });

        HBox botonesLayout = new HBox(20, btnRegistrar, btnCancelar);
        botonesLayout.setAlignment(Pos.CENTER);

        // Layout principal
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(lblNombre, 0, 0);
        gridPane.add(txtNombre, 1, 0);
        gridPane.add(lblDni, 0, 1);
        gridPane.add(txtDni, 1, 1);
        gridPane.add(lblCorreo, 0, 2);
        gridPane.add(txtCorreo, 1, 2);
        gridPane.add(lblContrasena, 0, 3);
        gridPane.add(txtContrasena, 1, 3);
        gridPane.add(lblRepetirContrasena, 0, 4);
        gridPane.add(txtRepetirContrasena, 1, 4);
        gridPane.add(lblTipo, 0, 5);
        gridPane.add(tipoLayout, 1, 5);

        VBox layout = new VBox(20, tituloLayout, gridPane, botonesLayout);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #D3D3D3;");

        Scene scene = new Scene(layout, 600, 400);
        modalStage.setScene(scene);
        modalStage.showAndWait(); // Mostrar la ventana modal y esperar
    }
}
