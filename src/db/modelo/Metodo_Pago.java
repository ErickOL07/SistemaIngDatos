package db.modelo;

public class Metodo_Pago {
    
    private int Metodo_Pago_Id;
    private String Descripcion;

    public Metodo_Pago(int Metodo_Pago_Id, String Descripcion) {
        this.Metodo_Pago_Id = Metodo_Pago_Id;
        this.Descripcion = Descripcion;
    }

    public int getMetodo_Pago_Id() {
        return Metodo_Pago_Id;
    }

    public void setMetodo_Pago_Id(int Metodo_Pago_Id) {
        this.Metodo_Pago_Id = Metodo_Pago_Id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
    
}
