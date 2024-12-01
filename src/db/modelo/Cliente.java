package db.modelo;

public class Cliente {
    
    private int Cliente_Id;
    private String Nombre_Razon_Social;
    private float Puntos;
    private int Tipo_Documento_Id;
    private String Numero_Documento;

    public Cliente(int Cliente_Id, String Nombre_Razon_Social, float Puntos, int Tipo_Documento_Id, String Numero_Documento) {
        this.Cliente_Id = Cliente_Id;
        this.Nombre_Razon_Social = Nombre_Razon_Social;
        this.Puntos = Puntos;
        this.Tipo_Documento_Id = Tipo_Documento_Id;
        this.Numero_Documento = Numero_Documento;
    }

    public int getCliente_Id() {
        return Cliente_Id;
    }

    public void setCliente_Id(int Cliente_Id) {
        this.Cliente_Id = Cliente_Id;
    }

    public String getNombre_Razon_Social() {
        return Nombre_Razon_Social;
    }

    public void setNombre_Razon_Social(String Nombre_Razon_Social) {
        this.Nombre_Razon_Social = Nombre_Razon_Social;
    }

    public float getPuntos() {
        return Puntos;
    }

    public void setPuntos(float Puntos) {
        this.Puntos = Puntos;
    }

    public int getTipo_Documento_Id() {
        return Tipo_Documento_Id;
    }

    public void setTipo_Documento_Id(int Tipo_Documento_Id) {
        this.Tipo_Documento_Id = Tipo_Documento_Id;
    }

    public String getNumero_Documento() {
        return Numero_Documento;
    }

    public void setNumero_Documento(String Numero_Documento) {
        this.Numero_Documento = Numero_Documento;
    }

    
    
}
