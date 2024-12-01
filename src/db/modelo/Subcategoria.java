package db.modelo;

public class Subcategoria {
    
    private int Subcategoria_Id;
    private String Descripcion;
    private int Categoria_Id;

    public Subcategoria(int Subcategoria_Id, String Descripcion, int Categoria_Id) {
        this.Subcategoria_Id = Subcategoria_Id;
        this.Descripcion = Descripcion;
        this.Categoria_Id = Categoria_Id;
    }

    public int getSubcategoria_Id() {
        return Subcategoria_Id;
    }

    public void setSubcategoria_Id(int Subcategoria_Id) {
        this.Subcategoria_Id = Subcategoria_Id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public int getCategoria_Id() {
        return Categoria_Id;
    }

    public void setCategoria_Id(int Categoria_Id) {
        this.Categoria_Id = Categoria_Id;
    }
    
}
