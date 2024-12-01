package db.modelo;

public class Rol {
    
    private int Rol_Id;
    private String Descripcion;

    public Rol(int Rol_Id, String Descripcion) {
        this.Rol_Id = Rol_Id;
        this.Descripcion = Descripcion;
    }

    public int getRol_Id() {
        return Rol_Id;
    }

    public void setRol_Id(int Rol_Id) {
        this.Rol_Id = Rol_Id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

}
