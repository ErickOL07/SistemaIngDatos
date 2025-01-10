package gui.ventanas.administrador.productos;

import db.modelo.Producto;
import db.repositorio.ProductoRep;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tda.ListaEnlazada;
import tda.Nodo;

import java.sql.SQLException;
import java.util.List;

public class EdicionP {
    private final ProductoRep productoRep = new ProductoRep();

    public void start(Stage parentStage, TableView<Producto> tablaProductos) {
        Producto producto = GestionProductos.getProductoSeleccionado();
        if (producto == null) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "No se seleccionó ningún producto.");
            alerta.showAndWait();
            return;
        }


        Stage modalStage = new Stage();
        modalStage.initOwner(parentStage);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Edición de producto");

        Label titulo = new Label("Edición de producto");
        titulo.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        HBox tituloLayout = new HBox(titulo);
        tituloLayout.setAlignment(Pos.CENTER);

        Label lblCategoria = new Label("Categoría");
        ComboBox<String> cbCategoria = new ComboBox<>();
        cbCategoria.getItems().addAll("Perros", "Gatos"); // Cargar categorías
        cbCategoria.setValue(producto.getCategoria());

        Label lblSubcategoria = new Label("Subcategoría");
        ComboBox<String> cbSubcategoria = new ComboBox<>();
        cargarSubcategorias(cbCategoria.getValue(), cbSubcategoria); // Cargar subcategorías iniciales
        cbSubcategoria.setValue(producto.getSubcategoria());
        cbCategoria.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cbSubcategoria.getItems().clear();
                cbSubcategoria.setValue(null);
                cargarSubcategorias(newValue, cbSubcategoria);
                cbSubcategoria.setValue(null);
            }
        });

        cbCategoria.setOnAction(e -> cargarSubcategorias(cbCategoria.getValue(), cbSubcategoria));


        Label lblDescripcion = new Label("Descripción");
        TextField txtDescripcion = new TextField(producto.getDescripcion());
        txtDescripcion.setPrefWidth(400);

        Label lblPrecio = new Label("Precio unitario");
        TextField txtPrecio = new TextField(String.valueOf(producto.getPrecio_Unitario()));
        txtPrecio.setPrefWidth(100);

        Label lblStock = new Label("Stock");
        TextField txtStock = new TextField(String.valueOf(producto.getStock()));
        txtStock.setPrefWidth(100);

        Label lblImagen = new Label("Imagen referencial");
        Button btnImportar = new Button("IMPORTAR");
        ImageView imgPreview = new ImageView();
        imgPreview.setFitWidth(100);
        imgPreview.setFitHeight(100);
        imgPreview.setPreserveRatio(false);
        imgPreview.setImage(new Image(producto.getImagen_Referencial(), true));

        btnImportar.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Importar imagen");
            dialog.setHeaderText("Ingrese el enlace de la imagen:");
            dialog.setContentText("URL:");

            dialog.showAndWait().ifPresent(url -> {
                try {
                    Image img = new Image(url, true);
                    imgPreview.setImage(img);
                    producto.setImagen_Referencial(url);
                } catch (Exception ex) {
                    Alert alerta = new Alert(Alert.AlertType.ERROR, "No se pudo cargar la imagen.");
                    alerta.showAndWait();
                }
            });
        });

        Button btnEliminar = new Button("ELIMINAR PRODUCTO");
        btnEliminar.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-weight: bold;");
        btnEliminar.setOnAction(e -> {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas eliminar este producto?", ButtonType.YES, ButtonType.NO);
            confirmacion.setTitle("Confirmación de eliminación");
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        productoRep.eliminarProducto(producto.getProducto_Id());
                        Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Producto eliminado correctamente.");
                        alerta.showAndWait();

                        // Actualizar la tabla en la interfaz principal
                        new GestionProductos().start(parentStage);
                        modalStage.close();
                    } catch (Exception ex) {
                        Alert alerta = new Alert(Alert.AlertType.ERROR, "Error al eliminar producto: " + ex.getMessage());
                        alerta.showAndWait();
                    }
                }
            });
        });

        Button btnGuardar = new Button("GUARDAR CAMBIOS");
        btnGuardar.setStyle("-fx-background-color: #D3D3D3; -fx-font-weight: bold;");
        btnGuardar.setOnAction(e -> {
            try {
                String nuevaCategoria = cbCategoria.getValue();
                String nuevaSubcategoria = cbSubcategoria.getValue();

                if (nuevaCategoria == null || nuevaSubcategoria == null) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING, "Debe seleccionar una categoría y subcategoría.");
                    alerta.showAndWait();
                    return;
                }

                ListaEnlazada<String> subcategoriasValidas = productoRep.obtenerSubcategoriasPorCategoria(nuevaCategoria);
                boolean subcategoriaValida = false;
                Nodo<String> nodo = subcategoriasValidas.getHead();
                while (nodo != null) {
                    if (nodo.getData().equals(nuevaSubcategoria)) {
                        subcategoriaValida = true;
                        break;
                    }
                    nodo = nodo.getNext();
                }

                if (!subcategoriaValida) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING, "La subcategoría seleccionada no corresponde a la categoría.");
                    alerta.showAndWait();
                    return;
                }

                producto.setCategoria(nuevaCategoria);
                producto.setSubcategoria(nuevaSubcategoria);
                producto.setDescripcion(txtDescripcion.getText());
                producto.setPrecio_Unitario(Float.parseFloat(txtPrecio.getText()));
                producto.setStock(Integer.parseInt(txtStock.getText()));
                producto.setImagen_Referencial(imgPreview.getImage().getUrl());

                productoRep.actualizarProducto(producto);

                Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Producto actualizado correctamente.");
                alerta.showAndWait();

                GestionProductos gestionProductos = new GestionProductos();
                gestionProductos.llenarTabla(tablaProductos);

                modalStage.close();
            } catch (SQLException ex) {
                Alert alerta = new Alert(Alert.AlertType.ERROR, "Error al guardar cambios: " + ex.getMessage());
                alerta.showAndWait();
            } catch (NumberFormatException ex) {
                Alert alerta = new Alert(Alert.AlertType.ERROR, "Error en los datos ingresados. Verifique los campos numéricos.");
                alerta.showAndWait();
            }
        });

        Button btnCancelar = new Button("CANCELAR");
        btnCancelar.setStyle("-fx-background-color: #D3D3D3; -fx-font-weight: bold;");
        btnCancelar.setOnAction(e -> modalStage.close());

        HBox botonesLayout = new HBox(20, btnEliminar, btnGuardar, btnCancelar);
        botonesLayout.setAlignment(Pos.CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(lblCategoria, 0, 0);
        gridPane.add(cbCategoria, 1, 0);
        gridPane.add(lblSubcategoria, 0, 1);
        gridPane.add(cbSubcategoria, 1, 1);
        gridPane.add(lblDescripcion, 0, 2);
        gridPane.add(txtDescripcion, 1, 2);
        gridPane.add(lblPrecio, 0, 3);
        gridPane.add(txtPrecio, 1, 3);
        gridPane.add(lblStock, 0, 4);
        gridPane.add(txtStock, 1, 4);
        gridPane.add(lblImagen, 0, 5);
        gridPane.add(btnImportar, 1, 5);
        gridPane.add(imgPreview, 1, 6);

        VBox layout = new VBox(20, tituloLayout, gridPane, botonesLayout);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #D3D3D3;");

        Scene scene = new Scene(layout, 600, 600);
        modalStage.setScene(scene);
        modalStage.showAndWait();

    }

    private void cargarSubcategorias(String categoria, ComboBox<String> cbSubcategoria) {
        cbSubcategoria.getItems().clear();
        try {
            System.out.println("Cargando subcategorías para la categoría: " + categoria);
            ListaEnlazada<String> subcategorias = productoRep.obtenerSubcategoriasPorCategoria(categoria);
            Nodo<String> nodoActual = subcategorias.getHead();

            while (nodoActual != null) {
                System.out.println("Subcategoría cargada: " + nodoActual.getData());
                cbSubcategoria.getItems().add(nodoActual.getData());
                nodoActual = nodoActual.getNext();
            }
        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Error al cargar subcategorías: " + e.getMessage());
            alerta.showAndWait();
        }
    }




}