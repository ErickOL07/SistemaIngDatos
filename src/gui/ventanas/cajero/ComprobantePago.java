package gui.ventanas.cajero;

import db.repositorio.VentaRep;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ComprobantePago extends Stage {
    public ComprobantePago(int ventaId, boolean esTemporal, String cajeroNombre) {
        setTitle("Comprobante de Pago");
        VentaRep ventaRep = new VentaRep();

        Image logo = new Image("https://objectstorage.us-ashburn-1.oraclecloud.com/n/idew1j1vbcak/b/bucket-20241201-0716/o/Mundo%20Mascotas%20-%20Logo%20Negro.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(250);
        logoView.setPreserveRatio(true);

        try {
            ResultSet reporte = ventaRep.generarReporteVenta(ventaId);

            if (!reporte.next()) {
                throw new SQLException("No se encontró información para la venta ID: " + ventaId);
            }

            String empresa = reporte.getString("Empresa");
            String ruc = reporte.getString("RUC");
            String ciudad = reporte.getString("Ciudad");
            String telefono = reporte.getString("Telefono");
            String tipoComprobante = reporte.getString("Tipo_Comprobante");
            String serieComprobante = reporte.getString("Serie_Comprobante");
            String fecha = reporte.getString("Fecha");
            String cliente = reporte.getString("Cliente");
            String tipoDocumento = reporte.getString("Tipo_Documento_Cliente");
            String numeroDocumento = reporte.getString("Numero_Documento_Cliente");

            float subtotal = reporte.getFloat("Subtotal");
            float descuento = reporte.getFloat("Descuento");
            float igv = reporte.getFloat("IGV");
            float total = reporte.getFloat("Total");

            VBox encabezado = new VBox(5,
                    logoView,
                    styledLabel(empresa, "-fx-font-size: 14px; -fx-font-weight: bold;"),
                    new Label("R.U.C. " + ruc),
                    new Label(ciudad),
                    new Label("Teléfono: " + telefono),
                    styledLabel(tipoComprobante, "-fx-font-size: 16px; -fx-font-weight: bold;"),
                    styledLabel(serieComprobante, "-fx-font-size: 14px;")
            );
            encabezado.setAlignment(Pos.CENTER);

            Line line1 = new Line(0, 0, 600, 0);
            line1.setStrokeWidth(1);

            GridPane infoVenta = new GridPane();
            infoVenta.setHgap(10);
            infoVenta.setVgap(5);
            infoVenta.setPadding(new Insets(10));
            infoVenta.add(styledLabel("Fecha:", "-fx-font-weight: bold;"), 0, 0);
            infoVenta.add(new Label(fecha), 1, 0);
            infoVenta.add(styledLabel("Cajero:", "-fx-font-weight: bold;"), 0, 1);
            infoVenta.add(new Label(cajeroNombre), 1, 1);
            infoVenta.add(styledLabel("Cliente:", "-fx-font-weight: bold;"), 0, 2);
            infoVenta.add(new Label(cliente), 1, 2);
            infoVenta.add(styledLabel(tipoDocumento + ":", "-fx-font-weight: bold;"), 0, 3);
            infoVenta.add(new Label(numeroDocumento), 1, 3);

            Line line2 = new Line(0, 0, 600, 0);
            line2.setStrokeWidth(1);

            GridPane productosGrid = new GridPane();
            productosGrid.setHgap(10);
            productosGrid.setVgap(5);
            productosGrid.setPadding(new Insets(10));

            productosGrid.add(styledLabel("Producto", "-fx-font-weight: bold;"), 0, 0);
            productosGrid.add(styledLabel("Cant.", "-fx-font-weight: bold;"), 1, 0);
            productosGrid.add(styledLabel("Precio", "-fx-font-weight: bold;"), 2, 0);
            productosGrid.add(styledLabel("Importe", "-fx-font-weight: bold;"), 3, 0);

            int rowIndex = 1;
            do {
                String producto = reporte.getString("Producto");
                int cantidad = reporte.getInt("Cantidad");
                float precioUnitario = reporte.getFloat("Precio_Unitario");
                float importe = reporte.getFloat("Importe");

                productosGrid.add(new Label(producto), 0, rowIndex);
                productosGrid.add(new Label(String.valueOf(cantidad)), 1, rowIndex);
                productosGrid.add(new Label(String.format("%.2f", precioUnitario)), 2, rowIndex);
                productosGrid.add(new Label(String.format("%.2f", importe)), 3, rowIndex);

                rowIndex++;
            } while (reporte.next());

            productosGrid.add(new Label(" "), 0, rowIndex);
            productosGrid.add(styledLabel("Los precios no incluyen IGV", "-fx-font-style: italic; -fx-font-size: 10px;"), 0, rowIndex+1);

            Line line3 = new Line(0, 0, 600, 0);
            line3.setStrokeWidth(1);

            GridPane totalesGrid = new GridPane();
            totalesGrid.setHgap(10);
            totalesGrid.setPadding(new Insets(10));
            totalesGrid.add(styledLabel("Subtotal:", "-fx-font-weight: bold;"), 0, 0);
            totalesGrid.add(new Label(String.format("%.2f", subtotal)), 1, 0);
            totalesGrid.add(styledLabel("Descuento:", "-fx-font-weight: bold;"), 0, 1);
            totalesGrid.add(new Label(String.format("%.2f", descuento)), 1, 1);
            totalesGrid.add(styledLabel("IGV:", "-fx-font-weight: bold;"), 0, 2);
            totalesGrid.add(new Label(String.format("%.2f", igv)), 1, 2);
            totalesGrid.add(styledLabel("TOTAL:", "-fx-font-weight: bold;"), 0, 3);
            totalesGrid.add(styledLabel(String.format("%.2f", total), "-fx-font-size: 14px; -fx-font-weight: bold;"), 1, 3);

            VBox layout = new VBox(20, encabezado, line1, infoVenta, line2, productosGrid, line3, totalesGrid);
            layout.setPadding(new Insets(20));
            layout.setAlignment(Pos.TOP_CENTER);

            Scene scene = new Scene(layout, 600, 800);
            setScene(scene);
            show();

            setOnCloseRequest(event -> {
                if (esTemporal) {
                    try {
                        ventaRep.eliminarVenta(ventaId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Label styledLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        return label;
    }
}
