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

import java.sql.SQLException;

public class EdicionU {
    private final EmpleadoRep empleadoRep = new EmpleadoRep();

    public void start(Stage parentStage) {
        // Obtener el empleado seleccionado
        Empleado empleado = GestionUsuarios.getEmpleadoSeleccionado();
        if (empleado == null) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "No se seleccionó ningún usuario.");
            alerta.showAndWait();
            return;
        }

        // Crear un nuevo Stage para la ventana modal
        Stage modalStage = new Stage();
        modalStage.initOwner(parentStage);
        modalStage.initModality(Modality.APPLICATION_MODAL); // Hace que sea modal
        modalStage.setTitle("Edición de Usuario");

        // Título
        Label titulo = new Label("Edición de usuario");
        titulo.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        HBox tituloLayout = new HBox(titulo);
        tituloLayout.setAlignment(Pos.CENTER);

        // Campos de edición
        Label lblNombre = new Label("Nombre");
        lblNombre.setStyle("-fx-font-weight: bold;");
        TextField txtNombre = new TextField(empleado.getNombre());

        Label lblDni = new Label("DNI");
        lblDni.setStyle("-fx-font-weight: bold;");
        TextField txtDni = new TextField(empleado.getNumero_Documento());

        Label lblCorreo = new Label("Correo");
        lblCorreo.setStyle("-fx-font-weight: bold;");
        TextField txtCorreo = new TextField(empleado.getCorreo_Electronico());

        Label lblContrasena = new Label("Contraseña");
        lblContrasena.setStyle("-fx-font-weight: bold;");
        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setText(empleado.getContrasena());

        Label lblTipo = new Label("Tipo");
        lblTipo.setStyle("-fx-font-weight: bold;");
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton rbAdministrador = new RadioButton("Administrador");
        rbAdministrador.setToggleGroup(toggleGroup);
        RadioButton rbCajero = new RadioButton("Cajero");
        rbCajero.setToggleGroup(toggleGroup);
        if (empleado.getRol_Id() == 1) {
            rbAdministrador.setSelected(true);
        } else {
            rbCajero.setSelected(true);
        }
        HBox tipoLayout = new HBox(10, rbAdministrador, rbCajero);
        tipoLayout.setAlignment(Pos.CENTER_LEFT);

        // Botones de acción
        Button btnEliminar = new Button("ELIMINAR USUARIO");
        btnEliminar.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-weight: bold;");
        btnEliminar.setOnAction(e -> {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas eliminar este usuario?", ButtonType.YES, ButtonType.NO);
            confirmacion.setTitle("Confirmación de eliminación");
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        empleadoRep.eliminarEmpleado(empleado.getEmpleado_Id());
                        Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Usuario eliminado correctamente.");
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
                        Alert alerta = new Alert(Alert.AlertType.ERROR, "Error al eliminar usuario: " + ex.getMessage());
                        alerta.showAndWait();
                        ex.printStackTrace();
                    }
                }
            });
        });

        Button btnGuardar = new Button("GUARDAR CAMBIOS");
        btnGuardar.setStyle("-fx-background-color: #D3D3D3; -fx-font-weight: bold;");
        btnGuardar.setOnAction(e -> {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas guardar los cambios?", ButtonType.YES, ButtonType.NO);
            confirmacion.setTitle("Confirmación de guardado");
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        empleado.setNombre(txtNombre.getText());
                        empleado.setCorreo_Electronico(txtCorreo.getText());
                        empleado.setContrasena(txtContrasena.getText());
                        empleado.setRol_Id(rbAdministrador.isSelected() ? 1 : 2);
                        empleado.setNumero_Documento(txtDni.getText());

                        empleadoRep.actualizarEmpleado(empleado);

                        Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Usuario actualizado correctamente.");
                        alerta.showAndWait();

                        // Actualizar la tabla en la interfaz principal
                        if (parentStage.getScene().getRoot() instanceof VBox layoutPrincipal) {
                            TableView<Empleado> tablaUsuarios = (TableView<Empleado>) layoutPrincipal.lookup("#tablaUsuarios");
                            if (tablaUsuarios != null) {
                                new GestionUsuarios().llenarTabla(tablaUsuarios);
                            }
                        }

                        modalStage.close(); // Cerrar la ventana modal
                    } catch (SQLException ex) {
                        if (ex.getMessage().contains("CHK_CORREO_ELECTRONICO")) {
                            Alert alerta = new Alert(Alert.AlertType.ERROR, "El correo electrónico no tiene un formato válido.");
                            alerta.showAndWait();
                        } else {
                            Alert alerta = new Alert(Alert.AlertType.ERROR, "Error al guardar cambios: " + ex.getMessage());
                            alerta.showAndWait();
                        }
                        ex.printStackTrace();
                    }
                }
            });
        });


        Button btnCancelar = new Button("CANCELAR");
        btnCancelar.setStyle("-fx-background-color: #D3D3D3; -fx-font-weight: bold;");
        btnCancelar.setOnAction(e -> {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas cancelar los cambios?", ButtonType.YES, ButtonType.NO);
            confirmacion.setTitle("Confirmación de cancelación");
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    modalStage.close(); // Cerrar la ventana modal
                }
            });
        });

        HBox botonesLayout = new HBox(20, btnEliminar, btnGuardar, btnCancelar);
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
        gridPane.add(lblTipo, 0, 4);
        gridPane.add(tipoLayout, 1, 4);

        VBox layout = new VBox(20, tituloLayout, gridPane, botonesLayout);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #D3D3D3;");

        Scene scene = new Scene(layout, 600, 400);
        modalStage.setScene(scene);
        modalStage.showAndWait(); // Mostrar la ventana modal y esperar
    }
}
