package modelo;

public class carrito {

    private String idproducto="";
    private String descripcion="";
    private String cantidad="0";
    private String precio="0";
    
    public String getCantidad() {
        return cantidad;
    }

    public String getCantidadNeg() {
        return "-"+cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }    

    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

}