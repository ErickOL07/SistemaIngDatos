package db.modelo;

public class Venta {

    private int Venta_Id;
    private float Monto_Total;
    private float Descuento;
    private String Fecha_Venta;
    private int Cliente_Id;
    private int Empleado_Id;
    private int Metodo_Pago_Id;
    private int Forma_Pago_Id;

    public Venta(int Venta_Id, float Monto_Total, float Descuento, String Fecha_Venta, int Cliente_Id, int Empleado_Id, int Metodo_Pago_Id, int Forma_Pago_Id) {
        this.Venta_Id = Venta_Id;
        this.Monto_Total = Monto_Total;
        this.Descuento = Descuento;
        this.Fecha_Venta = Fecha_Venta;
        this.Cliente_Id = Cliente_Id;
        this.Empleado_Id = Empleado_Id;
        this.Metodo_Pago_Id = Metodo_Pago_Id;
        this.Forma_Pago_Id = Forma_Pago_Id;
    }

    public float getDescuento() {
        return Descuento;
    }

    public void setDescuento(float Descuento) {
        this.Descuento = Descuento;
    }

    public int getVenta_Id() {
        return Venta_Id;
    }

    public void setVenta_Id(int Venta_Id) {
        this.Venta_Id = Venta_Id;
    }

    public float getMonto_Total() {
        return Monto_Total;
    }

    public void setMonto_Total(float Monto_Total) {
        this.Monto_Total = Monto_Total;
    }

    public String getFecha_Venta() {
        return Fecha_Venta;
    }

    public void setFecha_Venta(String Fecha_Venta) {
        this.Fecha_Venta = Fecha_Venta;
    }

    public int getCliente_Id() {
        return Cliente_Id;
    }

    public void setCliente_Id(int Cliente_Id) {
        this.Cliente_Id = Cliente_Id;
    }

    public int getEmpleado_Id() {
        return Empleado_Id;
    }

    public void setEmpleado_Id(int Empleado_Id) {
        this.Empleado_Id = Empleado_Id;
    }

    public int getMetodo_Pago_Id() {
        return Metodo_Pago_Id;
    }

    public void setMetodo_Pago_Id(int Metodo_Pago_Id) {
        this.Metodo_Pago_Id = Metodo_Pago_Id;
    }

    public int getForma_Pago_Id() {
        return Forma_Pago_Id;
    }

    public void setForma_Pago_Id(int Forma_Pago_Id) {
        this.Forma_Pago_Id = Forma_Pago_Id;
    }
}
