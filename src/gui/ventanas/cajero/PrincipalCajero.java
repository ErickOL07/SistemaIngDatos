package gui.ventanas.cajero;

import db.modelo.*;
import db.repositorio.*;
import gui.ventanas.InicioSesion;
import gui.ventanas.administrador.PrincipalAdmin;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import tda.ListaEnlazada;
import tda.Nodo;


public class PrincipalCajero extends Application {

    private final ListaEnlazada<VentaProducto> ventas = new ListaEnlazada<>();
    private Label totalLabel;
    private Label puntosGanadosLabel;
    private Label valorPuntosLabel;
    private Label puntosTotalesLabel;
    private TableView<VentaProducto> ventasTable;
    private Venta ventaActual = null;
    private final DetalleVentaRep detalleVentaRep = new DetalleVentaRep();
    private final VentaRep ventaRep = new VentaRep();
    private final ProductoRep productoRep = new ProductoRep();
    private int empleadoId;
    private TextField dniField;
    private TextField nombreField;
    private Image defaultImage;
    private ImageView productImageView;
    private ComboBox<String> formaPagoCombo;
    private ComboBox<String> tipoPagoCombo;


    @Override
    public void start(Stage primaryStage) {
        empleadoId = InicioSesion.empleadoAutenticado.getEmpleado_Id();

        totalLabel = new Label("TOTAL S/ 0.00");
        totalLabel.setStyle("-fx-font-size: 30px; -fx-background-color: #FFA07A;");

        ventasTable = new TableView<>();
        ventasTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);



        puntosGanadosLabel = new Label("PUNTOS: 0");
        puntosGanadosLabel.setStyle("-fx-font-size: 30px; -fx-background-color: #90EE90;");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Label fechaGrande = new Label(LocalDate.now().format(formatter));
        fechaGrande.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Image volverImage = new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/Volver.png");
        ImageView volverView = new ImageView(volverImage);
        volverView.setFitWidth(50);
        volverView.setPreserveRatio(true);
        Button btnVolver = new Button("", volverView);
        btnVolver.setStyle("-fx-background-color: transparent;");
        btnVolver.setOnAction(e -> {
            try {
                new PrincipalAdmin().start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topRight = new HBox(spacer, fechaGrande, btnVolver);
        topRight.setAlignment(Pos.CENTER);
        topRight.setPadding(new Insets(10));
        topRight.setSpacing(10);

        Image logoImage = new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/LogoMundoMascotasMundo%20Mascotas%20-%20Logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(250);
        logoView.setPreserveRatio(true);

        HBox header = new HBox(logoView, topRight);
        header.setAlignment(Pos.CENTER);
        header.setSpacing(20);

        header.setSpacing(20);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-border-color: black; -fx-border-width: 2;");

        TextField codigoField = new TextField();
        codigoField.setPromptText("CÓDIGO");
        codigoField.setPrefWidth(200);
        Button registrarButton = new Button("REGISTRAR");
        registrarButton.setStyle("-fx-background-color: #CCCCCC; -fx-font-weight: bold;");

        HBox codigoPanel = new HBox(10, codigoField, registrarButton);
        codigoPanel.setAlignment(Pos.CENTER_LEFT);
        codigoPanel.setPadding(new Insets(10));

        dniField = new TextField();
        dniField.setPromptText("DNI/RUC");
        dniField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                dniField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 11) {
                dniField.setText(newValue.substring(0, 11));
            }
        });

        Button buscarButton = new Button("BUSCAR");
        Button registrarClienteButton = new Button("REGISTRAR");
        nombreField = new TextField();
        nombreField.setPromptText("NOMBRE");
        buscarButton.setOnAction(e -> {
            String dniRuc = dniField.getText().trim();

            if (dniRuc.length() != 8 && dniRuc.length() != 11) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "El DNI debe tener 8 dígitos o el RUC 11 dígitos.");
                alert.showAndWait();
                return;
            }

            try {
                ClienteRep clienteRep = new ClienteRep();
                ListaEnlazada<Cliente> clientes = clienteRep.listarClientes();
                Nodo<Cliente> current = clientes.getHead();
                Cliente clienteEncontrado = null;

                while (current != null) {
                    Cliente cliente = current.getData();
                    if (cliente.getNumero_Documento().equals(dniRuc)) {
                        clienteEncontrado = cliente;
                        break;
                    }
                    current = current.getNext();
                }

                if (clienteEncontrado != null) {
                    nombreField.setText(clienteEncontrado.getNombre_Razon_Social());
                    float puntosTotales = clienteEncontrado.getPuntos();
                    puntosTotalesLabel.setText(String.format("PUNTOS TOTALES: %.0f", puntosTotales));
                    valorPuntosLabel.setText(String.format("VALOR S/ %.2f", puntosTotales / 1000));

                    ventaActual = new Venta(0, 0, 0, LocalDate.now().toString(), clienteEncontrado.getCliente_Id(), 1, 1, 1);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente no encontrado. Puede registrarlo.");
                    alert.showAndWait();
                    puntosTotalesLabel.setText("PUNTOS TOTALES: 0");
                    valorPuntosLabel.setText("VALOR S/ 0");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al buscar el cliente: " + ex.getMessage());
                alert.showAndWait();
            }
        });





        registrarClienteButton.setOnAction(e -> {
            String dniRuc = dniField.getText().trim();
            String nombreRazonSocial = nombreField.getText().trim();

            if (dniRuc.isEmpty() || nombreRazonSocial.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Debe ingresar el DNI/RUC y el Nombre/Razón Social.");
                alert.showAndWait();
                return;
            }

            if (dniRuc.length() != 8 && dniRuc.length() != 11) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "El DNI debe tener 8 dígitos o el RUC 11 dígitos.");
                alert.showAndWait();
                return;
            }

            try {
                ClienteRep clienteRep = new ClienteRep();
                Cliente nuevoCliente = new Cliente(
                        0,
                        nombreRazonSocial,
                        0,
                        dniRuc.length() == 8 ? 1 : 2,
                        dniRuc
                );

                int clienteId = clienteRep.insertarCliente(nuevoCliente);

                dniField.clear();
                nombreField.clear();
                puntosTotalesLabel.setText("PUNTOS TOTALES: 0");
                valorPuntosLabel.setText("VALOR S/ 0");

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente registrado con éxito. ID: " + clienteId);
                alert.showAndWait();

                ventaActual = null;

            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al registrar el cliente: " + ex.getMessage());
                alert.showAndWait();
            }
        });


        VBox clientePanel = new VBox(10, dniField, new HBox(10, buscarButton, registrarClienteButton), nombreField);
        clientePanel.setPadding(new Insets(10));
        clientePanel.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: #f0f0f0;");

        defaultImage = new Image("https://cdn.discordapp.com/attachments/886128843029643324/1312638652412727347/blank.png?ex=674d3982&is=674be802&hm=d3c192d17dcb0d3b6a099b688557df741ff3bd30191823dda6271fef70743198&");
        productImageView = new ImageView(defaultImage);
        productImageView.setFitWidth(200);
        productImageView.setPreserveRatio(true);

        VBox imagePanel = new VBox(productImageView);
        imagePanel.setAlignment(Pos.CENTER);
        imagePanel.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: #f0f0f0;");
        imagePanel.setPadding(new Insets(10));

        formaPagoCombo = new ComboBox<>();
        tipoPagoCombo = new ComboBox<>();

        try {
            MetodoPagoRep metodoPagoRep = new MetodoPagoRep();
            ListaEnlazada<Metodo_Pago> metodosPago = metodoPagoRep.listarMetodosPago();

            Nodo<Metodo_Pago> nodoActual = metodosPago.getHead();
            while (nodoActual != null) {
                Metodo_Pago metodo = nodoActual.getData();
                formaPagoCombo.getItems().add(metodo.getDescripcion());
                nodoActual = nodoActual.getNext();
            }

            formaPagoCombo.getSelectionModel().select("EFECTIVO"); // Selección predeterminada
            tipoPagoCombo.getItems().addAll("CONTADO", "CUOTAS");
            tipoPagoCombo.getSelectionModel().select("CONTADO"); // Selección predeterminada
        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar métodos de pago: " + ex.getMessage());
            alert.showAndWait();
        }


        Button eliminarProductoButton = new Button("ELIMINAR PRODUCTO");
        eliminarProductoButton.setStyle("-fx-background-color: #FF6666; -fx-text-fill: white;");

        VBox opcionesVentaPanel = new VBox(10, eliminarProductoButton, formaPagoCombo, tipoPagoCombo);
        opcionesVentaPanel.setPadding(new Insets(10));
        opcionesVentaPanel.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: #f0f0f0;");

        eliminarProductoButton.setOnAction(e -> {
            VentaProducto ventaSeleccionada = ventasTable.getSelectionModel().getSelectedItem();

            if (ventaSeleccionada == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Seleccione un producto para eliminar.");
                alert.showAndWait();
                return;
            }

            Nodo<VentaProducto> nodoActual = ventas.getHead();
            while (nodoActual != null) {
                VentaProducto venta = nodoActual.getData();
                if (venta.getCodigo().equals(ventaSeleccionada.getCodigo())) {
                    if (venta.getCantidad() > 1) {
                        venta.setCantidad(venta.getCantidad() - 1);
                    } else {
                        ventas.eliminar(venta);
                    }
                    break;
                }
                nodoActual = nodoActual.getNext();
            }

            actualizarTablaVentas(ventasTable);
            actualizarVenta();

            actualizarTotales();
        });




        puntosTotalesLabel = new Label("PUNTOS TOTALES: 0");
        puntosTotalesLabel.setStyle("-fx-background-color: #90EE90;");
        puntosTotalesLabel.setAlignment(Pos.CENTER_LEFT);

        valorPuntosLabel = new Label("VALOR S/ 0");
        valorPuntosLabel.setStyle("-fx-background-color: #FFA07A;");
        valorPuntosLabel.setAlignment(Pos.CENTER_LEFT);

        Button aplicarDescuentoButton = new Button("APLICAR DESCUENTO");

        VBox descuentosPanel = new VBox(10, puntosTotalesLabel, valorPuntosLabel, aplicarDescuentoButton);
        descuentosPanel.setPadding(new Insets(10));
        descuentosPanel.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: #f0f0f0;");

        aplicarDescuentoButton.setOnAction(e -> {
            if (ventaActual == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Debe buscar o registrar un cliente primero.");
                alert.showAndWait();
                return;
            }

            try {
                ClienteRep clienteRep = new ClienteRep();
                Cliente cliente = clienteRep.buscarClientePorId(ventaActual.getCliente_Id());
                if (cliente == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Cliente no encontrado.");
                    alert.showAndWait();
                    return;
                }

                float puntosTotales = cliente.getPuntos();
                float valorDescuento = puntosTotales / 1000;

                if (valorDescuento > ventaActual.getMonto_Total()) {
                    valorDescuento = ventaActual.getMonto_Total();
                }

                ventaActual.setDescuento(valorDescuento);
                ventaActual.setMonto_Total(ventaActual.getMonto_Total() - valorDescuento);

                clienteRep.actualizarPuntosCliente(ventaActual.getCliente_Id(), -puntosTotales);

                totalLabel.setText(String.format("TOTAL S/ %.2f", ventaActual.getMonto_Total()));
                puntosTotalesLabel.setText("PUNTOS TOTALES: 0");
                valorPuntosLabel.setText("VALOR S/ 0");

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Descuento aplicado correctamente.");
                alert.showAndWait();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al aplicar descuento: " + ex.getMessage());
                alert.showAndWait();
            }
            ventaActual.setDescuento(ventaActual.getDescuento());

        });


        Label tituloCatalogo = new Label("CATÁLOGO DE PRODUCTOS");
        tituloCatalogo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        TableView<Producto> catalogoTable = new TableView<>();
        catalogoTable.setPrefWidth(300);
        catalogoTable.setPrefHeight(500);
        catalogoTable.setStyle("-fx-background-color: white; -fx-border-color: #4D4D4D; -fx-border-width: 1;");
        catalogoTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Producto, String> colCodigo = new TableColumn<>("CÓDIGO");
        TableColumn<Producto, String> colDescripcion = new TableColumn<>("DESCRIPCIÓN");
        TableColumn<Producto, Float> colPUnitario = new TableColumn<>("P. UNITARIO");

        colCodigo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getConcatenadaDesc()));
        colDescripcion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));
        colPUnitario.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrecio_Unitario()));

        catalogoTable.getColumns().addAll(colCodigo, colDescripcion, colPUnitario);

        try {
            ProductoRep productoRep = new ProductoRep();
            ListaEnlazada<Producto> productos = productoRep.obtenerDetallesProductos();

            Nodo<Producto> current = productos.getHead();
            while (current != null) {
                catalogoTable.getItems().add(current.getData());
                current = current.getNext();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar los productos: " + e.getMessage());
            alert.showAndWait();
        }

        VBox catalogoPanel = new VBox(tituloCatalogo, catalogoTable);
        catalogoPanel.setPadding(new Insets(10));
        catalogoPanel.setSpacing(10);
        catalogoPanel.setStyle("-fx-background-color: #4D4D4D; -fx-border-color: black; -fx-border-width: 2;");


        ComboBox<String> categoriaCombo = new ComboBox<>();
        categoriaCombo.getItems().addAll("TODOS");
        categoriaCombo.setPromptText("CATEGORÍA");
        categoriaCombo.setStyle("-fx-background-color: #CCCCCC; -fx-font-weight: bold;");

        ComboBox<String> subcategoriaCombo = new ComboBox<>();
        subcategoriaCombo.getItems().addAll("TODOS");
        subcategoriaCombo.setPromptText("SUBCATEGORÍA");
        subcategoriaCombo.setStyle("-fx-background-color: #CCCCCC; -fx-font-weight: bold;");

        Button filtrarButton = new Button("FILTRAR PRODUCTOS");
        filtrarButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-weight: bold;");

        VBox filtrosPanel = new VBox(categoriaCombo, subcategoriaCombo, filtrarButton);
        filtrosPanel.setSpacing(10);
        filtrosPanel.setPadding(new Insets(10));

        Button registrarVentaButton = new Button("REGISTRAR VENTA");
        registrarVentaButton.setStyle("-fx-font-size: 23px; -fx-background-color: #CCCCCC;");
        Button imprimirButton = new Button("IMPRIMIR");
        imprimirButton.setStyle("-fx-font-size: 23px; -fx-background-color: #CCCCCC;");

        totalLabel = new Label("TOTAL S/ 0.00");
        totalLabel.setStyle("-fx-font-size: 30px; -fx-background-color: #FFA07A;");

        puntosGanadosLabel = new Label("PUNTOS: 0");
        puntosGanadosLabel.setStyle("-fx-font-size: 30px; -fx-background-color: #90EE90;");

        HBox footer = new HBox(20, registrarVentaButton, imprimirButton, puntosGanadosLabel, totalLabel);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-border-color: black; -fx-border-width: 2;");

        registrarVentaButton.setOnAction(e -> {
            if (ventaActual == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Debe buscar o registrar un cliente primero.");
                alert.showAndWait();
                return;
            }

            if (ventas.estaVacia()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Debe agregar al menos un producto a la venta.");
                alert.showAndWait();
                return;
            }

            try {
                actualizarVenta();

                MetodoPagoRep metodoPagoRep = new MetodoPagoRep();
                FormaPagoRep formaPagoRep = new FormaPagoRep();

                int metodoPagoId = metodoPagoRep.obtenerMetodoPagoIdPorDescripcion(formaPagoCombo.getValue());
                int formaPagoId = formaPagoRep.obtenerFormaPagoIdPorDescripcion(tipoPagoCombo.getValue());

                ventaActual.setEmpleado_Id(empleadoId);
                ventaActual.setMetodo_Pago_Id(metodoPagoId);
                ventaActual.setForma_Pago_Id(formaPagoId);

                ventaRep.insertarVenta(ventaActual);

                Nodo<VentaProducto> nodoActual = ventas.getHead();
                while (nodoActual != null) {
                    VentaProducto vp = nodoActual.getData();
                    Detalle_Venta detalleVenta = new Detalle_Venta(
                            vp.getCodigo(),
                            ventaActual.getVenta_Id(),
                            productoRep.buscarProductoIdPorConcatenada(vp.getCodigo()),
                            vp.getCantidad()
                    );
                    detalleVentaRep.insertarOActualizarDetalleVenta(detalleVenta);
                    nodoActual = nodoActual.getNext();
                }

                float puntosGanados = ventaActual.getMonto_Total() * 5;
                ClienteRep clienteRep = new ClienteRep();
                clienteRep.actualizarPuntosCliente(ventaActual.getCliente_Id(), puntosGanados);

                Cliente cliente = clienteRep.buscarClientePorId(ventaActual.getCliente_Id());
                if (cliente != null) {
                    float puntosTotales = cliente.getPuntos();
                    float valorEnSoles = puntosTotales / 1000;

                    puntosTotalesLabel.setText(String.format("PUNTOS TOTALES: %.0f", puntosTotales));
                    valorPuntosLabel.setText(String.format("VALOR S/ %.2f", valorEnSoles));
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Venta registrada con éxito.\nPuntos ganados: " + (int) puntosGanados);
                alert.showAndWait();
                limpiarCamposVenta();

            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al registrar la venta: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        imprimirButton.setOnAction(e -> {
            try {
                int ultimaVentaId = ventaRep.obtenerUltimaVentaId();

                if (ultimaVentaId <= 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "No se encontró ninguna venta registrada.");
                    alert.showAndWait();
                    return;
                }

                String cajeroNombre = "Erick Obradovich Luna";
                new ComprobantePago(ultimaVentaId, false, cajeroNombre);

            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al obtener la última venta: " + ex.getMessage());
                alert.showAndWait();
            }
        });



        TableView<VentaProducto> ventasTable = new TableView<>();
        ventasTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ventasTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                VentaProducto ventaSeleccionada = newValue;
                try {
                    ProductoRep productoRep = new ProductoRep();
                    Producto producto = productoRep.buscarProductoPorConcatenada2(ventaSeleccionada.getCodigo());
                    if (producto != null && producto.getImagen_Referencial() != null) {
                        Image nuevaImagen = new Image(producto.getImagen_Referencial());
                        productImageView.setImage(nuevaImagen);
                    } else {
                        productImageView.setImage(defaultImage);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error al obtener la imagen del producto: " + e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        registrarButton.setOnAction(e -> {
            if (ventaActual == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Debe buscar o registrar un cliente primero.");
                alert.showAndWait();
                return;
            }

            String codigo = codigoField.getText().trim();
            if (codigo.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Ingrese un código.");
                alert.showAndWait();
                return;
            }

            try {
                Producto producto = productoRep.buscarProductoPorConcatenada2(codigo);

                if (producto == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Producto no encontrado.");
                    alert.showAndWait();
                    return;
                }

                boolean productoExistente = false;
                Nodo<VentaProducto> nodoActual = ventas.getHead();

                while (nodoActual != null) {
                    VentaProducto ventaProducto = nodoActual.getData();
                    if (ventaProducto.getCodigo().equals(codigo)) {
                        if (ventaProducto.getCantidad() + 1 > producto.getStock()) {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "No se puede agregar más de lo disponible en stock.");
                            alert.showAndWait();
                            return;
                        }
                        ventaProducto.setCantidad(ventaProducto.getCantidad() + 1);
                        productoExistente = true;
                        break;
                    }
                    nodoActual = nodoActual.getNext();
                }

                if (!productoExistente) {
                    if (producto.getStock() <= 0) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "El producto no tiene stock disponible.");
                        alert.showAndWait();
                        return;
                    }
                    VentaProducto nuevaVentaProducto = new VentaProducto(
                            codigo,
                            producto.getDescripcion(),
                            1,
                            producto.getPrecio_Unitario()
                    );
                    ventas.insertar(nuevaVentaProducto);
                }

                actualizarTablaVentas(ventasTable);
                actualizarTotales();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al registrar el producto: " + ex.getMessage());
                alert.showAndWait();
            }
        });





        TableColumn<VentaProducto, String> colCodigoVentas = new TableColumn<>("CÓDIGO");
        colCodigoVentas.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodigo()));

        TableColumn<VentaProducto, String> colDescripcionVentas = new TableColumn<>("DESCRIPCIÓN");
        colDescripcionVentas.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));


        TableColumn<VentaProducto, Integer> colCantidad = new TableColumn<>("CANTIDAD");
        colCantidad.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCantidad()));

        TableColumn<VentaProducto, Float> colPUnitarioVentas = new TableColumn<>("P. UNITARIO");
        colPUnitarioVentas.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrecioUnitario()));

        TableColumn<VentaProducto, Float> colTotal = new TableColumn<>("TOTAL");
        colTotal.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTotal()));

        ventasTable.getColumns().addAll(colCodigoVentas, colDescripcionVentas, colCantidad, colPUnitarioVentas, colTotal);

        ventasTable.setRowFactory(tv -> {
            TableRow<VentaProducto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    VentaProducto ventaSeleccionada = row.getItem();

                    Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmacion.setTitle("Confirmar eliminación");
                    confirmacion.setHeaderText("¿Desea eliminar este producto?");
                    confirmacion.setContentText("Producto: " + ventaSeleccionada.getDescripcion() + "\nCantidad: " + ventaSeleccionada.getCantidad());

                    ButtonType botonSi = new ButtonType("Sí");
                    ButtonType botonNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                    confirmacion.getButtonTypes().setAll(botonSi, botonNo);

                    confirmacion.showAndWait().ifPresent(opcion -> {
                        if (opcion == botonSi) {
                            eliminarProducto(ventaSeleccionada);
                            actualizarTablaVentas(ventasTable);
                            actualizarVenta();
                        }
                    });
                }
            });
            return row;
        });


        VBox ventasPanel = new VBox(ventasTable);
        ventasPanel.setPadding(new Insets(10));
        ventasPanel.setStyle("-fx-border-color: black; -fx-border-width: 2;");


        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(header);
        mainLayout.setLeft(catalogoPanel);
        mainLayout.setCenter(new VBox(codigoPanel, clientePanel, ventasPanel));
        mainLayout.setRight(new VBox(imagePanel, opcionesVentaPanel, descuentosPanel));
        mainLayout.setBottom(footer);

        Scene scene = new Scene(mainLayout, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Principal Cajero");
        primaryStage.show();
    }

    private void actualizarTablaVentas(TableView<VentaProducto> ventasTable) {
        ObservableList<VentaProducto> items = FXCollections.observableArrayList();

        Nodo<VentaProducto> nodoActual = ventas.getHead();
        while (nodoActual != null) {
            items.add(nodoActual.getData());
            nodoActual = nodoActual.getNext();
        }

        ventasTable.setItems(items);
        ventasTable.refresh();
    }

    private void actualizarTotales() {
        float total = 0;

        Nodo<VentaProducto> nodoActual = ventas.getHead();
        while (nodoActual != null) {
            total += nodoActual.getData().getTotal();
            nodoActual = nodoActual.getNext();
        }

        totalLabel.setText(String.format("TOTAL S/ %.2f", total));
        puntosGanadosLabel.setText(String.format("PUNTOS: %.0f", total * 5));
        ventaActual.setMonto_Total(total);
    }




    private void eliminarProducto(VentaProducto ventaSeleccionada) {
        Nodo<VentaProducto> nodoActual = ventas.getHead();

        while (nodoActual != null) {
            VentaProducto venta = nodoActual.getData();

            if (venta.getCodigo().equals(ventaSeleccionada.getCodigo())) {
                if (venta.getCantidad() > 1) {
                    venta.setCantidad(venta.getCantidad() - 1);
                } else {
                    ventas.eliminar(venta);
                }
                break;
            }
            nodoActual = nodoActual.getNext();
        }

        actualizarTablaVentas(ventasTable);
        actualizarTotales();
    }

    private void actualizarVenta() {
        if (ventaActual != null) {
            float total = 0;
            Nodo<VentaProducto> nodoActual = ventas.getHead();
            while (nodoActual != null) {
                total += nodoActual.getData().getTotal();
                nodoActual = nodoActual.getNext();
            }
            ventaActual.setMonto_Total(total);
        }
    }


    private void limpiarCamposVenta() {
        ventas.clear();

        actualizarTablaVentas(ventasTable);

        ventaActual = null;

        dniField.clear();
        nombreField.clear();

        puntosTotalesLabel.setText("PUNTOS TOTALES: 0");
        valorPuntosLabel.setText("VALOR S/ 0");

        totalLabel.setText("TOTAL S/ 0.00");
        puntosGanadosLabel.setText("PUNTOS: 0");

        productImageView.setImage(defaultImage);

        formaPagoCombo.getSelectionModel().selectFirst();
        tipoPagoCombo.getSelectionModel().selectFirst();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
