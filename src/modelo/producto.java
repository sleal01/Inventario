package modelo;

public class producto {

    private String id="";
    private String descripcion = "";
    private float precio = 0;
    private float precioc = 0;
    private int stock = 0;
    private String categoria="";

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int value) {
        this.stock = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String nombre) {
        this.descripcion = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public float getPrecioC() {
        return precioc;
    }

    public void setPrecioC(float precio) {
        this.precioc = precio;
    }

}