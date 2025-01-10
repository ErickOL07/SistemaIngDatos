package db.modelo;

public class Forma_Pago {

    private int Forma_Pago_Id;
    private String Descripcion;

    public Forma_Pago(int forma_Pago_Id, String descripcion) {
        Forma_Pago_Id = forma_Pago_Id;
        Descripcion = descripcion;
    }

    public int getForma_Pago_Id() {
        return Forma_Pago_Id;
    }

    public void setForma_Pago_Id(int forma_Pago_Id) {
        Forma_Pago_Id = forma_Pago_Id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
