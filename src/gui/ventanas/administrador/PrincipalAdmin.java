package gui.ventanas.administrador;

import gui.ventanas.InicioSesion;
import gui.ventanas.cajero.PrincipalCajero;
import gui.ventanas.administrador.usuarios.GestionUsuarios;
import gui.ventanas.administrador.ventas.GestionVentas;
import gui.ventanas.administrador.productos.GestionProductos;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PrincipalAdmin extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Logo principal
        Image logoImage = new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/LogoMundoMascotasMundo%20Mascotas%20-%20Logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(300);
        logoView.setPreserveRatio(true);

        // Imagen de cerrar sesión
        Image cerrarSesionImage = new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/CerrarSesion.png");
        ImageView cerrarSesionView = new ImageView(cerrarSesionImage);
        cerrarSesionView.setFitWidth(50);
        cerrarSesionView.setPreserveRatio(true);
        Button btnCerrarSesion = new Button("", cerrarSesionView);
        btnCerrarSesion.setStyle("-fx-background-color: transparent;");
        btnCerrarSesion.setOnAction(e -> {
            InicioSesion inicioSesion = new InicioSesion();
            try {
                inicioSesion.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Imágenes dentro de los botones
        ImageView gestionarUsuariosImg = new ImageView(new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/GestionarUsuarios.png"));
        gestionarUsuariosImg.setFitWidth(100);
        gestionarUsuariosImg.setPreserveRatio(true);

        ImageView gestionarVentasImg = new ImageView(new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/GestionarVentas.png"));
        gestionarVentasImg.setFitWidth(100);
        gestionarVentasImg.setPreserveRatio(true);

        ImageView gestionarProductosImg = new ImageView(new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/GestionarProductos.png"));
        gestionarProductosImg.setFitWidth(100);
        gestionarProductosImg.setPreserveRatio(true);

        ImageView abrirMenuCajaImg = new ImageView(new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/AbrirMenuDeCaja.png"));
        abrirMenuCajaImg.setFitWidth(250);
        abrirMenuCajaImg.setPreserveRatio(true);

        // Botones funcionales con imágenes
        Button btnGestionarUsuarios = new Button("", gestionarUsuariosImg);
        btnGestionarUsuarios.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        btnGestionarUsuarios.setPrefSize(150, 150);
        btnGestionarUsuarios.setOnAction(e -> {
            GestionUsuarios gestionUsuarios = new GestionUsuarios();
            try {
                gestionUsuarios.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button btnGestionarVentas = new Button("", gestionarVentasImg);
        btnGestionarVentas.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        btnGestionarVentas.setPrefSize(150, 150);
        btnGestionarVentas.setOnAction(e -> {
            GestionVentas gestionVentas = new GestionVentas();
            try {
                gestionVentas.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button btnGestionarProductos = new Button("", gestionarProductosImg);
        btnGestionarProductos.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        btnGestionarProductos.setPrefSize(150, 150);
        btnGestionarProductos.setOnAction(e -> {
            GestionProductos gestionProductos = new GestionProductos();
            try {
                gestionProductos.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button btnAbrirMenuCaja = new Button("", abrirMenuCajaImg);
        btnAbrirMenuCaja.setStyle("-fx-background-color: transparent;");
        btnAbrirMenuCaja.setOnAction(e -> {
            PrincipalCajero principalCajero = new PrincipalCajero();
            try {
                principalCajero.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // HBox para los botones principales
        HBox mainButtons = new HBox(50, btnGestionarUsuarios, btnGestionarVentas, btnGestionarProductos);
        mainButtons.setAlignment(Pos.CENTER);

        // VBox para el botón de abrir menú de caja
        VBox menuCajaBox = new VBox(btnAbrirMenuCaja);
        menuCajaBox.setAlignment(Pos.BOTTOM_LEFT);
        menuCajaBox.setPadding(new Insets(20));

        // VBox para el logo y botones
        VBox layout = new VBox(20, logoView, mainButtons);
        layout.setAlignment(Pos.CENTER);

        // BorderPane para organizar toda la interfaz
        BorderPane root = new BorderPane();
        root.setCenter(layout);
        root.setTop(btnCerrarSesion);
        root.setBottom(menuCajaBox);

        BorderPane.setAlignment(btnCerrarSesion, Pos.TOP_RIGHT);
        BorderPane.setMargin(btnCerrarSesion, new Insets(10));

        // Estilo del fondo
        root.setStyle("-fx-background-color: #999999;");

        // Crear escena
        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mundo Mascotas - Menú Principal del Administrador");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
