package db.modelo;

public class Detalle_Venta {

    private int Venta_Id;
    private int Producto_Id;
    private String ConcatenadaDesc;
    private int ConcatenadaId;
    private int Cantidad;

    public Detalle_Venta(String ConcatenadaDesc, int Venta_Id, int Producto_Id, int Cantidad) {
        this.ConcatenadaDesc = ConcatenadaDesc;
        this.Venta_Id = Venta_Id;
        this.Producto_Id = Producto_Id;
        this.Cantidad = Cantidad;
    }

    public Detalle_Venta(int cantidad, int concatenadaId, int Venta_Id, int Producto_Id) {
        this.Cantidad = cantidad;
        this.ConcatenadaId = concatenadaId;
        this.Venta_Id = Venta_Id;
        this.Producto_Id = Producto_Id;
    }

    public int getVentaId() {
        return Venta_Id;
    }

    public void setVentaId(int Venta_Id) {
        this.Venta_Id = Venta_Id;
    }

    public int getProductoId() {
        return Producto_Id;
    }

    public void setProductoId(int Producto_Id) {
        this.Producto_Id = Producto_Id;
    }

    public String getConcatenadaDesc() {
        return ConcatenadaDesc;
    }

    public void setConcatenadaDesc(String ConcatenadaDesc) {
        this.ConcatenadaDesc = ConcatenadaDesc;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }
}
