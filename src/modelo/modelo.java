package modelo;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

public class modelo extends database {

    private DefaultTableModel tablemodel = new DefaultTableModel();

    /** Constructor de clase */
    public modelo (){}

     /** Metodo privado para validar datos */
    private boolean valida_datos(String id, String nombre , String precio, String cantidad, String categoria)
    {
        if( id.equals("  -   ") )
            return false;
        else if( nombre.length() > 0 && precio.length()>0 && cantidad.length() >0  && categoria.length()>0)
        {
            return true;
        }
        else  return false;
    }

    /** Añade un nuevo registro a la base de datos */
    public boolean NuevoProducto(String id, String nombre , String precio, String cantidad, String categoria)
    {
        if( valida_datos( id, nombre, precio, cantidad, categoria ) )
        {
            //se reemplaza "," por "."
            precio = precio.replace(",", ".");
            //Se arma la consulta
            String q=" INSERT INTO tproducto ( idprod , descripcion , preciov, stock , idcat ) "
                + "VALUES ( '" + id + "','" + nombre + "', '" + precio + "'," + cantidad + " , '"+ categoria+"' ) ";
            //se ejecuta la consulta
            try {
                PreparedStatement pstm = this.getConexion().prepareStatement(q);
                pstm.execute();
                pstm.close();
                return true;
            }catch(SQLException e){
                System.err.println( e.getMessage() );
            }
            return false;
        }
        else return false;
    }

    /** Actualiza producto condicional WHERE el ID del producto */
    public boolean ActualizarProducto( String id, String nombre , String precio, String cantidad, String categoria )
    {
        //se reemplaza "," por "."
        precio = precio.replace(",", ".");
        String q= "UPDATE tproducto SET descripcion='"+nombre+"', preciov='"+precio+"' , stock='"+cantidad+"' , idcat='"+categoria+"' "
                + " WHERE idprod='"+id+"' ";
         //se ejecuta la consulta
        try {
            PreparedStatement pstm = this.getConexion().prepareStatement(q);
            pstm.execute();
            pstm.close();
            return true;
         }catch(SQLException e){
            System.err.println( e.getMessage() );
        }
        return false;
    }

    /**
     * Registra nueva venta ademas de detalle de ventas y actualiza stock de productos
     */
    public boolean RegistrarVenta( String idventa , Map productos , String total, String detalle , String nit )
    {
        try {
            //registra venta
            String q = " INSERT tventa (idventa, fecha, preciot, detalle, idcliente ) "
                + "values( '"+idventa+"', '"+getFecha()+"', '"+total.replace(",", ".")+"', '"+detalle+"' , '"+nit+"' );";
            PreparedStatement pstm = this.getConexion().prepareStatement(q);
            pstm.execute();
            pstm.close();
            //registra venta de productos
            Iterator it = productos.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry)it.next();
                carrito itm = (carrito) e.getValue();
                pstm = this.getConexion().prepareStatement(" INSERT INTO tpv (idprod, idventa, cantidad, precio)"
                        + " values( '"+itm.getIdproducto()+"' , '"+idventa+"' , '"+itm.getCantidad()+"' , '"+itm.getPrecio().replace(",", ".")+"' );");
                pstm.execute();
                pstm.close();                
                //actualiza stock
                pstm = this.getConexion().prepareStatement(" UPDATE tproducto SET stock=stock-"+itm.getCantidad()+" WHERE idprod='"+itm.getIdproducto()+"' ");
                pstm.execute();
                pstm.close();
            }
            return true;
         }catch(SQLException e){
            System.err.println( e.getMessage() );
        }
        return false;
    }

    /**
     * Registra nueva venta ademas de detalle de ventas y actualiza stock de productos
     */
    public boolean RegistrarCompra( String idventa , Map productos , String total, String detalle , String nit )
    {
        try {
            //registra venta
            String q = " INSERT tcompra (idcompra, fecha, precioc, detalle, idprov ) "
                + "values( '"+idventa+"', '"+getFecha()+"', '"+total.replace(",", ".")+"', '"+detalle+"' , '"+nit+"' );";
            PreparedStatement pstm = this.getConexion().prepareStatement(q);
            pstm.execute();
            pstm.close();
            //registra venta de productos
            Iterator it = productos.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry)it.next();
                carrito itm = (carrito) e.getValue();
                pstm = this.getConexion().prepareStatement(" INSERT INTO tpv (idprod, idventa, cantidad, precio)"
                        + " values( '"+itm.getIdproducto()+"' , '"+idventa+"' , '"+itm.getCantidadNeg()+"' , '"+itm.getPrecio().replace(",", ".")+"' );");
                pstm.execute();
                pstm.close();                
                //actualiza stock
                pstm = this.getConexion().prepareStatement(" UPDATE tproducto SET stock=stock+"+itm.getCantidad()+" WHERE idprod='"+itm.getIdproducto()+"' ");
                pstm.execute();
                pstm.close();
            }
            return true;
         }catch(SQLException e){
            System.err.println( e.getMessage() );
        }
        return false;
    }

    /**
     * Obtiene los datos de una venta dado su ID en un DefaultTableModel
     */
    public DefaultTableModel getTablaVenta( String idventa )
    {
      String q1= "SELECT count(*) as total "
              + " FROM tproducto INNER JOIN tpv ON idprod=idprod WHERE idventa='"+idventa+"'";

      String q2= "SELECT p.idprod,p.descripcion,tr.cantidad,tr.precio "
              + " FROM tproducto p INNER JOIN tpv tr ON tr.idprod=p.idprod WHERE idventa='"+idventa+"'";
     //realizamos la consulta sql y llenamos los datos en la matriz "Object[][] data"
      int registros = 0;
      String[] columNames = {"ID Producto","Descripcion","Cantidad","Precio Unitario","Precio Total"};
      //obtenemos la cantidad de registros existentes en la tabla y se almacena en la variable "registros"
      try{
         PreparedStatement pstm = this.getConexion().prepareStatement( q1 );
         ResultSet res = pstm.executeQuery();
         res.next();
         registros = res.getInt("total");
         res.close();
      }catch(SQLException e){
         System.err.println( e.getMessage() );
      }
    //se crea una matriz con tantas filas y columnas que necesite
    Object[][] data = new String[registros][6];
      try{
         PreparedStatement pstm = this.getConexion().prepareStatement( q2 );
         ResultSet res = pstm.executeQuery();
         int i=0;
         while(res.next()){
                data[i][0] =res.getString( "idprod" );
                data[i][1] = res.getString( "descripcion" );
                data[i][2] =res.getString( "cantidad" );
                data[i][3] =res.getString( "precio" );
                data[i][4] = ""+(res.getFloat( "precio" ) * res.getInt( "cantidad" ));
            i++;
         }
         res.close();
         this.tablemodel.setDataVector(data, columNames );
         }catch(SQLException e){
            System.err.println( e.getMessage() );
        }
        return this.tablemodel;
    }

    /**
     * Obtiene los datos de ventas segun un rango de fechas de la forma yyyy-MM-dd
     */
    public DefaultTableModel getDatosVentaxFechas( Date fecha1, Date fecha2  )
    {
      SimpleDateFormat _sdf= new SimpleDateFormat("yyyy-MM-dd");
      /* String q1a= "SELECT count(*) as total FROM tventa tv INNER JOIN tcliente cte ON cte.idcliente=tv.idcliente "
            + " WHERE date(fecha) BETWEEN '"+_sdf.format(fecha1)+"' and '"+_sdf.format(fecha2)+"' ";
      */

      String q1= "SELECT count(*) as total FROM ( "
            + "SELECT tv.idventa FROM tventa tv INNER JOIN tcliente cte ON cte.idcliente=tv.idcliente"
            + " WHERE date(fecha) BETWEEN '"+_sdf.format(fecha1)+"' and '"+_sdf.format(fecha2)+"' "
            + "UNION SELECT tc.idcompra FROM tcompra tc INNER JOIN tproveedor pro ON pro.idprov=tc.idprov "
            + " WHERE date(fecha) BETWEEN '"+_sdf.format(fecha1)+"' and '"+_sdf.format(fecha2)+"') a ";
      
      String q2= "SELECT CONCAT('Vta:',tv.idventa) as idventa, tv.fecha, cte.idcliente, cte.nombre, tv.preciot "
              + " FROM tventa tv INNER JOIN tcliente cte ON cte.idcliente=tv.idcliente "
              + "WHERE date(fecha) BETWEEN '"+_sdf.format(fecha1)+"' and '"+_sdf.format(fecha2)+"' "
              + " UNION SELECT CONCAT('Cmp:',tc.idcompra), tc.fecha, pro.idprov, pro.nombre, tc.precioc"
              + " FROM tcompra tc INNER JOIN tproveedor pro ON pro.idprov=tc.idprov "
              + "WHERE date(fecha) BETWEEN '"+_sdf.format(fecha1)+"' and '"+_sdf.format(fecha2)+"' ";     
      int registros = 0;
      String[] columNames = {"ID Venta","Fecha","IDCte","Cliente","Precio Total"};
      //obtenemos la cantidad de registros existentes en la tabla y se almacena en la variable "registros"
      try{
         PreparedStatement pstm = this.getConexion().prepareStatement( q1 );
         ResultSet res = pstm.executeQuery();
         res.next();
         registros = res.getInt("total");
         res.close();
      }catch(SQLException e){
         System.err.println( e.getMessage() );
      }
    //se crea una matriz con tantas filas y columnas que necesite
    Object[][] data = new String[registros][6];
      try{
         PreparedStatement pstm = this.getConexion().prepareStatement( q2 );
         ResultSet res = pstm.executeQuery();
         int i=0;
         while(res.next()){
                data[i][0] = res.getString( "idventa" );
                data[i][1] = res.getString( "fecha" );
                data[i][2] = res.getString( "idcliente" );
                data[i][3] = res.getString( "nombre" );
                data[i][4] = res.getString( "preciot" );
            i++;
         }
         res.close();
         this.tablemodel.setDataVector(data, columNames );
         }catch(SQLException e){
            System.err.println( e.getMessage() );
        }
        return this.tablemodel;
    }

    /**
     * Obtiene los registros de los productos del inventario en un DefaultTableModel
     */
    public DefaultTableModel getTablaProducto()
    {      
      int registros = 0;
      String[] columNames = {"ID","Descripcion","Precio Venta","Stock","Categoria"};
      //obtenemos la cantidad de registros existentes en la tabla y se almacena en la variable "registros"
      try{
         PreparedStatement pstm = this.getConexion().prepareStatement( "SELECT count(*) as total FROM tproducto");
         ResultSet res = pstm.executeQuery();
         res.next();
         registros = res.getInt("total");
         res.close();
      }catch(SQLException e){
         System.err.println( e.getMessage() );
      }
    //se crea una matriz con tantas filas y columnas que necesite
    Object[][] data = new String[registros][6];
      try{
         PreparedStatement pstm = this.getConexion().prepareStatement("SELECT * FROM tproducto p INNER JOIN tcategoria cat on cat.idcat = p.idcat");
         ResultSet res = pstm.executeQuery();
         int i=0;
         while(res.next()){                         
                data[i][0] =res.getString( "idprod" );
                data[i][1] = res.getString( "descripcion" );
                data[i][2] =res.getString( "preciov" );
                data[i][3] =res.getString( "stock" );
                data[i][4] = res.getString( "idcat" ) + " - " + res.getString( "descripcion" );
                data[i][5] =res.getString( "precioc" );
            i++;
         }
         res.close();
         this.tablemodel.setDataVector(data, columNames );
         }catch(SQLException e){
            System.err.println( e.getMessage() );
        }
        return this.tablemodel;
    }

    /**
     * Obtiene las categorias del sistema en un DefaultComboBoxModel
     */
    public DefaultComboBoxModel getListaCategorias()
    {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
         try{
         PreparedStatement pstm = this.getConexion().prepareStatement("SELECT * FROM tcategoria ");
         ResultSet res = pstm.executeQuery();
         while(res.next()){
                model.addElement( res.getString( "idcat" ) + " - " + res.getString( "descripcion" ) );
         }
         res.close();
         }catch(SQLException e){
            System.err.println( e.getMessage() );
        }
        return model;
    }

    /**
     * Obtiene los registros de clientes NIT y nombre completo en un DefaultComboBoxModel
     */
    public DefaultComboBoxModel getListaClientes()
    {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
         try{
         PreparedStatement pstm = this.getConexion().prepareStatement("SELECT * FROM tcliente ");
         ResultSet res = pstm.executeQuery();
         while(res.next()){
                model.addElement( "Cte: - " + res.getString( "idcliente" ) + " - " + res.getString( "nombre" ) );
         }
         res.close();
         }catch(SQLException e){
            System.err.println( e.getMessage() );
        }
        return model;
    }

    /**
     * Obtiene los registros de clientes NIT y nombre completo en un DefaultComboBoxModel
     */
    public DefaultComboBoxModel getListaProveedores()
    {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
         try{
         PreparedStatement pstm = this.getConexion().prepareStatement("SELECT * FROM tproveedor ");
         ResultSet res = pstm.executeQuery();
         while(res.next()){
                model.addElement( "Prov: - " + res.getString( "idprov" ) + " - " + res.getString( "nombre" ) );
         }
         res.close();
         }catch(SQLException e){
            System.err.println( e.getMessage() );
        }
        return model;
    }


    /**
     * Genera un codigo aleatorio alfanumerico de 8 caracteres
     */
    public String getRandomCode()
    {
        String code = "";
        long milis = new java.util.GregorianCalendar().getTimeInMillis();
        Random r = new Random(milis);
        int i = 0;
        while ( i < 8){
            char c = (char)r.nextInt(255);
            if ( (c >= '0' && c <='9') || (c >='A' && c <='Z') ){
                code += c;
                i ++;
            }
        }
        return code;
    }

    /**
     * obtiene la fecha actual del sistema en el formato de MYSQL
     */
    public String getFecha()
    {
        SimpleDateFormat _sdf= new SimpleDateFormat("yyyy-MM-dd");
        return _sdf.format(new Date());
    }

    public DefaultComboBoxModel getListaProductos()
    {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
         try{
         PreparedStatement pstm = this.getConexion().prepareStatement("SELECT descripcion FROM tproducto ");
         ResultSet res = pstm.executeQuery();         
         while(res.next()){                                                         
                model.addElement( res.getString( "descripcion" ) );
         }
         res.close();         
         }catch(SQLException e){
            System.err.println( e.getMessage() );
        }        
        return model;
    }

    /**
     * Dado un MAP que contiene clases "carrito.java" genera un DefaultComboBoxModel y retorna
     */
    public DefaultComboBoxModel ListaProductos( Map carrito_compra )
    {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        Iterator it = carrito_compra.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry)it.next();
            carrito itm = (carrito) e.getValue();
            model.addElement( itm.getIdproducto() + " | " + itm.getDescripcion() + " | " + itm.getPrecio() + "           | " + itm.getCantidad() );
        }
        return model;
    }

    /**
     * dado un MAP que contiene clases "carrito.java" recorre todos los productos contenidos y calcula el total de la venta
     */
    public float getTotalVenta( Map carrito_compra )
    {
        float total=0;
        Iterator it = carrito_compra.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry)it.next();
            carrito itm = (carrito) e.getValue();
            total += Float.valueOf(itm.getPrecio().toString()) * Integer.valueOf(itm.getCantidad().toString()) ;
        }
        return total;
    }
    
    /**
     * Obtiene los datos de un producto dado su nombre
     */
    public producto getProducto( String nombre )
    {
        producto p = new producto();
        String q = "SELECT * FROM tproducto WHERE descripcion = '"+nombre+"' ";
         try{
         PreparedStatement pstm = this.getConexion().prepareStatement(q);
         ResultSet res = pstm.executeQuery();
         while(res.next()){
            p.setId( res.getString( "idprod" ) );
            p.setDescripcion( res.getString( "descripcion" ) );
            p.setPrecio( res.getFloat( "preciov" ) );
            p.setPrecioC( res.getFloat( "precioc" ) );
            p.setStock( res.getInt( "stock" ) );
            p.setCategoria( res.getString( "idcat" ) );
         }
         res.close();
         }catch(SQLException e){
            System.err.println( e.getMessage() );
        }
        return p;
    }

    /**
     * Obtiene en un string los datos generales de una venta dado su ID
     */
    public String getDatosVenta( String idventa )
    {
        String resultado="";         
        String q = "SELECT * FROM tventa vta INNER JOIN tcliente cte ON cte.idcliente=vta.idcliente"
                + " WHERE v_id = '"+idventa+"' ";
        try{
         PreparedStatement pstm = this.getConexion().prepareStatement(q);
         ResultSet res = pstm.executeQuery();
         while(res.next()){
             resultado += "Cliente: " + res.getString("nombre") + "\n";
             resultado += "NIT : " + res.getString("idcliente") + "\n";
             resultado += "Dirección : " + res.getString("dir") + "\n";
             resultado += "Fecha : " + res.getString("fecha") + "\n";
             resultado +="----------------------------------------\n";
             resultado += "TOTAL [BS.] : " + res.getString("preciot") + "\n";
             resultado += "Detalle: " + res.getString("detalle") + "\n";
        }
         res.close();
        }catch(SQLException e){
            System.err.println( e.getMessage() );
        }
        return resultado;
    }

}
