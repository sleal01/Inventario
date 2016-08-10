package controlador;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
//Logica de aplicación -> MODELO
import modelo.carrito;
import modelo.modelo;
import modelo.producto;
//interfaz de usuario -> VISTA
import vista.frmConsulta;
import vista.frmPrincipal;
import vista.frmProducto;
import vista.frmVenta;
import vista.frmCompra;

public class jcontrolador implements ActionListener,MouseListener{
  
    //vista
    private frmPrincipal frmprincipal ;
    private frmProducto frmproducto = new frmProducto();
    private frmVenta frmventa = new frmVenta();
    private frmCompra frmcompra = new frmCompra();
    private frmConsulta frmconsulta = new frmConsulta();
    //modelo
    public modelo mimodelo = new modelo();
    
    //variable para almacenar los productos de una venta
    private Map carrito_compra = new HashMap();

     //acciones que se ejecuta por los controles de cada VISTA
    public enum Accion
    {
        __VER_PRODUCTOS, //-> Abre VISTA productos
        __VER_NUEVA_VENTA, //abre VISTA nueva venta
        __VER_NUEVA_COMPRA, //abre VISTA nueva compra
        __CONSULTAS, //-> Abre VISTA para realizar consultas
        
        __AGREGAR_PRODUCTO,//-> Ejecuta consulta
        __ACTUALIZAR_PRODUCTO,//-> Ejecuta consulta

        __NUEVA_VENTA,//-> genera un codigo al azar y limpia controles
        __NUEVA_COMPRA,//-> genera un codigo al azar y limpia controles
        __lista_productos,//->se ejecuta cuando se hace clic en el jcombobox  de productos de  VISTA frmVenta
        __lista_productos_compra,//->se ejecuta cuando se hace clic en el jcombobox  de productos de  VISTA frmVenta
        __AGREGAR_A_LISTA,//->asigna un producto al carrito de compras
        __AGREGAR_A_LISTA_COMPRA,//->asigna un producto al carrito de compras
        __REMOVER_PRODUCTO,//-> remueve un producto del carrito de compras
        __REGISTRAR_VENTA,//-> registra una venta en la base de datos
        __REGISTRAR_COMPRA,//-> registra una compra en la base de datos

        __PROCESAR_CONSULTA, //-> Ejecuta consulta -> busqueda de venta por ID
        __PROCESAR_CONSULTA2//-> Ejecuta consulta -> busqueda de venta por rango de fechas      

    }

    /** Constructor de clase */
    public jcontrolador( JFrame padre )
    {
        this.frmprincipal = (frmPrincipal) padre;
    }

    //EVENTOS DEL MOUSE
    public void mouseClicked(MouseEvent e) {
        if( e.getButton()== 1)//boton izquierdo
        {
             int fila = this.frmproducto.__TABLA_PRODUCTO.rowAtPoint(e.getPoint());
             if (fila > -1){
                this.frmproducto.__id.setText( String.valueOf( this.frmproducto.__TABLA_PRODUCTO.getValueAt(fila, 0) ));
                this.frmproducto.__nombre.setText( String.valueOf( this.frmproducto.__TABLA_PRODUCTO.getValueAt(fila, 1) ));
                this.frmproducto.__precio.setText( String.valueOf( this.frmproducto.__TABLA_PRODUCTO.getValueAt(fila, 2) ));
                this.frmproducto.__cantidad.setText( String.valueOf( this.frmproducto.__TABLA_PRODUCTO.getValueAt(fila, 3) ));
                this.frmproducto.__lista_categorias.setSelectedItem(  String.valueOf( this.frmproducto.__TABLA_PRODUCTO.getValueAt(fila, 4) ) );                
             }
        }
    }

    public void mousePressed(MouseEvent e){/** nada por aqui */}

    public void mouseReleased(MouseEvent e){/** nada por aqui */}

    public void mouseEntered(MouseEvent e){/** nada por aqui */}

    public void mouseExited(MouseEvent e){/** nada por aqui */}

    /** Inicia todos las acciones y listener de la vista */
    public void iniciar()
    {
        //frmprincipal.setVisible(true); //si no se utiliza SKIN
         // Skin tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(frmprincipal);
            SwingUtilities.updateComponentTreeUI( this.frmproducto );
            SwingUtilities.updateComponentTreeUI( this.frmventa );
            SwingUtilities.updateComponentTreeUI( this.frmcompra );
            SwingUtilities.updateComponentTreeUI( this.frmconsulta );
            this.frmprincipal.setLocationRelativeTo(null);
            this.frmprincipal.setTitle("Control de Inventario");
            this.frmprincipal.setVisible(true);
        } catch (UnsupportedLookAndFeelException ex) {}
          catch (ClassNotFoundException ex) {}
          catch (InstantiationException ex) {}
          catch (IllegalAccessException ex) {}

        //boton
        this.frmprincipal.__VER_PRODUCTOS.setActionCommand( "__VER_PRODUCTOS" );
        this.frmprincipal.__VER_PRODUCTOS.addActionListener(this);
        //boton
        this.frmprincipal.__VER_NUEVA_VENTA.setActionCommand( "__VER_NUEVA_VENTA" );
        this.frmprincipal.__VER_NUEVA_VENTA.addActionListener(this);
        //boton
        this.frmprincipal.__VER_NUEVA_COMPRA.setActionCommand( "__VER_NUEVA_COMPRA" );
        this.frmprincipal.__VER_NUEVA_COMPRA.addActionListener(this);
        //boton
        this.frmprincipal.__CONSULTAS.setActionCommand("__CONSULTAS");
        this.frmprincipal.__CONSULTAS.addActionListener(this);

         //escucha eventos del raton sobre la tabla
        this.frmproducto.__TABLA_PRODUCTO.addMouseListener(this);
        //comando y listener para boton agregar producto
        this.frmproducto.__AGREGAR_PRODUCTO.setActionCommand("__AGREGAR_PRODUCTO");
        this.frmproducto.__AGREGAR_PRODUCTO.addActionListener(this);
        //comando y listener para boton actualizar producto
        this.frmproducto.__ACTUALIZAR_PRODUCTO.setActionCommand("__ACTUALIZAR_PRODUCTO");
        this.frmproducto.__ACTUALIZAR_PRODUCTO.addActionListener(this);

        //boton VISTA frmVenta
        this.frmventa.__NUEVA_VENTA.setActionCommand("__NUEVA_VENTA");
        this.frmventa.__NUEVA_VENTA.addActionListener(this);
        //jcombobox frmVenta
        this.frmventa.__lista_productos.setActionCommand("__lista_productos");
        this.frmventa.__lista_productos.addActionListener(this);
        //boton frmVenta
        this.frmventa.__AGREGAR_A_LISTA.setActionCommand("__AGREGAR_A_LISTA");
        this.frmventa.__AGREGAR_A_LISTA.addActionListener(this);
        //boton frmVenta
        this.frmventa.__REMOVER_PRODUCTO.setActionCommand("__REMOVER_PRODUCTO");
        this.frmventa.__REMOVER_PRODUCTO.addActionListener(this);
        //botn frmVenta
        this.frmventa.__REGISTRAR_VENTA.setActionCommand("__REGISTRAR_VENTA");
        this.frmventa.__REGISTRAR_VENTA.addActionListener(this);
       
        //boton VISTA frmCompra
        this.frmcompra.__NUEVA_COMPRA.setActionCommand("__NUEVA_COMPRA");
        this.frmcompra.__NUEVA_COMPRA.addActionListener(this);
        //jcombobox frmVenta
        this.frmcompra.__lista_productos_compra.setActionCommand("__lista_productos_compra");
        this.frmcompra.__lista_productos_compra.addActionListener(this);
        //boton frmVenta
        this.frmcompra.__AGREGAR_A_LISTA_COMPRA.setActionCommand("__AGREGAR_A_LISTA_COMPRA");
        this.frmcompra.__AGREGAR_A_LISTA_COMPRA.addActionListener(this);
        //boton frmVenta
        this.frmcompra.__REMOVER_PRODUCTO.setActionCommand("__REMOVER_PRODUCTO");
        this.frmcompra.__REMOVER_PRODUCTO.addActionListener(this);
        //botn frmVenta
        this.frmcompra.__REGISTRAR_COMPRA.setActionCommand("__REGISTRAR_COMPRA");
        this.frmcompra.__REGISTRAR_COMPRA.addActionListener(this);
       
        //boton frmConsulta
        this.frmconsulta.__PROCESAR_CONSULTA.setActionCommand("__PROCESAR_CONSULTA");
        this.frmconsulta.__PROCESAR_CONSULTA.addActionListener(this);
        //boton frmConsulta
        this.frmconsulta.__PROCESAR_CONSULTA2.setActionCommand("__PROCESAR_CONSULTA2");
        this.frmconsulta.__PROCESAR_CONSULTA2.addActionListener(this);
    }

    /** Gestion de accciones */
    public void actionPerformed(ActionEvent e) {
        producto tmp;
        switch ( Accion.valueOf( e.getActionCommand() ) )
        {
            case __VER_PRODUCTOS:
                this.frmproducto.setLocationRelativeTo(null);
                this.frmproducto.setTitle("Catalogo de Productos");
                this.frmproducto.setVisible(true);
                this.frmproducto.__TABLA_PRODUCTO.setModel( this.mimodelo.getTablaProducto() );
                this.frmproducto.__lista_categorias.setModel( this.mimodelo.getListaCategorias() );
                break;
            case __VER_NUEVA_VENTA:
                this.frmventa.setLocationRelativeTo(null);
                this.frmventa.setTitle("Registra Venta");
                this.frmventa.__id_venta.setText( this.mimodelo.getRandomCode() );
                this.frmventa.__fecha_venta.setText( this.mimodelo.getFecha() );
                this.frmventa.__lista_productos.setModel( this.mimodelo.getListaProductos() );
                this.frmventa.__lista_clientes.setModel( this.mimodelo.getListaClientes() );
                this.carrito_compra.clear();
                this.frmventa.__detalle_productos.setModel(new DefaultComboBoxModel());
                this.frmventa.setVisible(true);
                break;
            case __VER_NUEVA_COMPRA:
                // JOptionPane.showMessageDialog(null,"you can do it");
                this.frmcompra.setLocationRelativeTo(null);
                this.frmcompra.setTitle("Registra Compra");
                this.frmcompra.__id_venta.setText( this.mimodelo.getRandomCode() );
                this.frmcompra.__fecha_venta.setText( this.mimodelo.getFecha() );
                this.frmcompra.__lista_productos_compra.setModel( this.mimodelo.getListaProductos() );
                this.frmcompra.__lista_proveedores.setModel( this.mimodelo.getListaProveedores() );
                this.carrito_compra.clear();
                this.frmcompra.__detalle_productos.setModel(new DefaultComboBoxModel());
                this.frmcompra.setVisible(true);
                break;
            case __CONSULTAS:
                this.frmconsulta.setLocationRelativeTo(null);
                this.frmconsulta.setTitle("Consultas");
                this.frmconsulta.__fecha1.setDateFormatString("yyyy-MM-dd");
                this.frmconsulta.__fecha2.setDateFormatString("yyyy-MM-dd");
                this.frmconsulta.__fecha1.setDate( new Date() );
                this.frmconsulta.__fecha2.setDate( new Date() );
                this.frmconsulta.setVisible(true);
                break;
//----------------------------------------------------------------- Soy una barra separadora pobre :)
            case __AGREGAR_PRODUCTO:
                //obtiene ID de producto
                String cat[] = this.frmproducto.__lista_categorias.getSelectedItem().toString().split("-");
                if ( this.mimodelo.NuevoProducto(
                        this.frmproducto.__id.getText(),
                        this.frmproducto.__nombre.getText() ,
                        this.frmproducto.__precio.getText(),
                        this.frmproducto.__cantidad.getText() ,
                        cat[0].trim()
                      ) )
                {
                    this.frmproducto.__TABLA_PRODUCTO.setModel( this.mimodelo.getTablaProducto() ); //actualiza JTable
                    JOptionPane.showMessageDialog(null,"Nuevo producto agregado");
                }
                else
                    JOptionPane.showMessageDialog(null,"Error: Verifique los datos del nuevo producto");
                break;
            case __ACTUALIZAR_PRODUCTO:
                String cat2[] = this.frmproducto.__lista_categorias.getSelectedItem().toString().split("-");
                if( this.mimodelo.ActualizarProducto(
                        this.frmproducto.__id.getText(),
                        this.frmproducto.__nombre.getText() ,
                        this.frmproducto.__precio.getText(),
                        this.frmproducto.__cantidad.getText() ,
                        cat2[0].trim()
                        ))
                {
                    this.frmproducto.__TABLA_PRODUCTO.setModel( this.mimodelo.getTablaProducto() ); //actualiza JTable
                    JOptionPane.showMessageDialog(null,"Registro actualizado");
                }
                break;
//----------------------------------------------------------------- Soy una barra separadora pobre :)
            case __NUEVA_VENTA:
                //asigna un nuevo codigo aleatorio al control
                this.frmventa.__id_venta.setText( this.mimodelo.getRandomCode() );
                //muestra la fecha actual en el control
                this.frmventa.__fecha_venta.setText( this.mimodelo.getFecha() );
                //limpia carrito MAP, jcombobox y demas controles
                this.carrito_compra.clear();
                this.frmventa.__detalle_productos.setModel(new DefaultComboBoxModel());
                this.frmventa.__cantidad.setText("0");
                this.frmventa.__idp.setText("");
                this.frmventa.__stock.setText("0");
                this.frmventa.__precio_unidad.setText("0");
                this.frmventa.__total.setText("0" );
                this.frmventa.__detalle.setText("");
                this.frmventa.__REGISTRAR_VENTA.setEnabled(true);
                break;
            case __NUEVA_COMPRA:
                //asigna un nuevo codigo aleatorio al control
                this.frmcompra.__id_venta.setText( this.mimodelo.getRandomCode() );
                //muestra la fecha actual en el control
                this.frmcompra.__fecha_venta.setText( this.mimodelo.getFecha() );
                //limpia carrito MAP, jcombobox y demas controles
                this.carrito_compra.clear();
                this.frmcompra.__detalle_productos.setModel(new DefaultComboBoxModel());
                this.frmcompra.__cantidad.setText("0");
                this.frmcompra.__idp.setText("");
                this.frmcompra.__stock.setText("0");
                this.frmcompra.__precio_unidad.setText("0");
                this.frmcompra.__total.setText("0" );
                this.frmcompra.__detalle.setText("");
                this.frmcompra.__REGISTRAR_COMPRA.setEnabled(true);
                break;
            case __lista_productos://cuando se hace clic en algun producto del form VENTA
                //obtiene datos de producto
                tmp = this.mimodelo.getProducto( this.frmventa.__lista_productos.getSelectedItem().toString() );
                //coloca los datos en los controles
                this.frmventa.__idp.setText( String.valueOf( tmp.getId() ) );
                this.frmventa.__stock.setText( String.valueOf( tmp.getStock()) );
                this.frmventa.__precio_unidad.setText( String.valueOf( tmp.getPrecio() ) );
                break;
            case __lista_productos_compra://cuando se hace clic en algun producto del form VENTA
                //obtiene datos de producto
                tmp = this.mimodelo.getProducto( this.frmcompra.__lista_productos_compra.getSelectedItem().toString() );
                //coloca los datos en los controles
                this.frmcompra.__idp.setText( String.valueOf( tmp.getId() ) );
                this.frmcompra.__stock.setText( String.valueOf( tmp.getStock()) );
                this.frmcompra.__precio_unidad.setText( String.valueOf( tmp.getPrecioC() ) );
                break;
            case __AGREGAR_A_LISTA:
                //controla que se halla seleccionado un producto y
                if( !this.frmventa.__idp.getText().equals("") && !this.frmventa.__cantidad.getText().equals("0"))
                {
                    //controla stock de producto
                    if( Integer.valueOf(this.frmventa.__stock.getText()) > Integer.valueOf(this.frmventa.__cantidad.getText()) )
                    {
                        //nueva instancia a carrito de compras, se cargan datos en la clase
                        carrito tmp_carrito = new carrito();
                        tmp_carrito.setIdproducto( this.frmventa.__idp.getText() );
                        tmp_carrito.setDescripcion( this.frmventa.__lista_productos.getSelectedItem().toString() );
                        tmp_carrito.setPrecio( this.frmventa.__precio_unidad.getText() );
                        tmp_carrito.setCantidad( this.frmventa.__cantidad.getText() );
                        //se añade clase a MAP
                        this.carrito_compra.put( this.frmventa.__idp.getText() , tmp_carrito);
                        //segun el MAP se actualiza la vista del jcombobox
                        this.frmventa.__detalle_productos.setModel( this.mimodelo.ListaProductos(carrito_compra) );
                        //asigna el total de la venta en control
                        this.frmventa.__total.setText( "" + this.mimodelo.getTotalVenta(carrito_compra) );
                    }
                    else JOptionPane.showMessageDialog(null,"Error: No existe stock suficiente para este producto.");
                }
                else JOptionPane.showMessageDialog(null,"Error: Verifique sus datos");
                break;
            case __AGREGAR_A_LISTA_COMPRA:
                //controla que se halla seleccionado un producto y
                if( !this.frmcompra.__idp.getText().equals("") && !this.frmcompra.__cantidad.getText().equals("0"))
                {
                    //controla stock de producto
                    if( Integer.valueOf(this.frmcompra.__cantidad.getText()) > 0 )
                    {
                        //nueva instancia a carrito de compras, se cargan datos en la clase
                        carrito tmp_carrito = new carrito();
                        tmp_carrito.setIdproducto( this.frmcompra.__idp.getText() );
                        tmp_carrito.setDescripcion( this.frmcompra.__lista_productos_compra.getSelectedItem().toString() );
                        tmp_carrito.setPrecio( this.frmcompra.__precio_unidad.getText() );
                        tmp_carrito.setCantidad( this.frmcompra.__cantidad.getText() );
                        //se añade clase a MAP
                        this.carrito_compra.put( this.frmcompra.__idp.getText() , tmp_carrito);
                        //segun el MAP se actualiza la vista del jcombobox
                        this.frmcompra.__detalle_productos.setModel( this.mimodelo.ListaProductos(carrito_compra) );
                        //asigna el total de la venta en control
                        this.frmcompra.__total.setText( "" + this.mimodelo.getTotalVenta(carrito_compra) );
                    }
                    else JOptionPane.showMessageDialog(null,"Error: Favor de verificar la cantidad." + this.frmcompra.__cantidad.getText());
                }
                else JOptionPane.showMessageDialog(null,"Error: Verifique sus datos (c)");
                break;
            case __REMOVER_PRODUCTO:
                JOptionPane.showMessageDialog(null,"Fase II");
                break;
            case __REGISTRAR_VENTA:
                //comprueba que existan productos en el carrito
                if( !this.carrito_compra.isEmpty())
                {
                    //obtiene ID del cliente
                    String idcliente[] = this.frmventa.__lista_clientes.getSelectedItem().toString().split("-");
                    if ( this.mimodelo.RegistrarVenta(this.frmventa.__id_venta.getText(),
                                             carrito_compra,
                                             this.frmventa.__total.getText(),
                                             this.frmventa.__detalle.getText(),
                                             idcliente[1].trim()
                                             ))
                    {
                        JOptionPane.showMessageDialog(null,"Nueva Venta registrada");
                        this.frmventa.__REGISTRAR_VENTA.setEnabled(false);
                    }
                }
                else JOptionPane.showMessageDialog(null,"Error: Sin productos.");
                break;
            case __REGISTRAR_COMPRA:
                //comprueba que existan productos en el carrito
                if( !this.carrito_compra.isEmpty())
                {
                    //obtiene ID del cliente
                    String idprov[] = this.frmcompra.__lista_proveedores.getSelectedItem().toString().split("-");
                    if ( this.mimodelo.RegistrarCompra(this.frmcompra.__id_venta.getText(),
                                             carrito_compra,
                                             this.frmcompra.__total.getText(),
                                             this.frmcompra.__detalle.getText(),
                                             idprov[1].trim()
                                             ))
                    {
                        JOptionPane.showMessageDialog(null,"Nueva Compra registrada");
                        this.frmcompra.__REGISTRAR_COMPRA.setEnabled(false);
                    }
                }
                else JOptionPane.showMessageDialog(null,"Error: Sin productos.");
                break;
//----------------------------------------------------------------- Soy una barra separadora pobre :)
            case __PROCESAR_CONSULTA:
                String r = this.mimodelo.getDatosVenta(this.frmconsulta.__idventa.getText());
                if( !r.equals("") )
                {
                    this.frmconsulta.__resultadoTabla.setModel( this.mimodelo.getTablaVenta( this.frmconsulta.__idventa.getText() ) );
                    this.frmconsulta.__resultadoTexto.setText( r );
                }
                else
                {
                    this.frmconsulta.__resultadoTabla.setModel( new DefaultTableModel() );
                    this.frmconsulta.__resultadoTexto.setText( "" );
                    JOptionPane.showMessageDialog(null,"No existen registros");
                }
                break;
            case __PROCESAR_CONSULTA2:
                this.frmconsulta.__resultadoTabla.setModel(
                        this.mimodelo.getDatosVentaxFechas( this.frmconsulta.__fecha1.getDate(), this.frmconsulta.__fecha2.getDate() ) );
                this.frmconsulta.__resultadoTexto.setText("");
                break;
        }//-> fin case

    }

}
