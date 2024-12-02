package db.modelo;

public class Producto {
    private int Producto_Id;
    private String Descripcion;
    private float Precio_Unitario;
    private int Stock;
    private String Imagen_Referencial;
    private int Subcategoria_Id;
    private String Categoria; // Nuevo atributo para la categoría
    private String Subcategoria; // Nuevo atributo para la subcategoría
    private String concatenadaDesc;

    public Producto(int Producto_Id, String Descripcion, float Precio_Unitario, int Stock, String Imagen_Referencial, int Subcategoria_Id) {
        this.Producto_Id = Producto_Id;
        this.Descripcion = Descripcion;
        this.Precio_Unitario = Precio_Unitario;
        this.Stock = Stock;
        this.Imagen_Referencial = Imagen_Referencial;
        this.Subcategoria_Id = Subcategoria_Id;
    }

    public Producto(String descripcionConcatenada, String categoria, String subcategoria, String descripcionProducto, float precioUnitarioProducto, int stockProducto) {
        this.concatenadaDesc = descripcionConcatenada;
        this.Categoria = categoria;
        this.Subcategoria = subcategoria;
        this.Descripcion = descripcionProducto;
        this.Precio_Unitario = precioUnitarioProducto;
        this.Stock = stockProducto;
    }

    // Getters y Setters


    public String getConcatenadaDesc() {
        return concatenadaDesc;
    }

    public void setConcatenadaDesc(String concatenadaDesc) {
        this.concatenadaDesc = concatenadaDesc;
    }


    public int getProducto_Id() {
        return Producto_Id;
    }

    public void setProducto_Id(int Producto_Id) {
        this.Producto_Id = Producto_Id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public float getPrecio_Unitario() {
        return Precio_Unitario;
    }

    public void setPrecio_Unitario(float Precio_Unitario) {
        this.Precio_Unitario = Precio_Unitario;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int Stock) {
        this.Stock = Stock;
    }

    public String getImagen_Referencial() {
        return Imagen_Referencial;
    }

    public void setImagen_Referencial(String Imagen_Referencial) {
        this.Imagen_Referencial = Imagen_Referencial;
    }

    public int getSubcategoria_Id() {
        return Subcategoria_Id;
    }

    public void setSubcategoria_Id(int Subcategoria_Id) {
        this.Subcategoria_Id = Subcategoria_Id;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    public String getSubcategoria() {
        return Subcategoria;
    }

    public void setSubcategoria(String Subcategoria) {
        this.Subcategoria = Subcategoria;
    }
}
