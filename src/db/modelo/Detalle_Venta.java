package db.modelo;

public class Detalle_Venta {
    
    private int Venta_Id;
    private int Producto_Id;
    private int Cantidad;

    public Detalle_Venta(int Venta_Id, int Producto_Id, int Cantidad) {
        this.Venta_Id = Venta_Id;
        this.Producto_Id = Producto_Id;
        this.Cantidad = Cantidad;
    }

    public int getVenta_Id() {
        return Venta_Id;
    }

    public void setVenta_Id(int Venta_Id) {
        this.Venta_Id = Venta_Id;
    }

    public int getProducto_Id() {
        return Producto_Id;
    }

    public void setProducto_Id(int Producto_Id) {
        this.Producto_Id = Producto_Id;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }
            
}
