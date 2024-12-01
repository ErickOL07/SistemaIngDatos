package db.modelo;

public class Empleado {
    
    private int Empleado_Id;
    private String Nombre;
    private String Correo_Electronico;
    private String Contrasena;
    private int Rol_Id;
    private String Numero_Documento;

    public Empleado(int Empleado_Id, String Nombre, String Correo_Electronico, String Contrasena, int Rol_Id, String Numero_Documento) {
        this.Empleado_Id = Empleado_Id;
        this.Nombre = Nombre;
        this.Correo_Electronico = Correo_Electronico;
        this.Contrasena = Contrasena;
        this.Rol_Id = Rol_Id;
        this.Numero_Documento = Numero_Documento;
    }

    public int getEmpleado_Id() {
        return Empleado_Id;
    }

    public void setEmpleado_Id(int Empleado_Id) {
        this.Empleado_Id = Empleado_Id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getCorreo_Electronico() {
        return Correo_Electronico;
    }

    public void setCorreo_Electronico(String Correo_Electronico) {
        this.Correo_Electronico = Correo_Electronico;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public void setContrasena(String Contrasena) {
        this.Contrasena = Contrasena;
    }

    public int getRol_Id() {
        return Rol_Id;
    }

    public void setRol_Id(int Rol_Id) {
        this.Rol_Id = Rol_Id;
    }

    public String getNumero_Documento() {
        return Numero_Documento;
    }

    public void setNumero_Documento(String Numero_Documento) {
        this.Numero_Documento = Numero_Documento;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "Empleado_Id=" + Empleado_Id +
                ", Nombre='" + Nombre + '\'' +
                ", Correo_Electronico='" + Correo_Electronico + '\'' +
                ", Contrasena='" + Contrasena + '\'' +
                ", Rol_Id=" + Rol_Id +
                ", Numero_Documento='" + Numero_Documento + '\'' +
                '}';
    }
}
