package db.modelo;

public class Tipo_Documento {
    
    private int Tipo_Documento_Id;
    private String Descripcion;

    public Tipo_Documento(int Tipo_Documento_Id, String Descripcion) {
        this.Tipo_Documento_Id = Tipo_Documento_Id;
        this.Descripcion = Descripcion;
    }

    public int getTipo_Documento_Id() {
        return Tipo_Documento_Id;
    }

    public void setTipo_Documento_Id(int Tipo_Documento_Id) {
        this.Tipo_Documento_Id = Tipo_Documento_Id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

}
