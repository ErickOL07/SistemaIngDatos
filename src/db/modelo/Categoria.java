package db.modelo;

public class Categoria {
    
    private int Categoria_Id;
    private String Descripcion;

    public Categoria(int Categoria_Id, String Descripcion) {
        this.Categoria_Id = Categoria_Id;
        this.Descripcion = Descripcion;
    }

    public int getCategoria_Id() {
        return Categoria_Id;
    }

    public void setCategoria_Id(int Categoria_Id) {
        this.Categoria_Id = Categoria_Id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
    
}
