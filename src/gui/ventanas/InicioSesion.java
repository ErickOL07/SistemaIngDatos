package gui.ventanas;

import db.config.Conexion;
import db.modelo.Empleado;
import db.repositorio.EmpleadoRep;
import gui.ventanas.administrador.PrincipalAdmin;
import gui.ventanas.cajero.PrincipalCajero;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class InicioSesion extends Application {

    private final EmpleadoRep empleadoRep = new EmpleadoRep();

    @Override
    public void start(Stage primaryStage) {
        // Imagen del logo
        Image logo = new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/LogoMundoMascotasMundo%20Mascotas%20-%20Logo.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(180);
        logoView.setPreserveRatio(true);

        // Título
        Text title = new Text("INICIAR SESIÓN");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setFill(Color.BLACK);

        // Campo de usuario
        TextField userField = new TextField();
        userField.setPromptText("Usuario");
        userField.setMaxWidth(200);

        // Campo de contraseña
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        passwordField.setMaxWidth(200);

        // Botón de ingreso
        Button loginButton = new Button("INGRESAR");
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        loginButton.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: #FFA500;");
        loginButton.setMaxWidth(200);

        // Acción del botón de ingreso
        loginButton.setOnAction(e -> {
            String usuario = userField.getText();
            String contrasena = passwordField.getText();
            if (usuario.isEmpty() || contrasena.isEmpty()) {
                mostrarAlerta("Error", "Por favor, completa todos los campos.", Alert.AlertType.ERROR);
                return;
            }
            Empleado empleado = validarCredenciales(usuario, contrasena);
            if (empleado != null) {
                mostrarAlerta("Bienvenido", "Inicio de sesión exitoso.", AlertType.INFORMATION);
                redirigirSegunRol(primaryStage, empleado);
            } else {
                mostrarAlerta("Error", "Credenciales incorrectas. Inténtalo nuevamente.", AlertType.ERROR);
            }
        });



        // Contenedor del formulario
        VBox formBox = new VBox(15, userField, passwordField, loginButton);
        formBox.setAlignment(Pos.CENTER);

        // Contenedor central cuadrado con bordes redondeados
        VBox centerBox = new VBox(20, logoView, title, formBox);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));
        centerBox.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: black;");
        centerBox.setMaxWidth(300);
        centerBox.setMaxHeight(300);

        // Contenedor raíz
        StackPane root = new StackPane(centerBox);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #999999;");

        // Escena
        Scene scene = new Scene(root, 1024, 768);

        primaryStage.setTitle("Mundo Mascotas - Inicio de Sesión");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para validar credenciales de inicio de sesión
    private Empleado validarCredenciales(String correo, String contrasena) {
        try {
            Empleado empleado = empleadoRep.buscarEmpleadoPorCorreo(correo);
            if (empleado != null && empleado.getContrasena().equals(contrasena)) {
                return empleado;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Método para mostrar un error
    private void mostrarError(String mensaje) {
        System.out.println("Error: " + mensaje);
    }

    private void redirigirSegunRol(Stage primaryStage, Empleado empleado) {
        try {
            if (empleado.getRol_Id() == 1) { // Administrador
                PrincipalAdmin adminWindow = new PrincipalAdmin();
                adminWindow.start(primaryStage);
            } else if (empleado.getRol_Id() == 2) { // Cajero
                PrincipalCajero cajeroWindow = new PrincipalCajero();
                cajeroWindow.start(primaryStage);
            } else {
                mostrarAlerta("Error", "Rol desconocido. Contacta al administrador.", AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana correspondiente.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Connection conexion = Conexion.conectar();
            System.out.println("Conexión realizada correctamente.");

            // Mostrar todos los empleados en la consola
            listarEmpleados();

        } catch (Exception e) {
            System.err.println("Error en la conexión: " + e.getMessage());
        }
        launch(args);
    }

    // Método para listar empleados y mostrarlos en consola
    private static void listarEmpleados() {
        String sql = "SELECT * FROM empleado";
        try (Connection conexion = Conexion.conectar();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Empleados en la tabla:");
            System.out.println("-------------------------------------------------");
            System.out.printf("%-10s %-30s %-30s %-20s %-10s %-15s\n",
                    "ID", "Nombre", "Correo Electrónico", "Contraseña", "Rol ID", "Documento");
            System.out.println("-------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-10d %-30s %-30s %-20s %-10d %-15s\n",
                        rs.getInt("empleado_id"),
                        rs.getString("nombre"),
                        rs.getString("correo_electronico"),
                        rs.getString("contrasena"),
                        rs.getInt("rol_id"),
                        rs.getString("numero_documento"));
            }
            System.out.println("-------------------------------------------------");

            String sql1 = "SELECT * FROM empleado WHERE correo_electronico = '" + "20221751@aloe.ulima.edu.pe" + "'";
            try (Connection conexion1 = Conexion.getConexion();
                 Statement stmt1 = conexion1.createStatement();
                 ResultSet rs1 = stmt1.executeQuery(sql1)) {

                if (rs1.next()) {
                    System.out.println(rs1.getInt("empleado_id") +
                            rs1.getString("nombre") +
                            rs1.getString("correo_electronico")+
                            rs1.getString("contrasena")+
                            rs1.getInt("rol_id")+
                            rs1.getString("numero_documento")

                    );
                }
            }
            EmpleadoRep empleadoRep = new EmpleadoRep();

            System.out.println(empleadoRep.buscarEmpleadoPorCorreo("20221751@aloe.ulima.edu.pe"));

        } catch (Exception e) {
            System.err.println("Error al listar empleados: " + e.getMessage());
        }
    }


    /*
    public static void main(String[] args) {
        try {
            Connection conexion = Conexion.conectar();
            System.out.println("Conexión realizada correctamente.");
        } catch (Exception e) {
            System.err.println("Error en la conexión: " + e.getMessage());
        }
        launch(args);
    }
*/
}



