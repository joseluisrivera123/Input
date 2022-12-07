
package model;

import java.util.Date;
import java.util.GregorianCalendar;
import lombok.Data;

@Data
public class Venta {
    /*Variable tipo date*/
    Date fechaVenta = GregorianCalendar.getInstance().getTime();
    
    /*Variable de tipo entero*/
    int idVenta;
    int idVendedor;
    int idCliente;
    int cantidad;
    
    /*Variable de tipo decimal*/
    double precio;
    double subtotal;
    
    /*Variable para consumir el api*/
    double cambio;
    double convertido;
    double pago;
    double vuelto;
    
    /*Nombre(s) y apellidos*/
    String cliente;
    String vendedor;
    String producto;
}
